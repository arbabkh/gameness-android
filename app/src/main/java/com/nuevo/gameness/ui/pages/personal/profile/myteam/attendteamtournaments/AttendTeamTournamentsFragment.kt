package com.nuevo.gameness.ui.pages.personal.profile.myteam.attendteamtournaments

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentItem
import com.nuevo.gameness.data.model.tournaments.TournamentsStatusFilter
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentAttendTeamTournamentsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.AllTournamentsAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.adapter.ButtonFilterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AttendTeamTournamentsFragment : BaseFragment<FragmentAttendTeamTournamentsBinding>() {

    private val viewModel by viewModels<AttendTeamTournamentsViewModel>()

    private lateinit var filterList: ArrayList<TournamentsStatusFilter>
    private val filterAdapter = ButtonFilterAdapter()

    private val teamTournamentsList = arrayListOf<TournamentItem>()
    private val teamTournamentsAdapter = AllTournamentsAdapter()

    override fun getViewBinding() = FragmentAttendTeamTournamentsBinding.inflate(layoutInflater)

    override fun initView() {
        filterList = arrayListOf(
            TournamentsStatusFilter(getString(R.string.all), null, true),
            TournamentsStatusFilter(getString(R.string.active), 1, false),
            TournamentsStatusFilter(getString(R.string.ending), 2, false)
        )
        filterAdapter.updateAllData(filterList)

        events()
        fetchData()
    }

    private fun events(){
        binding.apply {
            recyclerTournamentFilter.adapter = filterAdapter
            recyclerTeamTournaments.adapter = teamTournamentsAdapter

            filterAdapter.onItemClickListener = { pos, item ->
                viewModel.teamTournamentsList.fetchData()

                for (filter in filterList) filter.selected = false
                item.selected = true
                filterList[pos] = item
                filterAdapter.updateAllData(filterList)
            }

            teamTournamentsAdapter.onItemClickListener = { _, item ->
                navigate(R.id.nav_tournaments, bundleOf(
                    "navigate_id" to R.id.tournamentDetailFragment,
                    "id" to item.id
                ))
            }
        }
    }

    private fun fetchData(){
        viewModel.teamTournamentsList.fetchData()

        viewModel.teamTournamentsList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        val filter = filterList.find { it.selected == true }?.value
                        teamTournamentsList.clear()
                        teamTournamentsList.addAll(
                            if (filter != null) items.filter { item ->
                                item.tournamentStatus == filter
                            }
                            else items
                        )
                        teamTournamentsAdapter.updateAllData(teamTournamentsList)
                        binding.txtTeamTournamentsWarning.visibility = if (teamTournamentsList.isEmpty()) {
                            binding.txtTeamTournamentsWarning.text = when(filter) {
                                2 -> getString(R.string.not_found_finished_tournaments)
                                1 -> getString(R.string.not_found_active_tournaments)
                                else -> getString(R.string.not_found_tournaments)
                            }
                            View.VISIBLE
                        } else View.GONE
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

}