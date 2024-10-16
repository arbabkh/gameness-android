package com.nuevo.gameness.ui.pages.personal.profile.myteam.createteam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateTeamViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application) {

    var fileForRequestBody: File? = null
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)

    val getGameList = StateLiveData(viewModelScope) {
        repository.getGameList(
            takeCount = 10,
            sorting = "id desc",
            skipCount = 0
        )
    }

    val createTeam = StateLiveData(viewModelScope) {
        repository.createTeam(requestBody.build())
    }

}