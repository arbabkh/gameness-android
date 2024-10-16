package com.nuevo.gameness.ui.pages.verifyphone


import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.phone_otp.VerifySMSOtpRequest
import com.nuevo.gameness.data.result.NetworkResult
import com.nuevo.gameness.databinding.FragmentVerifyPhoneNumberBinding
import com.nuevo.gameness.utils.setTabColor
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyPhoneNumberFragment : com.nuevo.gameness.ui.base.BaseFragment<FragmentVerifyPhoneNumberBinding>()
{

    private val viewModel by viewModels<VerifyPhoneNumberViewModel>()
    private lateinit var phoneAdapter: PhoneVerificationAdapter
    private var reference: String? = null
    private var phoneNumber: String? = null

    fun events() {
        binding.apply {

            var phoneNumber = arguments?.getString("phoneNumber") ?: ""


            phoneAdapter = PhoneVerificationAdapter(this@VerifyPhoneNumberFragment)
            phoneAdapter.sendSmsFragment.phoneNumber = phoneNumber
            viewPager2Register.adapter = phoneAdapter
            viewPager2Register.isUserInputEnabled = false

            phoneAdapter.sendSmsFragment.onNextPageListener = {request ->
                phoneNumber = request.phoneNumber
                viewModel.sendSmsRequest = request
                viewModel.sendSms.fetchData()
            }

            phoneAdapter.verifyOtpFragment.onUserEnteredOTP = { otpToken ->
                viewModel.verifyPhoneNumberRequest = VerifySMSOtpRequest(phoneNumber ?: "", reference ?: "", otpToken)
                viewModel.verifyPhoneNumber.fetchData()
            }

            phoneAdapter.verifyOtpFragment.onUserWantChangePhoneNumber = {
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


        viewModel.sendSms.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is NetworkResult.Success -> {
                    val response = state.data
                    when {
                        response.success == true && response.data != null -> {

                            reference = response.data
                            binding.viewPager2Register.currentItem++

                        } else -> {
                        showDialog(state.data.message ?: getString(R.string.error_warning))
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

        viewModel.verifyPhoneNumber.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is NetworkResult.Success -> {
                    val success = state.data.success ?: false
                    val message = state.data.message ?: ""
                    val isValid = state.data.data ?: false
                    if (success && isValid) {
                        Toast.makeText(context, "phone number validated", Toast.LENGTH_LONG).show()
                        popBackStack()
                    } else {
                        showDialog(state.data.message ?: getString(R.string.error_warning))
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

    override fun getViewBinding() = FragmentVerifyPhoneNumberBinding.inflate(layoutInflater)

    override fun initView() {
        events()
        fetchData()
    }

}