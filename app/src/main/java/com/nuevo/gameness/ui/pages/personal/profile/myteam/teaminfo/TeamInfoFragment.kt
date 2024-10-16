package com.nuevo.gameness.ui.pages.personal.profile.myteam.teaminfo

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.profile.AwardsItem
import com.nuevo.gameness.data.model.tournaments.TournamentItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTeamInfoBinding
import com.nuevo.gameness.databinding.ItemAllAwardsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.adapter.AllAwardsAdapter
import com.nuevo.gameness.ui.pages.personal.profile.adapter.AllTournamentsAdapter
import com.nuevo.gameness.utils.load
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamInfoFragment:BaseFragment<FragmentTeamInfoBinding>() {

    private val viewModel by viewModels<TeamInfoViewModel>()

    private val teamTournamentsList = arrayListOf<TournamentItem>()
    private val teamTournamentsAdapter = AllTournamentsAdapter(isViewTypePagerView = true)

    private val allAwardsAdapter = AllAwardsAdapter()
    private val teamAwardsList = arrayListOf<AwardsItem>()

    override fun getViewBinding() = FragmentTeamInfoBinding.inflate(layoutInflater)

    override fun initView() {
        events()
        fetchData()
    }

    private fun events(){
        binding.apply {
           viewpagerTeamTournaments.adapter = teamTournamentsAdapter

            teamTournamentsAdapter.onItemClickListener = { _, item ->
                navigate(R.id.nav_tournaments, bundleOf(
                    "navigate_id" to R.id.tournamentDetailFragment,
                    "id" to item.id
                ))
            }

            allAwardsAdapter.onItemClickListener = { _, item ->
                val dialogBinding = ItemAllAwardsBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(dialogBinding.root)
                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBinding.apply {
                    imgAward.load(requireContext() ,item.iconImageURL)
                    (this.root.layoutParams as ViewGroup.MarginLayoutParams).apply {
                        marginStart = 10
                        marginEnd = 10
                    }
                    txtAwardTitle.text = item.gameName
                    txtAwardDesc.text = item.awardName
                    imageViewClose.visibility = View.VISIBLE
                    imageViewClose.setOnClickListener { dialog.dismiss() }
                }
            }
        }
    }

    private fun fetchData(){
        viewModel.teamTournamentsList.fetchData()
        viewModel.teamAwardList.fetchData()

        //son turnuvalar
        viewModel.teamTournamentsList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.items?.let { items ->
                            val filteredList = items.filter { item ->
                                item.tournamentStatus == 1 || item.tournamentStatus == 2
                            }
                            if(filteredList.isNotEmpty()){
                                teamTournamentsList.clear()
                                teamTournamentsList.addAll(filteredList)
                                teamTournamentsAdapter.updateAllData(filteredList as ArrayList<TournamentItem>)

                            }else{
                                txtTeamTournamentsWarning.visibility= View.VISIBLE
                                viewpagerTeamTournaments.visibility= View.GONE
                            }

                        }
                    }

                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }

        }

        //son başarımlar
        viewModel.teamAwardList.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.apply {
                        state.data.items.let { items ->
                            if(!items.isNullOrEmpty()){
                                teamAwardsList.clear()
                                teamAwardsList.addAll(items)
                                allAwardsAdapter.updateAllData(teamAwardsList)
                                recyclerAwards.adapter=allAwardsAdapter

                            }else{
                                txtAwardsWarning.visibility= View.VISIBLE
                                recyclerAwards.visibility= View.GONE
                            }
                        }
                    }

                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }

        }

    }
}