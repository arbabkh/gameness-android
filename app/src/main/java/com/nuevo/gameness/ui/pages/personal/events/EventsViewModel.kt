package com.nuevo.gameness.ui.pages.personal.events

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application){

    val eventList = StateLiveData(viewModelScope) {
        repository.eventList(
            sorting = "EventDate desc",
            skipCount = 0,
            maxResultCount = 10
        )
    }

}