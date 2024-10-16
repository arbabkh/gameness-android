package com.nuevo.gameness.ui.pages.personal.profile.settings.username

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.request.ChangeUsernameRequest
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentChangeUsernameBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeUserNameFragment:BaseFragment<FragmentChangeUsernameBinding>() {

    override fun getViewBinding(): FragmentChangeUsernameBinding =
        FragmentChangeUsernameBinding.inflate(layoutInflater)

    private val viewModel by viewModels<ChangeUserNameViewModel>()

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun events(){
        binding.apply {
            btnSave.setOnClickListener {
                val oldUsername = edtOldUsername.text.toString()
                val newUsername = edtNewUsername.text.toString()
                if (newUsername.length < 3) showDialog(getString(R.string.username_validation))
                else {
                    viewModel.request = ChangeUsernameRequest(oldUsername, newUsername)
                    viewModel.changeUsername.fetchData()
                }
            }
            icBack.setOnClickListener { popBackStack() }
        }
    }

    private fun fetchData(){
        viewModel.myInfo.fetchData()

        viewModel.myInfo.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    binding.edtOldUsername.setText(state.data.data?.userName)
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.changeUsername.state.observe(viewLifecycleOwner){state->
            when (state) {
                is NetworkResult.Success -> {
                    if(state.data.success) {
                        viewModel.setUsername(binding.edtNewUsername.text.toString())
                        showToast(getString(R.string.your_information_has_been_updated_successfully))
                        Handler(Looper.getMainLooper()).postDelayed({
                            popBackStack()
                        }, 10)
                    } else showToast(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }

    }
}