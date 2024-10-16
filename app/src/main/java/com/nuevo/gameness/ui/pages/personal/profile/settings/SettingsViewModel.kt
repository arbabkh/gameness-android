package com.nuevo.gameness.ui.pages.personal.profile.settings

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val prefs: SharedPreferences,
    repository: Repository
) : AndroidViewModel(application){

    fun clearUserInfo() = prefs.edit().clear().apply()

    val phoneVerificationRequired = prefs.getBoolean("phoneVerificationRequired", false)
    val emilVerificationRequired = prefs.getBoolean("emilVerificationRequired", false)


    val user = StateLiveData(viewModelScope) {
        repository.getUser()
    }

}