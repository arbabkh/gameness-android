package com.nuevo.gameness.base

import androidx.lifecycle.AndroidViewModel
import com.nuevo.gameness.NerfItApplication
import com.nuevo.gameness.data.repository.Repository
import javax.inject.Inject

open class BaseViewModel @Inject constructor(
    private val repository: Repository,
    application: NerfItApplication
) : AndroidViewModel(application) {
}