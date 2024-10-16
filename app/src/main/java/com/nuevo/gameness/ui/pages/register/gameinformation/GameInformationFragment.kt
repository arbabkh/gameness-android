package com.nuevo.gameness.ui.pages.register.gameinformation

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.games.GameListItem
import com.nuevo.gameness.data.model.users.SelectedGameRequest
import com.nuevo.gameness.data.model.users.UserGamesForRegisterRequest
import com.nuevo.gameness.databinding.FragmentGameInformationBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.settings.game.GameSettingsFragment
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameInformationFragment : BaseFragment<FragmentGameInformationBinding>() {

    private val gameInformationAdapter = GameInformationAdapter()
    var onNextButtonClickListener: ((request: UserGamesForRegisterRequest) -> Unit)? = null
    var selectedGameList: List<GameListItem>? = null
    private val viewModel by viewModels<GameInformationViewModel>()
    private var showDiscordIdEditText: Boolean = true

    override fun getViewBinding() = FragmentGameInformationBinding.inflate(layoutInflater)

    override fun initView() {
        events()
    }

    private fun events() {
        binding.apply {


            textviewDiscordId.visibility = if(showDiscordIdEditText) View.VISIBLE else View.GONE
            editTextDiscordId.visibility = if(showDiscordIdEditText) View.VISIBLE else View.GONE



            recyclerViewSelectedGameIds.adapter = gameInformationAdapter

            icBack.setOnClickListener {
                findNavController().apply {
                    previousBackStackEntry?.savedStateHandle?.set("result", false)
                }.navigateUp()
            }

            buttonGameInformationNext.setOnClickListener {
                val discordId = editTextDiscordId.text.toString()
                var isEmpty = false
                if (discordId.isEmpty() && showDiscordIdEditText) isEmpty = true
                selectedGameList?.let { itemList ->
                    for(item in itemList) {
                        if (item.uniqueID.isNullOrEmpty()) {
                            isEmpty = true
                            break
                        }
                    }
                }

                if (isEmpty) showDialog(getString(R.string.empty_warning))
                else {
                    val selectedGameRequestList: ArrayList<SelectedGameRequest> = arrayListOf()
                    selectedGameList?.let { itemList ->
                        for (item in itemList) {
                            selectedGameRequestList.add(SelectedGameRequest(item.id, item.uniqueID))
                        }
                    }


                    var request = UserGamesForRegisterRequest(
                        null,
                        editTextDiscordId.text.toString(),
                        selectedGameRequestList
                    )

                    if (onNextButtonClickListener == null) {
                        viewModel.userGamesForRegisterRequest = request
                        findNavController().apply {

                            GameSettingsFragment.SELECTED_GAMES_REQUEST = selectedGameRequestList
                            previousBackStackEntry?.savedStateHandle?.set("result", true)

                        }.navigateUp()
                    } else {
                        onNextButtonClickListener?.invoke(
                            UserGamesForRegisterRequest(
                                null,
                                editTextDiscordId.text.toString(),
                                selectedGameRequestList
                            )
                        )
                    }

                }
            }


            // fetch selectedGames from bundle
            val sg = arguments?.getParcelableArray("selected_games") as Array<GameListItem>?
            showDiscordIdEditText = arguments?.getBoolean("show_discord_id_edittext", true) ?: true
            if(sg != null) {
                selectedGameList = sg.toList()
                updateView()
            }

        }
    }

    fun updateView() {


        binding.textviewTitle2.visibility = if (showDiscordIdEditText) View.VISIBLE else View.GONE
        binding.headerLayout.visibility = if (showDiscordIdEditText) View.GONE else View.VISIBLE

        binding.textviewDiscordId.visibility = if(showDiscordIdEditText) View.VISIBLE else View.GONE
        binding.editTextDiscordId.visibility = if(showDiscordIdEditText) View.VISIBLE else View.GONE

        selectedGameList?.let { list ->
            gameInformationAdapter.updateAllData(list)
        }
    }



}