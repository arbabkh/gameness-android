package com.nuevo.gameness.ui.pages.personal.profile.settings.teamsettings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class TeamSettingsViewModel@Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var request:RequestBody?=null

    val myTeam=StateLiveData(viewModelScope){
        repository.myTeam()
    }

    val myTeamGameList=StateLiveData(viewModelScope){
        repository.getMyTeamGameList(
            takeCount = 10,
            sorting = "id desc",
            skipCount = 0)
    }

    val gameList = StateLiveData(viewModelScope) {
        repository.getGameList(
            takeCount = 10,
            sorting = "id desc",
            skipCount = 0
        )
    }


    val changeTeamInformation=StateLiveData(viewModelScope){
        repository.changeMyTeamInformation(request!!)
    }
}