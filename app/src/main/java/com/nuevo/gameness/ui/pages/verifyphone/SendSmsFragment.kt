package com.nuevo.gameness.ui.pages.verifyphone

import android.util.Log
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.nuevo.gameness.R
import com.nuevo.gameness.base.BaseFragment
import com.nuevo.gameness.data.model.phone_otp.SendSMSRequest
import com.nuevo.gameness.databinding.FragmentSendSmsBinding
import com.nuevo.gameness.hideKeyboard
import com.nuevo.gameness.utils.Constants
import com.nuevo.gameness.utils.showDialog
import java.util.*


class SendSmsFragment : BaseFragment<FragmentSendSmsBinding>() {



    var onNextPageListener: ((request: SendSMSRequest) -> Unit)? = null

    var phoneNumber: String = ""

    override fun getViewBinding() = FragmentSendSmsBinding.inflate(layoutInflater)


    fun events() {
        binding.apply {



            try {
                val phoneUtil = PhoneNumberUtil.getInstance()
                val numberProto = phoneUtil.parse(phoneNumber, "")

                val countryCode = "" + numberProto.countryCode
                val phoneNumber = phoneNumber.substring(countryCode.length + 1, phoneNumber.length)

                editTextPhoneNumber.setText(phoneNumber)
                ccp.setCountryForPhoneCode(numberProto.countryCode)

            } catch (e: java.lang.Exception) {

            }


            buttonSendSms.setOnClickListener {

                val phone = "+" + ccp.selectedCountryCode + editTextPhoneNumber.text.toString()

                if (!phone.matches(Constants.PHONE_NUMBER_REGEX)) {
                    showDialog(getString(R.string.phone_number_validation))
                    return@setOnClickListener
                }

                var lang = Locale.getDefault().language
                if (!(lang == "tr" || lang == "en")) {
                    lang = "en"
                }
                activity?.hideKeyboard()
                Log.e("SendSmsFragment", "events: line 48"  + (onNextPageListener == null))
                val req = SendSMSRequest(phone, lang)
                onNextPageListener?.let { it1 -> it1(req) }

            }

        }

    }

    fun fetchData() {

    }

    override fun initViews() {
        events()
        fetchData()

    }


}