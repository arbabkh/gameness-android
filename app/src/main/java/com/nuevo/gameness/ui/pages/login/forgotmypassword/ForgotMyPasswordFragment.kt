package com.nuevo.gameness.ui.pages.login.forgotmypassword

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Patterns
import android.view.View
import androidx.fragment.app.viewModels
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentForgotMyPasswordBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotMyPasswordFragment : BaseFragment<FragmentForgotMyPasswordBinding>() {

    override fun getViewBinding() = FragmentForgotMyPasswordBinding.inflate(layoutInflater)

    private val viewModel by viewModels<ForgotMyPasswordViewModel>()

    override fun initView() {
        setBottomVisibility(bottomLine = true, bottomMenu = false)
        setBackButton(false)

        fetchData()
        events()
    }

    private fun fetchData() {
        viewModel.sendPasswordCode.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) binding.apply {
                        editTextMailAddress.visibility = View.VISIBLE
                        editTextMailAddress.animate()
                            .translationX(editTextMailAddress.measuredWidth.toFloat() * -2)
                            .setDuration(1000)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    editTextMailAddress.clearAnimation()
                                    editTextMailAddress.visibility = View.GONE
                                }
                            })
                        editTextPassCode.visibility = View.VISIBLE
                        editTextPassCode.translationX = editTextMailAddress.measuredWidth.toFloat() * 2
                        editTextPassCode.animate()
                            .translationX(0F)
                            .setDuration(1000)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    editTextPassCode.clearAnimation()
                                }
                            })
                    } else showDialog(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.verifyPasswordCode.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true && state.data.data?.token != null) {
                        binding.apply {
                            viewModel.resetToken = state.data.data.resetToken
                            UserModel.instance.token = state.data.data.token
                            viewModel.saveToken(state.data.data.token)
                            val width = layoutVerifyMailAddress.measuredWidth.toFloat()
                            layoutVerifyMailAddress.animate()
                                .translationX(width * -2)
                                .setDuration(1000)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        layoutVerifyMailAddress.clearAnimation()
                                        layoutVerifyMailAddress.visibility = View.GONE
                                    }
                                })
                            layoutResetPassword.visibility = View.VISIBLE
                            layoutResetPassword.translationX = width * 2
                            layoutResetPassword.animate()
                                .translationX(0F)
                                .setDuration(1000)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        layoutResetPassword.clearAnimation()
                                    }
                                })
                        }
                    } else showDialog(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
        viewModel.resetPassword.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    if (state.data.success == true) binding.apply {
                        showToast(getString(R.string.successful))
                        popBackStack()
                    } else showDialog(state.data.message.toString())
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> state.show()
            }
        }
    }

    private fun events() {
        binding.apply {
            layoutVerifyMailAddress.visibility = View.VISIBLE
            layoutResetPassword.visibility = View.GONE
            editTextMailAddress.visibility = View.VISIBLE
            editTextMailAddress.translationX = 0F
            editTextPassCode.visibility = View.GONE

            imageViewBack.setOnClickListener {
                popBackStack()
            }

            buttonNext.setOnClickListener {
                when(View.VISIBLE) {
                    editTextMailAddress.visibility -> {
                        if (Patterns.EMAIL_ADDRESS.matcher(editTextMailAddress.text.toString()).matches()) {
                            viewModel.email = editTextMailAddress.text.toString()
                            viewModel.sendPasswordCode.fetchData()
                        } else showDialog(getString(R.string.mail_address_validation))
                    }
                    editTextPassCode.visibility -> {
                        viewModel.passCode = editTextPassCode.text.toString()
                        viewModel.verifyPasswordCode.fetchData()
                    }
                }
            }

            buttonSave.setOnClickListener {
                val newPassword = editTextPassword.text.toString()
                val newPasswordRepeat = editTextPasswordAgain.text.toString()
                val message = when {
                    newPassword.isEmpty() || newPasswordRepeat.isEmpty() -> getString(R.string.empty_warning)
                    newPassword != newPasswordRepeat -> getString(R.string.password_again_validation)
                    !newPassword.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{6,}\$".toRegex()) -> getString(R.string.password_validation)
                    else -> null
                }
                if (message != null) showDialog(message)
                else {
                    viewModel.newPassword = newPassword
                    viewModel.resetPassword.fetchData()
                }
            }
        }
    }

    override fun onDestroyView() {
        UserModel.instance.token = ""
        super.onDestroyView()
    }
}