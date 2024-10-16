package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom.home.scoreboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TournamentRoomScoreboardViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    var tournamentId: String? = null

    val getScoreboard = StateLiveData(viewModelScope) {
        repository.getScoreboard(tournamentId)
    }

    val getTournament = StateLiveData(viewModelScope) {
        repository.getTournament(tournamentId)
    }

}