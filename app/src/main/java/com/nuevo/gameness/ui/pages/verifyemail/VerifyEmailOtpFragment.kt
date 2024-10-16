package com.nuevo.gameness.ui.pages.verifyemail

import com.nuevo.gameness.databinding.FragmentVerifyEmailOtpBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyEmailOtpFragment : BaseFragment<FragmentVerifyEmailOtpBinding>() {

    var onUserEnteredOTP: ((otpToken: String) -> Unit)? = null
    var onUserWantChangeEmailAddress: (() -> Unit)? = null

    override fun getViewBinding() = FragmentVerifyEmailOtpBinding.inflate(layoutInflater)


    fun fetchData() {

    }

    fun events() {
        binding.apply {

            buttonPhoneVerify.setOnClickListener {

                var otp = editTextOtp.text.toString()
                otp = otp.trim()
                if (otp.isEmpty()) {
                    showDialog("otp token can not be empty")
                    return@setOnClickListener
                }

                onUserEnteredOTP?.let {
                    it(otp)
                }



            }

            buttonChangeEmail.setOnClickListener {
                onUserWantChangeEmailAddress?.let {
                    it()
                }
            }


        }
    }

    override fun initView() {
        events()
    }

}