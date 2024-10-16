package com.nuevo.gameness.ui.pages.personal.tournaments.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.tournaments.TournamentIdRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TournamentDetailViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    var id: String? = null

    val joinTournament = StateLiveData(viewModelScope) {
        repository.joinTournament(TournamentIdRequest(id))
    }

    val getTournament = StateLiveData(viewModelScope) {
        repository.getTournament(id)
    }

    val cancelTournament = StateLiveData(viewModelScope) {
        repository.cancelTournament(TournamentIdRequest(id))
    }

}