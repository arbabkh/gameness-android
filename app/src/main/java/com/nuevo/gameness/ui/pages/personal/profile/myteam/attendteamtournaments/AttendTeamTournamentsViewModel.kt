package com.nuevo.gameness.ui.pages.personal.profile.myteam.attendteamtournaments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AttendTeamTournamentsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    val teamTournamentsList = StateLiveData(viewModelScope) {
        repository.teamTournamentsList(
            maxResultCount = 10,
            sorting = "StartDate desc",
            skipCount = 0
        )
    }

}