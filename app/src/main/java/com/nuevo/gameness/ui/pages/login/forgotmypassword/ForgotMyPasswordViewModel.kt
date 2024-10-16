package com.nuevo.gameness.ui.pages.login.forgotmypassword

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.users.EMailRequest
import com.nuevo.gameness.data.model.users.ResetPasswordRequest
import com.nuevo.gameness.data.model.users.VerifyPasswordCodeRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotMyPasswordViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val pref: SharedPreferences
): AndroidViewModel(application) {

    var email: String? = null
    var passCode: String? = null
    var resetToken: String? = null
    var newPassword: String? = null

    val sendPasswordCode = StateLiveData(viewModelScope) {
        repository.sendPasswordCode(EMailRequest(email))
    }

    fun saveToken(token: String) {
        pref.edit().putString("token", token).apply()
    }

    val verifyPasswordCode = StateLiveData(viewModelScope) {
        repository.verifyPasswordCode(VerifyPasswordCodeRequest(email, passCode))
    }

    val resetPassword = StateLiveData(viewModelScope) {
        repository.resetPassword(ResetPasswordRequest(resetToken, newPassword))
    }

}