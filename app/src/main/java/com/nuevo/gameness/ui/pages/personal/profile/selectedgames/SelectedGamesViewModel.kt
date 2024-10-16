package com.nuevo.gameness.ui.pages.personal.profile.selectedgames

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectedGamesViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    val getMyGameList = StateLiveData(viewModelScope) {
        repository.getMyGameList(
            takeCount = 10,
            sorting = "id desc",
            skipCount = 0
        )
    }

}