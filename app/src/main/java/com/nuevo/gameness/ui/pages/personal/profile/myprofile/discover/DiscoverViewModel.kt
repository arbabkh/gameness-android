package com.nuevo.gameness.ui.pages.personal.profile.myprofile.discover

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel@Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application){

    //son başarımlar
    val getUserAwardList= StateLiveData(viewModelScope){
        repository.userAwardList(
            maxResultCount = 10,
            sorting = "AcquisitionDate desc",
            skipCount = 0
        )
    }

    //son turnuvalar
    val userTournamentsList= StateLiveData(viewModelScope){
        repository.userTournamentsList(
            maxResultCount = 10,
            sorting = "StartDate desc",
            skipCount = 0,
            tournamentStatus = null
        )
    }

    //seçtiğim oyunlar
    val getMyGameList = StateLiveData(viewModelScope) {
        repository.getMyGameList(
            takeCount = 10,
            sorting = "id desc",
            skipCount = 0
        )
    }

}