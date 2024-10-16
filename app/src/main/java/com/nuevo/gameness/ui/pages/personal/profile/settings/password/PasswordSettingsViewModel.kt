package com.nuevo.gameness.ui.pages.personal.profile.settings.password

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.request.ChangePasswordRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordSettingsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val prefs: SharedPreferences
) : AndroidViewModel(application) {

    var request: ChangePasswordRequest? = null

    val changePassword = StateLiveData(viewModelScope){
        repository.changePassword(request!!)
    }

    fun setPassword(password: String?) {
        prefs.edit().putString("password", password).apply()
    }

    fun getPassword(): String? = prefs.getString("password", "")

}