package com.nuevo.gameness.ui.pages.personal.profile.myprofile.attendtournaments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AttendTournamentsViewModel@Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    val userTournamentsList = StateLiveData(viewModelScope) {
        repository.userTournamentsList(
            maxResultCount = 10,
            sorting = "StartDate desc",
            skipCount = 0
        )
    }

}