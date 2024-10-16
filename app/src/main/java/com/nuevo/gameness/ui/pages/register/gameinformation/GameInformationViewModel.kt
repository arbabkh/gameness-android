package com.nuevo.gameness.ui.pages.register.gameinformation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.users.UserGamesForRegisterRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameInformationViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    var userGamesForRegisterRequest: UserGamesForRegisterRequest? = null

    val setUserGamesForRegister = StateLiveData(viewModelScope) {
        repository.setUserGamesForRegister(userGamesForRegisterRequest)
    }


}