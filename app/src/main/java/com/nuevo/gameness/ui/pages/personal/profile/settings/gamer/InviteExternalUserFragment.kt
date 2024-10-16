package com.nuevo.gameness.ui.pages.personal.profile.settings.gamer

import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentInviteUserBinding
import com.nuevo.gameness.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteExternalUserFragment : BaseFragment<FragmentInviteUserBinding>() {

    private val viewModel by viewModels<InviteExternalUserViewModel>()


    override fun getViewBinding() = FragmentInviteUserBinding.inflate(layoutInflater)

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)


        events()
        fetchData()

    }

    private fun events() {
        binding.apply {
            icBack.setOnClickListener { popBackStack() }
        }
    }

    private fun fetchData() {
        viewModel.inviteExternalUser.fetchData()

        viewModel.inviteExternalUser.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    val response = state.data
                    if (response.success == true) showToast(getString(R.string.successful))
                    else showToast(response.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }

        }
    }
}