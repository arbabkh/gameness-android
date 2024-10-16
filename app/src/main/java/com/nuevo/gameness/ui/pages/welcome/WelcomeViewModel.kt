package com.nuevo.gameness.ui.pages.welcome

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val prefs: SharedPreferences
): AndroidViewModel(application) {

    val getSliderList = StateLiveData(viewModelScope) {
        repository.getSliderList(
            sorting = "id desc",
            skipCount = 0,
            maxResultCount = 10
        )
    }

    val isSignupEnabled = prefs.getBoolean("signUpEnabled", true)
    val signUpPhoneVerificationRequired = prefs.getBoolean("signUpPhoneVerificationRequired", false)
    val signUpEMailVerificationRequired = prefs.getBoolean("signUpEMailVerificationRequired", false)
    val autoRefreshInterval = prefs.getLong("autoRefreshInterval", 5000)
    val phoneVerificationRequired = prefs.getBoolean("phoneVerificationRequired", false)
    val emilVerificationRequired = prefs.getBoolean("emilVerificationRequired", false)

    fun setLanguage(language: String?) = prefs.edit().putString("language", language).apply()

}