package com.nuevo.gameness.ui.pages.personal.profile.settings.screenshots

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.ScreenShotItem
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScreenShotsViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {
    var selected:ScreenShotItem? = null
    var fileForRequestBody: File? = null
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)

    val getScreenShots = StateLiveData(viewModelScope) {
        repository.getScreenShots(
        )
    }

    val uploadScreenshots = StateLiveData(viewModelScope) {
        repository.createMatchScreenshot(requestBody.build())
    }
}