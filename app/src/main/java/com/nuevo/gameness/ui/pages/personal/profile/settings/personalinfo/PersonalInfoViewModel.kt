package com.nuevo.gameness.ui.pages.personal.profile.settings.personalinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.model.settings.request.ChangeUserInformationRequest
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    var request: ChangeUserInformationRequest?=null

    val getUser= StateLiveData(viewModelScope){
        repository.getUser()
    }

    val changeUserInformation=StateLiveData(viewModelScope){
        repository.changeUserInformation(request!!)
    }

    val countryList = StateLiveData(viewModelScope) {
        repository.getCountryList()
    }
}