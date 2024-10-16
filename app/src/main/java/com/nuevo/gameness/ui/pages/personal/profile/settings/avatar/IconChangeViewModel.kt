package com.nuevo.gameness.ui.pages.personal.profile.settings.avatar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.request.PreDefinedProfilePictureRequest
import com.nuevo.gameness.data.model.settings.request.SetUserGamesRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IconChangeViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var request:SetUserGamesRequest?=null

    var fileId:String? = null
    val getProfileIcons = StateLiveData(viewModelScope) {
        repository.getPreDefinedProfileIcons(
        )
    }
    val setProfileIcon= StateLiveData(viewModelScope){
        repository.setPreDefinedProfileIcon(PreDefinedProfilePictureRequest(fileId))
    }
}