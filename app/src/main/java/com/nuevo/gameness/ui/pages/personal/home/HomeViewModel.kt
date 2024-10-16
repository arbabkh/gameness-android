package com.nuevo.gameness.ui.pages.personal.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.tournaments.TournamentIdRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    var tournamentId: String? = null

    val getAnnouncements = StateLiveData(viewModelScope) {
        repository.getAnnouncements(
            sorting = "id desc",
            skipCount = 0,
            maxResultCount = 10,
            isUserGameAnnouncementList = false
        )
    }

    val getTournaments = StateLiveData(viewModelScope) {
        repository.getTournaments(
            sorting = "id desc",
            skipCount = 0,
            maxResultCount = 10,
            tournamentTypeFilter = 0,
            tournamentStatus = 1
        )
    }

    val joinTournament = StateLiveData(viewModelScope) {
        repository.joinTournament(TournamentIdRequest(tournamentId))
    }

    val cancelTournament = StateLiveData(viewModelScope) {
        repository.cancelTournament(TournamentIdRequest(tournamentId))
    }

}