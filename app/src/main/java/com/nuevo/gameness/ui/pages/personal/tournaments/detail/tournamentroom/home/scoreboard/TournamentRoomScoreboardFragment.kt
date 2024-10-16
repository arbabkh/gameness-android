package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.scoreboard

import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTournamentRoomScoreboardBinding
import com.nuevo.gameness.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TournamentRoomScoreboardFragment : BaseFragment<FragmentTournamentRoomScoreboardBinding>() {

    private val viewModel by viewModels<TournamentRoomScoreboardViewModel>()
    private val tournamentRoomScoreboardAdapter = TournamentRoomScoreboardAdapter()

    override fun getViewBinding() = FragmentTournamentRoomScoreboardBinding.inflate(layoutInflater)

    override fun initView() {
        viewModel.tournamentId = arguments?.getString("id")

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getTournament.fetchData()
        viewModel.getScoreboard.fetchData()

        viewModel.getTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        binding.textViewTournamentParticipationType.text =
                            if (state.data.data?.tournamentParticipationType == 2) getString(R.string.team)
                            else getString(R.string.user)
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getScoreboard.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        state.data.data?.let { items ->
                            tournamentRoomScoreboardAdapter.updateAllData(items)
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
            recyclerViewScoreboard.adapter = tournamentRoomScoreboardAdapter
        }
    }

}