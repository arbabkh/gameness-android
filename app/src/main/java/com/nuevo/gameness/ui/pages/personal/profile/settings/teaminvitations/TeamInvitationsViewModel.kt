package com.nuevo.gameness.ui.pages.personal.profile.settings.teaminvitations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.teamusers.TeamInviteDecisionRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamInvitationsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var teamInviteDecisionRequest:TeamInviteDecisionRequest? = null

    val getTeamInviteList = StateLiveData(viewModelScope) {
        repository.getTeamInviteList()
    }

    val acceptTeamInvitation = StateLiveData(viewModelScope) {
            repository.sendTeamInvitationDecision(teamInviteDecisionRequest)
    }
    val rejectTeamInvitation = StateLiveData(viewModelScope) {
        repository.sendTeamInvitationDecision(teamInviteDecisionRequest)
    }

}