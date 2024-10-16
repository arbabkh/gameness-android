package com.nuevo.gameness.ui.pages.verifyemail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.email_otp.SendEmailRequest
import com.nuevo.gameness.data.model.email_otp.VerifyEmailOtpRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(application: Application,private val repository: Repository): AndroidViewModel(application) {

    var sendEmailRequest: SendEmailRequest? = null

    var verifyEmailAddressRequest: VerifyEmailOtpRequest? = null

    val verifyEmailAddress = StateLiveData(viewModelScope) {
        repository.verifyOTPEmail(verifyEmailAddressRequest)
    }

    val sendEmail = StateLiveData(viewModelScope) {
        repository.sendOTPEmail(sendEmailRequest)
    }

}