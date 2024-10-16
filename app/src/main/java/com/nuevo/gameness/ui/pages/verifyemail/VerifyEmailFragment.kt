package com.nuevo.gameness.ui.pages.verifyemail

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.email_otp.VerifyEmailOtpRequest
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentVerifyEmailBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.setTabColor
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyEmailFragment : BaseFragment<FragmentVerifyEmailBinding>() {

    private val viewModel by viewModels<VerifyEmailViewModel>()
    private lateinit var phoneAdapter: EmailVerificationAdapter
    private var reference: String? = null
    private var emailAddress: String? = null

    fun events() {
        binding.apply {


            var emailAddress = arguments?.getString("emailAddress") ?: ""

            phoneAdapter = EmailVerificationAdapter(this@VerifyEmailFragment)

            phoneAdapter.sendEmailFragment.emailAddress = emailAddress

            viewPager2Register.adapter = phoneAdapter
            viewPager2Register.isUserInputEnabled = false

            phoneAdapter.sendEmailFragment.onNextPageListener = {request ->
                emailAddress = request.emailAddress
                viewPager2Register.currentItem++
                viewModel.sendEmailRequest = request
                viewModel.sendEmail.fetchData()
            }

            phoneAdapter.verifyEmailOtpFragment.onUserEnteredOTP = { otpToken ->
                viewModel.verifyEmailAddressRequest = VerifyEmailOtpRequest(emailAddress ?: "", reference ?: "", otpToken)
                viewModel.verifyEmailAddress.fetchData()
            }

            phoneAdapter.verifyEmailOtpFragment.onUserWantChangeEmailAddress = {
                viewPager2Register.currentItem--
            }

            viewPager2Register.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when(position) {
                        0 -> {
                            context.setTabColor(textViewPersonalInformation, linePersonalInformation, R.color.white)
                            context.setTabColor(textViewChooseGame, lineChooseGame, R.color.tertiary_dark)
                        }
                        1 -> {
                            context.setTabColor(textViewPersonalInformation, linePersonalInformation, R.color.green)
                            context.setTabColor(textViewChooseGame, lineChooseGame, R.color.blue)
                        }

                    }
                }
            })

            imageViewBack.setOnClickListener {
                popBackStack()
            }

        }



    }

    fun fetchData() {


        viewModel.sendEmail.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is NetworkResult.Success -> {
                    val response = state.data
                    when {
                        response.success == true && response.data != null -> {

                            reference = response.data
                            binding.viewPager2Register.currentItem++

                            // navigate to otp fragment
                        } else -> {
                        showDialog(state.data.message ?: "")
                    }
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> {
                    state.show()
                    showDialog(state.message)
                }
            }
        }

        viewModel.verifyEmailAddress.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is NetworkResult.Success -> {
                    val success = state.data.success ?: false
                    val message = state.data.message ?: ""
                    val isValid = state.data.data ?: false

                    if (success && isValid) {
                        Toast.makeText(context, "email address validated", Toast.LENGTH_LONG).show()
                        popBackStack()
                    } else {
                        showDialog(state.data.message ?: "")
                    }
                }
                is NetworkResult.SuccessWithNoContent -> {}
                is NetworkResult.Loading -> showLoading.value = state.isLoading
                is NetworkResult.Error -> {
                    state.show()
                    showDialog(state.message)
                }
            }
        }

    }

    override fun getViewBinding() = FragmentVerifyEmailBinding.inflate(layoutInflater)

    override fun initView() {
        events()
        fetchData()
    }


}