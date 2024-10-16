package com.nuevo.gameness.ui.pages.personal.profile.myteam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTeamViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    val myTeam= StateLiveData(viewModelScope){
        repository.myTeam()
    }

    val leaveTheTeam = StateLiveData(viewModelScope){
        repository.leaveTheTeam()
    }
}