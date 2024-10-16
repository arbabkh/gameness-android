package com.nuevo.gameness.ui.pages.personal.profile.myprofile

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
class MyProfileViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application){

    var fileForRequestBody: File? = null
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)

    val changeProfilePicture = StateLiveData(viewModelScope) {
        repository.changeProfilePicture(requestBody.build())
    }
    val getUser= StateLiveData(viewModelScope){
        repository.getUser()
    }

    val myTeam= StateLiveData(viewModelScope){
        repository.myTeam()
    }

}