package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.refereechat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.tournamentrefereemessages.RefereeMessageAsReadRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RefereeChatViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    var requestBody: RequestBody? = null
    var tournamentId: String? = null

    val sendRefereeMessage = StateLiveData(viewModelScope) {
        repository.sendRefereeMessage(requestBody)
    }

    val getTournamentRefereeUserChatList = StateLiveData(viewModelScope) {
        repository.getTournamentRefereeUserChatList(
            tournamentId,
            "id desc",
            0,
            10
        )
    }

    val setRefereeMessageAsRead = StateLiveData(viewModelScope) {
        repository.setRefereeMessageAsRead(RefereeMessageAsReadRequest(tournamentId))
    }

}