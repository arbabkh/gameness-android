package com.nuevo.gameness.ui.pages.personal.events.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application){

    var eventId: String? = null

    val getEvent = StateLiveData(viewModelScope) {
        repository.getEvent(eventId)
    }

}