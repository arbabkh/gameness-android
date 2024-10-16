package com.nuevo.gameness.ui.pages.personal.profile.settings.invited

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.request.CancelTeamInvitationsRequest
import com.nuevo.gameness.data.model.teamusers.SendTeamInviteRequest
import com.nuevo.gameness.data.model.users.UserListByUserNameItem
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentInvitedGamersBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.ui.pages.personal.profile.settings.invited.adapter.UserListByUsernameAdapter
import com.nuevo.gameness.ui.pages.personal.profile.settings.invited.adapter.UserToTeamInvitedListAdapter
import com.nuevo.gameness.utils.myContains
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InvitedGamersFragment : BaseFragment<FragmentInvitedGamersBinding>() {

    override fun getViewBinding() = FragmentInvitedGamersBinding.inflate(layoutInflater)

    private val viewModel by viewModels<InvitedGamersViewModel>()
    private val userToTeamInvitedListAdapter = UserToTeamInvitedListAdapter()
    private val userListByUsernameAdapter = UserListByUsernameAdapter()
    private var userListByUsername: List<UserListByUserNameItem> = listOf()

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.getUserToTeamInvitedList.fetchData()
        viewModel.getUserListByUsername.fetchData()

        viewModel.getUserToTeamInvitedList.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.data?.let { items ->
                        userToTeamInvitedListAdapter.updateAllData(items)
                        binding.textViewWarning.visibility =
                            if (items.isEmpty()) View.VISIBLE
                            else View.GONE
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {
                    showLoading.value = state.isLoading
                }
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.getUserListByUsername.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    state.data.items?.let { items ->
                        userListByUsername = items
                        userListByUsernameAdapter.updateAllData(userListByUsername)
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {
                    showLoading.value = state.isLoading
                }
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.sendTeamInvite.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    viewModel.getUserToTeamInvitedList.fetchData()
                    showToast(
                        if (state.data.success == true) getString(R.string.successful)
                        else state.data.message.toString()
                    )
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {
                    showLoading.value = state.isLoading
                }
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.cancelTeamInvitations.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    viewModel.getUserToTeamInvitedList.fetchData()
                    showToast(
                        if (state.data.success == true) getString(R.string.successful)
                        else state.data.message.toString()
                    )
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> {
                    showLoading.value = state.isLoading
                }
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun events() {
        binding.apply {
            recyclerViewUserToTeamInvitedList.adapter = userToTeamInvitedListAdapter
            recyclerViewUserListByUserName.adapter = userListByUsernameAdapter

            layoutInvitePlayer.translationY = layoutInvitePlayer.measuredHeight.toFloat()

            imageViewBack.setOnClickListener { popBackStack() }

            imageViewAdd.setOnClickListener {
                layoutInvitePlayer.visibility = View.VISIBLE
                layoutInvitePlayer.animate()
                    .translationY(0F)
                    .setDuration(500)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {}
                    })
            }

            textViewCancel.setOnClickListener {
                layoutInvitePlayer.animate()
                    .translationY(layoutInvitePlayer.measuredHeight.toFloat())
                    .setDuration(500)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            layoutInvitePlayer.visibility = View.GONE
                        }
                    })
            }

            editTextSearch.addTextChangedListener { input ->
                val text = (input ?: "").toString()
                if (text.isEmpty()) userListByUsernameAdapter.updateAllData(userListByUsername)
                else userListByUsernameAdapter.updateAllData(
                    userListByUsername.filter { userListByUserNameItem ->
                        userListByUserNameItem.username.myContains(text)
                    }
                )
            }
        }
        userToTeamInvitedListAdapter.onPullBackClickListener = { _, item ->

            if (item.id != null) {
                viewModel.cancelTeamInvitationsRequest =
                    CancelTeamInvitationsRequest(listOf(item.id))
                viewModel.cancelTeamInvitations.fetchData()

            }
        }
        userListByUsernameAdapter.onInviteClickListener = { _, item ->
            viewModel.sendTeamInviteRequest = SendTeamInviteRequest(item.id, "")
            viewModel.sendTeamInvite.fetchData()
        }

    }

}