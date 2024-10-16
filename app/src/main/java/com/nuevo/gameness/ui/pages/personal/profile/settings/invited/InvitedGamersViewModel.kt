package com.nuevo.gameness.ui.pages.personal.profile.settings.invited

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.request.CancelTeamInvitationsRequest
import com.nuevo.gameness.data.model.teamusers.SendTeamInviteRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvitedGamersViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var sendTeamInviteRequest: SendTeamInviteRequest? = null
    var cancelTeamInvitationsRequest: CancelTeamInvitationsRequest? = null

    val cancelTeamInvitations = StateLiveData(viewModelScope){
        repository.cancelTeamInvitations(cancelTeamInvitationsRequest)
    }

    val getUserToTeamInvitedList = StateLiveData(viewModelScope) {
        repository.getUserToTeamInvitedList()
    }

    val getUserListByUsername = StateLiveData(viewModelScope) {
        repository.getUserListByUsername("me", 0, 10, true)
    }

    val sendTeamInvite = StateLiveData(viewModelScope) {
        repository.sendTeamInvite(sendTeamInviteRequest)
    }
}