package com.nuevo.gameness.ui.pages.personal.profile.settings.username

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.request.ChangeUsernameRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeUserNameViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val prefs: SharedPreferences
) : AndroidViewModel(application){

    var request: ChangeUsernameRequest? = null

    val myInfo = StateLiveData(viewModelScope) {
        repository.getUser()
    }

    val changeUsername = StateLiveData(viewModelScope) {
        repository.changeUsername(request!!)
    }

    fun setUsername(username: String?) {
        prefs.edit().putString("username", username)
    }

}