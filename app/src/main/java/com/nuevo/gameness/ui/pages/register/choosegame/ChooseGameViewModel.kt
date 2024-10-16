package com.nuevo.gameness.ui.pages.register.choosegame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseGameViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    val getGameList = StateLiveData(viewModelScope) {
        repository.getGameList(
            takeCount = 10,
            sorting = "id desc",
            skipCount = 0
        )
    }

}