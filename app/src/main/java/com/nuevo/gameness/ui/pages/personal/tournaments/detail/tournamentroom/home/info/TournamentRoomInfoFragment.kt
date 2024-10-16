package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.info

import android.view.View
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTournamentRoomInfoBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.load
import com.nuevo.gameness.utils.myToString
import com.nuevo.gameness.utils.toCalendar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TournamentRoomInfoFragment : BaseFragment<FragmentTournamentRoomInfoBinding>() {

    private val viewModel by viewModels<TournamentRoomInfoViewModel>()

    override fun getViewBinding() = FragmentTournamentRoomInfoBinding.inflate(layoutInflater)

    override fun initView() {
        viewModel.tournamentId = requireArguments().getString("id")
        fetchData()
    }

    private fun fetchData() {
        viewModel.getTournament.fetchData()
        viewModel.getLastMatch.fetchData()
        viewModel.getTournamentStage.fetchData()
        viewModel.isJoinedTournament.fetchData()

        viewModel.getTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        state.data.data?.let { item ->
                            binding.apply {
                                textViewTournamentRoomInfoGame.text = item.gameName
                                textViewTournamentRoomInfoTournament.text = item.name
                                textViewTournamentRoomInfoDate.text = item.startDate?.toCalendar()?.time.myToString("dd.MM.yyyy HH:mm")
                            }
                        }
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getLastMatch.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        binding.layoutLastMatch.visibility = View.VISIBLE
                        state.data.data?.let { data ->
                            binding.imageViewTournamentTheLastMatchHome.load(requireContext(), data.homeLogoUrl)
                            binding.imageViewTournamentTheLastMatchAway.load(requireContext(), data.awayLogoUrl)
                            binding.textViewTournamentTheLastMatchScore.apply {
                                text = data.homeScore.toString()
                                append(" - ")
                                append(data.awayScore.toString())
                            }
                            binding.textViewTournamentTheLastMatchHome.text = data.homeName
                            binding.textViewTournamentTheLastMatchAway.text = data.awayName
                        }
                    } else binding.layoutLastMatch.visibility = View.GONE
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getTournamentStage.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        binding.textViewTournamentRoomInfoStage.text =
                            when(state.data.data?.stage) {
                                1 -> getString(R.string.participation_phase)
                                2 -> getString(R.string.continues)
                                3 -> getString(R.string.paused)
                                4 -> getString(R.string.eliminated)
                                5 -> getString(R.string.completed)
                                else -> state.data.data?.stage.toString()
                            }
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.isJoinedTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        binding.textViewTournamentRoomInfoYourAttendanceStatus.text =
                            when(state.data.data?.userJoinStatus) {
                                0 -> getString(R.string.not_joined)
                                1 -> getString(R.string.joined)
                                2 -> getString(R.string.waiting_for_approval)
                                else -> state.data.data?.userJoinStatus.toString()
                            }
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

}