package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTournamentRoomBinding
import com.nuevo.gameness.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TournamentRoomFragment: BaseFragment<FragmentTournamentRoomBinding>() {

    private val viewModel by viewModels<TournamentRoomViewModel>()

    override fun getViewBinding() = FragmentTournamentRoomBinding.inflate(layoutInflater)

    override fun initView() {
        viewModel.tournamentId = requireArguments().getString("id")
        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getTournament.fetchData()

        viewModel.getTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        state.data.data?.let { item ->
                            binding.apply {
                                if (item.userJoined == true) {
                                    layoutJoinTournamentWarning.visibility = View.GONE
                                    buttonLoginToTheTournamentRoom.visibility = View.VISIBLE
                                } else {
                                    layoutJoinTournamentWarning.visibility = View.VISIBLE
                                    buttonLoginToTheTournamentRoom.visibility = View.GONE
                                }
                            }
                        }
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun events() {
        binding.apply {
            buttonLoginToTheTournamentRoom.setOnClickListener {
                navigate(
                    R.id.action_tournamentDetailFragment_to_tournamentRoomHomeFragment,
                    bundleOf("id" to viewModel.tournamentId)
                )
            }
        }
    }

}