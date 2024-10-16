package com.nuevo.gameness.ui.pages.verifyemail

import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.email_otp.SendEmailRequest
import com.nuevo.gameness.databinding.FragmentSendEmailBinding
import com.nuevo.gameness.hideKeyboard
import com.nuevo.gameness.utils.Constants
import com.nuevo.gameness.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SendEmailFragment : com.nuevo.gameness.base.BaseFragment<FragmentSendEmailBinding>() {

    var onNextPageListener: ((request: SendEmailRequest) -> Unit)? = null

    override fun getViewBinding() = FragmentSendEmailBinding.inflate(layoutInflater)


    var emailAddress: String = ""



    fun events() {
        binding.apply {

            editTextEmail.setText(emailAddress)

            buttonSendSms.setOnClickListener {

                val email = editTextEmail.text.toString()

                if (!email.matches(Constants.EMAIL_ADDRESS_REGEX)) {
                    showDialog(getString(R.string.mail_address_validation))
                    return@setOnClickListener
                }

                var lang = Locale.getDefault().language
                if (!(lang == "tr" || lang == "en")) {
                    lang = "en"
                }

                activity?.hideKeyboard()
                val req = SendEmailRequest(email, lang)
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