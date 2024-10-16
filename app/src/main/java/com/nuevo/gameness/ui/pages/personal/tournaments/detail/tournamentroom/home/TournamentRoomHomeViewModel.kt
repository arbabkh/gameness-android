package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TournamentRoomHomeViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val pref: SharedPreferences
): AndroidViewModel(application) {

    var tournamentId: String? = null

    val getNextMatch = StateLiveData(viewModelScope) {
        repository.getNextMatch(tournamentId)
    }

    val getTournament = StateLiveData(viewModelScope) {
        repository.getTournament(tournamentId)
    }

    val checkNewRefereeMessage = StateLiveData(viewModelScope) {
        repository.checkNewRefereeMessage(tournamentId)
    }

    val checkNewReadyMessage = StateLiveData(viewModelScope) {
        repository.checkNewReadyMessage(tournamentId)
    }

    fun getToken(): String {
        return pref.getString("token", "") ?: ""
    }

}