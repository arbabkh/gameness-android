package com.nuevo.gameness.ui.pages.verifyphone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.phone_otp.SendSMSRequest
import com.nuevo.gameness.data.model.phone_otp.VerifySMSOtpRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyPhoneNumberViewModel @Inject constructor(application: Application, repository: Repository): AndroidViewModel(application) {


    var sendSmsRequest: SendSMSRequest? = null

    var verifyPhoneNumberRequest: VerifySMSOtpRequest? = null

    val verifyPhoneNumber = StateLiveData(viewModelScope) {
        repository.verifyOTPSMS(verifyPhoneNumberRequest)
    }

    val sendSms = StateLiveData(viewModelScope) {
        repository.sendOTPSMS(sendSmsRequest)
    }

}