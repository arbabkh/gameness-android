package com.nuevo.gameness.ui.pages.personal.profile.myprofile.attendtournaments

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentItem
import com.nuevo.gameness.data.model.tournaments.TournamentsStatusFilter
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentAttendTournamentsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.AllTournamentsAdapter
import com.nuevo.gameness.ui.pages.personal.tournaments.adapter.ButtonFilterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AttendTournamentsFragment : BaseFragment<FragmentAttendTournamentsBinding>() {

    private val viewModel by viewModels<AttendTournamentsViewModel>()

    private val filterAdapter = ButtonFilterAdapter()
    private lateinit var filterList: ArrayList<TournamentsStatusFilter>

    private val tournamentsAdapter= AllTournamentsAdapter()
    private val tournamentsList= arrayListOf<TournamentItem>()

    override fun getViewBinding() = FragmentAttendTournamentsBinding.inflate(layoutInflater)

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
            recyclerToornaments.adapter = tournamentsAdapter

            filterAdapter.onItemClickListener = { pos, item ->
                viewModel.userTournamentsList.fetchData()

                for (filter in filterList) filter.selected = false
                item.selected = true
                filterList[pos] = item
                filterAdapter.updateAllData(filterList)
            }

            tournamentsAdapter.onItemClickListener = { _, item ->
                navigate(R.id.nav_tournaments, bundleOf(
                    "navigate_id" to R.id.tournamentDetailFragment,
                    "id" to item.id
                ))
            }
        }
    }

    private fun fetchData(){
        viewModel.userTournamentsList.fetchData()

        viewModel.userTournamentsList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        val filter = filterList.find { it.selected == true }?.value
                        tournamentsList.clear()
                        tournamentsList.addAll(
                            if (filter != null) items.filter { item ->
                                item.tournamentStatus == filter
                            }
                            else items
                        )
                        tournamentsAdapter.updateAllData(tournamentsList)
                        binding.txtTournamenstWarning.visibility = if (items.isEmpty()) {
                            binding.txtTournamenstWarning.text = when(filter) {
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