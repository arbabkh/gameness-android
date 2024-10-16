package com.nuevo.gameness.ui.pages.splash

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.publicconfigurations.PublicConfiguration
import com.nuevo.gameness.data.model.refreshtoken.RefreshTokenRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val prefs: SharedPreferences
): AndroidViewModel(application) {

    var request: RefreshTokenRequest? = null

    fun setDeviceToken(deviceToken: String?) = prefs.edit().putString("deviceToken", deviceToken).apply()

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
    fun savePublicConf(conf: PublicConfiguration) {

        prefs.edit()
            .putBoolean("signUpEnabled", conf.signUpEnabled)
            .putBoolean("signUpPhoneVerificationRequired", conf.signUpPhoneVerificationRequired)
            .putBoolean("signUpEMailVerificationRequired", conf.signUpEMailVerificationRequired)
            .putLong("autoRefreshInterval", conf.autoRefreshInterval)
            .putBoolean("phoneVerificationRequired", conf.phoneVerificationRequired)
            .putBoolean("emilVerificationRequired", conf.emilVerificationRequired)
            .apply()



    }
    fun getLanguage(): String? = prefs.getString("language", null)

    fun getUserInfo(): HashMap<String, Any?> {
        return hashMapOf(
            "token" to prefs.getString("token", null),
            "refreshToken" to prefs.getString("refreshToken", null),
            "rememberMe" to prefs.getBoolean("rememberMe", false),
            "tokenExpiresAt" to prefs.getString("tokenExpiresAt", ""),
            "refreshTokenExpiresAt" to prefs.getString("refreshTokenExpiresAt", "")
        )
    }

    fun getUserId(): String {
        return prefs.getString("user_id", "") ?: ""
    }



    val refreshToken = StateLiveData(viewModelScope) {
        repository.getRefreshToken(request)
    }

    val publicConfiguration = StateLiveData(viewModelScope) {
        repository.getPublicConfigurations()
    }

}