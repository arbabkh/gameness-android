package com.nuevo.gameness.ui.pages.personal.profile.selectedgames

import androidx.fragment.app.viewModels
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentSelectedGamesBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.register.choosegame.ChooseGameAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectedGamesFragment : BaseFragment<FragmentSelectedGamesBinding>() {

    private val viewModel by viewModels<SelectedGamesViewModel>()
    private val selectedGamesAdapter = ChooseGameAdapter(0, true)

    override fun getViewBinding() = FragmentSelectedGamesBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getMyGameList.fetchData()

        viewModel.getMyGameList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        selectedGamesAdapter.updateAllData(items)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun events() {
        binding.apply {
            recyclerViewSelectedGames.adapter = selectedGamesAdapter

            imageViewIcBack.setOnClickListener { popBackStack() }
        }
    }

}