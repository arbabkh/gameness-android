package com.nuevo.gameness.data.repository

import com.nuevo.gameness.data.service.NerfService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: NerfService) {
}