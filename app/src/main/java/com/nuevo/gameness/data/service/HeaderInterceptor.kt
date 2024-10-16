package com.nuevo.gameness.data.service

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(private val preferences: SharedPreferences)  : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {

        val token = preferences.getString("token", "") ?: ""

        val request = chain.request().newBuilder()
            .addHeader("Authorization" , "Bearer ${token}")
            .addHeader("Accept", "application/json")
            .addHeader("Content-type", "application/json")
            .addHeader("Accept-Language", Locale.getDefault().language)
            .build()

        return chain.proceed(request)
    }
}