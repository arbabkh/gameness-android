package com.nuevo.gameness.ui.pages.personal.profile.settings.gamer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.request.ChangeTeamGameUserRequestElement
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InviteUserViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var request: List<ChangeTeamGameUserRequestElement>?=null

    val teamUsers= StateLiveData(viewModelScope){
        repository.teamUsers()
    }

    val teamUserList=StateLiveData(viewModelScope){
        repository.teamUserList()
    }

    val setUserTeam=StateLiveData(viewModelScope) {
        repository.changeTeamGameUser(request!!)

    }

}