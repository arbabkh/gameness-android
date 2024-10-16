package com.nuevo.gameness.ui.pages.personal.profile.settings.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.request.SetUserGamesRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameSettingsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var request:SetUserGamesRequest?=null

    val getGameList = StateLiveData(viewModelScope) {
        repository.getGameList(
            takeCount = 10,
            sorting = "id desc",
            skipCount = 0
        )
    }

    val getMyGameList = StateLiveData(viewModelScope) {
        repository.getMyGameList(
            takeCount = 10,
            sorting = "id desc",
            skipCount = 0
        )
    }

    val setUserGames= StateLiveData(viewModelScope){
        repository.setUserGames(request!!)
    }
}