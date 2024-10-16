package com.nuevo.gameness.ui.pages.personal.profile.myprofile.achievements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nuevo.gameness.data.repository.Repository
import com.nuevo.gameness.data.result.StateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel@Inject constructor(
    application: Application,
    private val repository: Repository
): AndroidViewModel(application){

    //son başarımlar
    val getUserAwardList= StateLiveData(viewModelScope){
        repository.userAwardList(
            maxResultCount = 10,
            sorting = "AcquisitionDate desc",
            skipCount = 0
        )
    }

}