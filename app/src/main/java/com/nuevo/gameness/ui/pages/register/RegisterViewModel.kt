package com.nuevo.gameness.ui.pages.register

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.users.RegisterRequest
import com.nuevo.gameness.data.model.users.UserGamesForRegisterRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val preferences: SharedPreferences
): AndroidViewModel(application) {

    var registerRequest: RegisterRequest? = null
    var userGamesForRegisterRequest: UserGamesForRegisterRequest? = null

    val register = StateLiveData(viewModelScope) {
        repository.register(registerRequest)
    }

    val setUserGamesForRegister = StateLiveData(viewModelScope) {
        repository.setUserGamesForRegister(userGamesForRegisterRequest)
    }

    fun saveTokens(token: String, refreshToken: String, tokenExpiresAt: String, refreshTokenExpiresAt: String) {
        val editor = preferences.edit()
        editor.putString("token", token)
        editor.putString("refreshToken", refreshToken)
        editor.putString("tokenExpiresAt", tokenExpiresAt)
        editor.putString("refreshTokenExpiresAt", refreshTokenExpiresAt)
        editor.apply()
    }

    fun setUserInfo(username: String, password: String, rememberMe: Boolean) {
        val editor = preferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.putBoolean("rememberMe", rememberMe)
        editor.apply()
    }

    fun saveUserId(id: String) {
        preferences.edit().putString("user_id", id).apply()
    }


    val countryList = StateLiveData(viewModelScope) {
        repository.getCountryList()
    }

}