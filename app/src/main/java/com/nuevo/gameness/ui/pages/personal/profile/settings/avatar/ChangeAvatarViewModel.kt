package com.nuevo.gameness.ui.pages.personal.profile.settings.avatar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nuevo.gameness.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeAvatarViewModel@Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application)