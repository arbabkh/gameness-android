package com.nuevo.gameness.ui.pages.personal.announcements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    val getAnnouncements = StateLiveData(viewModelScope) {
        repository.getAnnouncements(
            sorting = "StartDate desc",
            skipCount = 0,
            maxResultCount = 20,
            isUserGameAnnouncementList = false
        )
    }

}