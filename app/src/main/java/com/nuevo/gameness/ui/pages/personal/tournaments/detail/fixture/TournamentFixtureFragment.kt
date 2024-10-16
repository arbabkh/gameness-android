package com.nuevo.gameness.ui.pages.personal.tournaments.detail.fixture

import android.view.View
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.tournaments.TournamentData
import com.nuevo.gameness.data.model.tournaments.TournamentFixtureData
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTournamentFixtureBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.tournaments.detail.fixture.adapter.TournamentFixtureSectionAdapter
import com.nuevo.gameness.utils.setMyBackgroundTintList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TournamentFixtureFragment : BaseFragment<FragmentTournamentFixtureBinding>() {

    private val viewModel by viewModels<TournamentFixtureViewModel>()
    private var tournamentData: TournamentData? = null
    private var tournamentFixtureList: List<TournamentFixtureData> = listOf()
    private var tournamentFixtureSectionAdapter: TournamentFixtureSectionAdapter? = null

    override fun getViewBinding() = FragmentTournamentFixtureBinding.inflate(layoutInflater)

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
                        tournamentData = state.data.data
                        if (tournamentData?.tournamentType == 2) {
                            binding.buttonUpperBracket.visibility = View.VISIBLE
                            binding.buttonLowerBracket.visibility = View.VISIBLE
                        } else {
                            binding.buttonUpperBracket.visibility = View.GONE
                            binding.buttonLowerBracket.visibility = View.GONE
                        }
                        tournamentFixtureSectionAdapter = TournamentFixtureSectionAdapter(
                            listOf(
                                getString(R.string.third_place_match),
                                getString(R.string.grand_final),
                                getString(R.string.final_text),
                                getString(R.string.semifinal),
                                getString(R.string.quarter_final)
                            ),
                            tournamentData?.tournamentType
                        )
                        binding.recyclerViewTournamentFixture.adapter = tournamentFixtureSectionAdapter
                        viewModel.getTournamentFixture.fetchData()
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getTournamentFixture.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.data?.let { items ->
                        tournamentFixtureList = items
                        tournamentFixtureSectionAdapter?.updateAllData(
                            if (tournamentData?.tournamentType == 2)
                                tournamentFixtureList.filter { item ->
                                    item.isLowerBracket == false
                                }
                            else tournamentFixtureList
                        )
                        binding.textViewFixtureHasNotBeenFormedYet.visibility =
                            if (tournamentFixtureList.isEmpty()) View.VISIBLE
                            else View.GONE
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
            buttonLowerBracket.setOnClickListener {
                buttonLowerBracket.setMyBackgroundTintList(context, R.color.blue)
                buttonUpperBracket.setMyBackgroundTintList(context, R.color.tertiary_dark)
                tournamentFixtureSectionAdapter?.updateAllData(tournamentFixtureList.filter { item ->
                    item.isLowerBracket == true
                })
            }
            buttonUpperBracket.setOnClickListener {
                buttonLowerBracket.setMyBackgroundTintList(context, R.color.tertiary_dark)
                buttonUpperBracket.setMyBackgroundTintList(context, R.color.blue)
                tournamentFixtureSectionAdapter?.updateAllData(tournamentFixtureList.filter { item ->
                    item.isLowerBracket == false
                })
            }
        }
    }

}