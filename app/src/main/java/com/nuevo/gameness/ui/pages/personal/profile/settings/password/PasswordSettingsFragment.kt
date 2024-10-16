package com.nuevo.gameness.ui.pages.personal.profile.settings.password

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.settings.request.ChangePasswordRequest
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentPasswordSettingsBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.Constants
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordSettingsFragment:BaseFragment<FragmentPasswordSettingsBinding>() {

    override fun getViewBinding(): FragmentPasswordSettingsBinding =
        FragmentPasswordSettingsBinding.inflate(layoutInflater)

    private val viewModel by viewModels<PasswordSettingsViewModel>()

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = true)
        setBackButton(false)

        events()
        fetchData()
    }

    private fun events() {
        binding.apply {
            btnSave.setOnClickListener {
                val oldPassword = edtOldPassword.text.toString()
                val newPassword = edtNewPassword.text.toString()
                val newPasswordRepeat = edtNewPasswordRepeat.text.toString()
                val message = when {
                    oldPassword.isEmpty() || newPassword.isEmpty() || newPasswordRepeat.isEmpty() -> getString(R.string.empty_warning)
                    viewModel.getPassword() != oldPassword -> getString(R.string.old_password_incorrect)
                    newPassword != newPasswordRepeat -> getString(R.string.password_again_validation)
                    !Constants.isValidPassword(newPassword) -> getString(R.string.password_validation)
                    else -> null
                }
                if (message != null) showDialog(message)
                else {
                    viewModel.request = ChangePasswordRequest(oldPassword, newPassword)
                    viewModel.changePassword.fetchData()
                }
            }
            icBack.setOnClickListener { popBackStack() }
        }
    }

    private fun fetchData(){
        viewModel.changePassword.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if(state.data.success) {
                        viewModel.setPassword(binding.edtNewPassword.text.toString())
                        showToast(getString(R.string.your_information_has_been_updated_successfully))
                        Handler(Looper.getMainLooper()).postDelayed({
                            popBackStack()
                        }, 10)
                    }else showDialog(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

}