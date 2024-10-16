package com.nuevo.gameness.di

import android.content.Context
import android.content.SharedPreferences
import com.nuevo.gameness.data.service.HeaderInterceptor
import com.nuevo.gameness.data.service.JWTInterceptor
import com.nuevo.gameness.data.service.NerfService
import com.nuevo.gameness.utils.Constants.BASE_URL
import com.nuevo.gameness.utils.Constants.PREFS_FILENAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHeaderInterceptor(pref: SharedPreferences): HeaderInterceptor = HeaderInterceptor(pref)

    @Singleton
    @Provides
    fun provideJWTInterceptor(@Named("simple_nerft_service")  apiService: NerfService, pref: SharedPreferences): JWTInterceptor = JWTInterceptor(apiService, pref)

    @Singleton
    @Provides
    fun provideHttpClient(
        interceptor: HeaderInterceptor,
        jwtInterceptor: JWTInterceptor,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .addInterceptor(jwtInterceptor)
//            .addInterceptor {chain ->
//                val request = chain.request()
//                val response = chain.proceed(request)
//
//                Log.e("NetworkModule", request.url.toString() + "   " + response.code )
//                Log.e("NetworkModule",  response.body.bytes().toString())
//                return@addInterceptor response
//            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    @Named("simple_retrofit")
    fun provideSimpleRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Singleton
    @Provides
    fun provideCurrencyService(
        retrofit: Retrofit
    ): NerfService = retrofit.create(NerfService::class.java)

    @Singleton
    @Provides
    @Named("simple_nerft_service")
    fun provideSimpleCurrencyService(
       @Named("simple_retrofit") retrofit: Retrofit
    ): NerfService = retrofit.create(NerfService::class.java)



    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences = appContext.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
}