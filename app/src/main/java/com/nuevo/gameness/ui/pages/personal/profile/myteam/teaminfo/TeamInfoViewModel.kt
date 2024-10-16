package com.nuevo.gameness.ui.pages.personal.profile.myteam.teaminfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamInfoViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {


    //son turnuvalar
    val teamTournamentsList= StateLiveData(viewModelScope){
        repository.teamTournamentsList(
            maxResultCount = 10,
            sorting = "StartDate desc",
            skipCount = 0,
            tournamentStatus = null
        )
    }

    //son başarımlar
    val teamAwardList=StateLiveData(viewModelScope){
        repository.teamAwardList(
            maxResultCount = 10,
            sorting = "AcquisitionDate desc",
            skipCount = 0
        )
    }
}