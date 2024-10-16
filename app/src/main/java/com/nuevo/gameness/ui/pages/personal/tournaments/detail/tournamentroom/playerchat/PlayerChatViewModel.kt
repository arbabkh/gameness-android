package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.playerchat

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.readymessages.ReadyMessageAsReadRequest
import com.nuevo.gameness.data.model.readymessages.SendReadyMessageRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerChatViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val pref: SharedPreferences
): AndroidViewModel(application) {

    var tournamentID: String? = null
    var recipientID: String? = null
    var readyMessageID: String? = null

    val getReadyMessageInbox = StateLiveData(viewModelScope) {
        repository.getReadyMessageInbox(tournamentID)
    }

    val getReadyMessageList = StateLiveData(viewModelScope) {
        repository.getReadyMessage(
            maxResultCount = 20,
            sorting = null,
            skipCount = null
        )
    }

    val getReadyMessageHistoryList = StateLiveData(viewModelScope) {
        repository.getReadyMessageHistory(tournamentID, recipientID)
    }

    val sendReadyMessage = StateLiveData(viewModelScope) {
        repository.sendReadyMessage(
            SendReadyMessageRequest(tournamentID, readyMessageID, recipientID)
        )
    }

    val setReadyMessageAsRead = StateLiveData(viewModelScope) {
        repository.setReadyMessageAsRead(ReadyMessageAsReadRequest(tournamentID, recipientID))
    }

    fun getToken(): String {
        return pref.getString("token", "") ?: ""
    }


}