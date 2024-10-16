package com.nuevo.gameness.ui.pages.personal.tournaments.detail

import android.text.Html
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentData
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTournamentDetailBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TournamentDetailFragment : BaseFragment<FragmentTournamentDetailBinding>() {

    private val viewModel by viewModels<TournamentDetailViewModel>()
    private var tournamentData: TournamentData? = null

    override fun getViewBinding() = FragmentTournamentDetailBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(visible = true) { popBackStack() }

        viewModel.id = requireArguments().getString("id")
        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getTournament.fetchData()

        viewModel.getTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        binding.apply {
                            state.data.data?.let { tournamentItem ->
                                this@TournamentDetailFragment.tournamentData = tournamentItem
                                viewPager2TournamentDetail.adapter = TournamentDetailAdapter(
                                    this@TournamentDetailFragment,
                                    bundleOf("id" to state.data.data.id)
                                )
                                TabLayoutMediator(tabLayoutTournamentDetail, viewPager2TournamentDetail) { tab, position ->
                                    tab.text = when(position) {
                                        2 -> getString(R.string.tournament_room)
                                        1 -> getString(R.string.fixture)
                                        else -> getString(R.string.tournament_information)
                                    }
                                }.attach()
                                imageViewTournamentDetail.load(requireContext(), tournamentItem.detailImageUrl)
                                tournamentItem.randomParticipantImages?.let { images ->
                                    for (index in images.indices) {
                                        when(index) {
                                            0 -> imageViewTournament1
                                            1 -> imageViewTournament2
                                            2 -> imageViewTournament3
                                            else -> null
                                        }?.apply {
                                            load(context, images[index].imageUrl)
                                            visibility = View.VISIBLE
                                        }
                                    }
                                }
                                textViewTournamentDate.apply {
                                    text = tournamentItem.registrationStartDate.toCalendar()?.time.myToString("d")
                                    append(" - ")
                                    append(tournamentItem.registrationEndDate.toCalendar()?.time.myToString("d MMMM yyyy"))
                                }
                                textViewParticipantCount.text = getString(R.string.participant_count, tournamentItem.currentParticipantCount)
                                textViewTournamentName.text = tournamentItem.name
                                textViewTournamentGameName.text = Html.fromHtml(tournamentItem.shortDescription)
                                textViewTournamentGameName.linksClickable=true
                                /**
                                 * item.userJoinStatus
                                 * 0 -> Katil
                                 * 1 -> Katildin
                                 * 2 -> Beklemede (Onay Bekliyor)
                                 */
                                if (tournamentItem.userJoinStatus == 0) {
                                    tournamentItem.registrationEndDate.toCalendar()?.let { date ->
                                        if (date.time < Calendar.getInstance().time) {
                                            buttonTournamentJoin.visibility = View.INVISIBLE
                                        } else {
                                            buttonTournamentJoin.visibility = View.VISIBLE
                                            buttonTournamentJoin.text = requireContext().getString(R.string.join)
                                            buttonTournamentJoin.setMyBackgroundTintList(context, R.color.blue)
                                        }
                                    }
                                } else {
                                    if (tournamentItem.cancelButtonActive == true) {
                                        buttonTournamentJoin.visibility = View.VISIBLE
                                        buttonTournamentJoin.setMyBackgroundTintList(context, R.color.tertiary_dark)
                                        if (tournamentItem.userJoinStatus == 1)
                                            buttonTournamentJoin.text = requireContext().getString(R.string.joined)
                                        else if (tournamentItem.userJoinStatus == 2)
                                            buttonTournamentJoin.text = requireContext().getString(R.string.waiting)
                                    } else buttonTournamentJoin.visibility = View.INVISIBLE
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
        viewModel.joinTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        viewModel.getTournament.fetchData()
                        showToast(getString(R.string.successful))
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.cancelTournament.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) {
                        viewModel.getTournament.fetchData()
                        showToast(getString(R.string.successful))
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
            buttonTournamentJoin.setOnClickListener {
                when(tournamentData?.userJoinStatus) {
                    0 -> { // Katil
                        viewModel.joinTournament.fetchData()
                    }
                    1 -> { // Katildin
                        showToast(getString(R.string.joined_tournament_warning))
                    }
                    2 -> { // Beklemede (Onay Bekliyor)
                        showQuestionDialog(getString(R.string.waiting_tournament_warning)) {
                            viewModel.cancelTournament.fetchData()
                        }
                    }
                }
            }
        }
    }

}