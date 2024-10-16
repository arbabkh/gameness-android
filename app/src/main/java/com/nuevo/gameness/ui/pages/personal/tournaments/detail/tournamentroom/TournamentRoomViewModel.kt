package com.nuevo.gameness.ui.pages.personal.tournaments.detail.tournamentroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TournamentRoomViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    var tournamentId: String? = null

    val getTournament = StateLiveData(viewModelScope) {
        repository.getTournament(tournamentId)
    }

}