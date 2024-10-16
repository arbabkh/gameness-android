package com.nuevo.gameness.ui.pages.login

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.users.DeviceLanguageRequest
import com.nuevo.gameness.data.model.users.LoginRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val prefs: SharedPreferences
): AndroidViewModel(application) {

    var request: LoginRequest? = null
    var deviceLanguageRequest: DeviceLanguageRequest ?= null


    val login = StateLiveData(viewModelScope) {
        repository.login(request)
    }

    val setDeviceLanguage = StateLiveData(viewModelScope) {
        repository.setDeviceLanguage(deviceLanguageRequest!!)
    }

    fun getDeviceToken(): String? = prefs.getString("deviceToken",null)

    fun setUserInfo(username: String, password: String, rememberMe: Boolean) {
        val editor = prefs.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.putBoolean("rememberMe", rememberMe)
        editor.apply()
    }

    fun saveTokens(token: String, refreshToken: String, tokenExpiresAt: String, refreshTokenExpiresAt: String) {
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.putString("refreshToken", refreshToken)
        editor.putString("tokenExpiresAt", tokenExpiresAt)
        editor.putString("refreshTokenExpiresAt", refreshTokenExpiresAt)
        editor.apply()
    }

    fun saveUserId(id: String) {
        prefs.edit().putString("user_id", id).apply()
    }

}