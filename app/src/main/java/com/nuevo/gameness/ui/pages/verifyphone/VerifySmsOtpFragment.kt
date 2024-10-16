package com.nuevo.gameness.ui.pages.verifyphone

import android.util.Log
import com.nuevo.gameness.databinding.FragmentVerifySmsOtpBinding
import com.nuevo.gameness.ui.base.BaseFragment
import com.nuevo.gameness.utils.showDialog

class VerifySmsOtpFragment : BaseFragment<FragmentVerifySmsOtpBinding>() {

    var onUserEnteredOTP: ((otpToken: String) -> Unit)? = null
    var onUserWantChangePhoneNumber: (() -> Unit)? = null

    override fun getViewBinding() = FragmentVerifySmsOtpBinding.inflate(layoutInflater)

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

            buttonChangePhone.setOnClickListener {
                onUserWantChangePhoneNumber?.let {
                    Log.e("VerifySmsOtpFragment", "events: " )
                    it()
                }
            }


        }
    }

    override fun initView() {
        events()
    }

}