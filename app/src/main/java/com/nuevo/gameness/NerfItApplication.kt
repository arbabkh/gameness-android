package com.nuevo.gameness

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NerfItApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}