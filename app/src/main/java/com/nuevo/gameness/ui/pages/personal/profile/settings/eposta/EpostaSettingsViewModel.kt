package com.nuevo.gameness.ui.pages.personal.profile.settings.eposta

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.request.ChangeEmailRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpostaSettingsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var request: ChangeEmailRequest? = null

    val getUser = StateLiveData(viewModelScope) {
        repository.getUser()
    }

    val changeEmail = StateLiveData(viewModelScope) {
        repository.changeEmail(request!!)
    }

}