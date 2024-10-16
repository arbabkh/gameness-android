package com.nuevo.gameness.ui.pages.personal.profile.settings.game

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.games.GameListItem
import com.nuevo.gameness.data.model.settings.request.SelectedGame
import com.nuevo.gameness.data.model.settings.request.SetUserGamesRequest
import com.nuevo.gameness.data.model.users.SelectedGameRequest
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentGameSettingsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.register.choosegame.ChooseGameAdapter
import com.nuevo.gameness.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameSettingsFragment: BaseFragment<FragmentGameSettingsBinding>() {

    private val viewModel by viewModels<GameSettingsViewModel>()
    private val myGamesAdapter = ChooseGameAdapter(0)
    private val allGameAdapter = ChooseGameAdapter(0)
    private val allGameList = arrayListOf<GameListItem>()

    override fun getViewBinding() = FragmentGameSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        findNavController().currentBackStackEntry
            ?.savedStateHandle?.let { handle ->
                handle.getLiveData<Boolean>("result")
                .observe(viewLifecycleOwner) { res ->

                    if (!res) {
                        return@observe
                    }

                    SELECTED_GAMES_REQUEST?.let {
                        if (it.isNotEmpty()) {
                            val selectedGames = arrayListOf<SelectedGame>()
                            for(item in it) item.gameId.let { id ->
                                selectedGames.add(SelectedGame(id ?: "", item.gameUniqueId))
                            }

                            viewModel.request = SetUserGamesRequest(selectedGames)
                            viewModel.setUserGames.fetchData()
                        }
                    }


                }
            }

    }
    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun events(){
        binding.apply {
            recyclerMyGames.adapter = myGamesAdapter
            recyclerAllGames.adapter = allGameAdapter

            allGameAdapter.onItemClickListener = { _, item ->
                allGameList.find { item.id == it.id }?.selected = item.selected
                buttonColorChange()
            }
            myGamesAdapter.onItemClickListener = { _, item ->
                allGameList.find { item.id == it.id }?.selected = item.selected
                buttonColorChange()
            }

            btnSave.setOnClickListener {
                val selectedGameList = allGameList.filter { it.selected }

                if (selectedGameList.isNotEmpty()) {

                    val selectedGames = arrayListOf<SelectedGame>()
                    for(item in selectedGameList) item.id?.let { id ->
                        selectedGames.add(SelectedGame(id, item.uniqueID))
                    }

                    navigateToGameInformationFragment(selectedGameList)

                } else showDialog(getString(R.string.one_game_must_be_selected))
            }
            icBack.setOnClickListener { popBackStack() }
        }

    }

    private fun fetchData(){
        viewModel.getGameList.fetchData()

        viewModel.getMyGameList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        for (item in items) {
                            allGameList.find { item.id == it.id }?.let {
                                it.selected = true
                                it.uniqueID = item.uniqueID
                            }
                        }

                        allGameList.forEach {
                            if (!it.selected) {
                                it.uniqueID = ""
                            }
                        }

                        myGamesAdapter.updateAllData(allGameList.filter { it.selected })
                        allGameAdapter.updateAllData(allGameList.filter { !it.selected })
                    }


                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.getGameList.state.observe(viewLifecycleOwner) { state->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        allGameList.clear()
                        allGameList.addAll(items)
                        viewModel.getMyGameList.fetchData()
                        binding.btnSave.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.tertiary_dark))
                        binding.btnSave.setMyTextColor(context, R.color.gray)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.setUserGames.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        if (state.data.success == true) {
                            showDialog(getString(R.string.update_your_games))
                            viewModel.getGameList.fetchData()
                        } else showToast(state.data.message.toString())
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun buttonColorChange() {
        binding.apply {
            if (allGameList.any { it.selected }) {
                btnSave.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                btnSave.setMyTextColor(context, R.color.white)
            } else {
                btnSave.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.tertiary_dark))
                btnSave.setMyTextColor(context, R.color.gray)
            }
        }
    }

    private fun  navigateToGameInformationFragment(selectedGames: List<GameListItem>) {
        val args = Bundle()
        args.putParcelableArray("selected_games", selectedGames.toTypedArray())
        args.putBoolean("show_discord_id_edittext", false)
        navigate(R.id.action_gameSettingsFragment_to_gameInformationFragment, args)

    }

    companion object {
        // comes form game information fragment
        var SELECTED_GAMES_REQUEST: ArrayList<SelectedGameRequest>? = null
    }
}