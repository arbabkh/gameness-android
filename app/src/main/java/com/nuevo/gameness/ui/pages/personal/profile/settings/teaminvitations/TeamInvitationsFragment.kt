package com.nuevo.gameness.ui.pages.personal.profile.settings.teaminvitations

import android.view.View
import androidx.fragment.app.viewModels
import com.nuevo.gameness.data.model.teamusers.TeamInviteDecisionRequest
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentTeamInvitationsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TeamInvitationsFragment : BaseFragment<FragmentTeamInvitationsBinding>() {

    override fun getViewBinding() = FragmentTeamInvitationsBinding.inflate(layoutInflater)

    private val viewModel by viewModels<TeamInvitationsViewModel>()
    private val teamInvitationsListAdapter = TeamInvitationsListAdapter()

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getTeamInviteList.fetchData()

        viewModel.getTeamInviteList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.teamInvitationItemList.let { items ->
                        if (items.isEmpty()){
                            binding.warningLayout.visibility = View.VISIBLE
                            binding.contentLayout.visibility = View.GONE
                        }else{
                            binding.warningLayout.visibility = View.GONE
                            binding.contentLayout.visibility = View.VISIBLE
                            teamInvitationsListAdapter.updateAllData(items)
                        }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {showLoading.value = state.isLoading}
                is NetworkResult.Error -> state.show()
            }
        }

        viewModel.acceptTeamInvitation.state.observe(viewLifecycleOwner){state ->
            when (state) {
                is NetworkResult.Success -> {
                    viewModel.getTeamInviteList.fetchData()
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {showLoading.value = state.isLoading}
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.rejectTeamInvitation.state.observe(viewLifecycleOwner){state ->
            when (state) {
                is NetworkResult.Success -> {
                    viewModel.getTeamInviteList.fetchData()
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {showLoading.value = state.isLoading}
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun events() {
       binding.apply {
           teamInvitationsRecyclerView.adapter = teamInvitationsListAdapter

           backImageView.setOnClickListener { popBackStack() }
           teamInvitationsListAdapter.onAcceptInvitationClickListener = { _, item ->

               viewModel.teamInviteDecisionRequest = TeamInviteDecisionRequest(item.id, true)
               viewModel.acceptTeamInvitation.fetchData()
           }
           teamInvitationsListAdapter.onRejectInvitationClickListener = { _, item ->
               viewModel.teamInviteDecisionRequest = TeamInviteDecisionRequest(item.id, false)
               viewModel.rejectTeamInvitation.fetchData()
           }


       }
    }

}