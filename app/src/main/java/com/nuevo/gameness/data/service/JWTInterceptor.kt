package com.nuevo.gameness.data.service

import android.content.SharedPreferences
import com.nuevo.gameness.data.model.refreshtoken.RefreshTokenRequest
import com.nuevo.gameness.data.model.users.UserModel
import com.nuevo.gameness.utils.Constants
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class JWTInterceptor  constructor(private val apiService: NerfService, private val prefs: SharedPreferences): Interceptor {

    private val TAG = "JWTInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()


        val reqUrl = request.url.toString()

        if (reqUrl.endsWith(Constants.REGISTER) ||request.url.toString().endsWith(Constants.LOGIN) || request.url.toString().endsWith(Constants.REFRESH_TOKEN)) {
            return chain.proceed(request)
        }

        val response = chain.proceed(request)

        if (response.code != 401) {
            return  response
        }

        val token = prefs.getString("token", "") ?: ""
        val refreshToken = prefs.getString("refreshToken", "") ?: ""

        val result = runBlocking {
            return@runBlocking apiService.getRefreshToken(RefreshTokenRequest(token, refreshToken))
        }

        if (result.isSuccessful) {

             result.body()?.let {body ->

                if (body.user == null || body.token == null || body.expiresAt == null || body.refreshToken == null || body.refreshTokenExpiresAt == null) {
                    return response
                }

                UserModel.instance.id = body.user.id!!
                UserModel.instance.token = body.token
                UserModel.instance.tokenExpiresAt = body.expiresAt
                UserModel.instance.refreshToken = body.refreshToken
                UserModel.instance.refreshTokenExpiresAt = body.refreshTokenExpiresAt

                 saveTokens(UserModel.instance.token, UserModel.instance.refreshToken, UserModel.instance.tokenExpiresAt, UserModel.instance.refreshTokenExpiresAt)
                 saveUserId(UserModel.instance.id)
                 response.close()
                 return chain.proceed(request.newBuilder().build())
             }

        }



       return response
    }


    private fun saveTokens(token: String, refreshToken: String, tokenExpiresAt: String, refreshTokenExpiresAt: String) {
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.putString("refreshToken", refreshToken)
        editor.putString("tokenExpiresAt", tokenExpiresAt)
        editor.putString("refreshTokenExpiresAt", refreshTokenExpiresAt)
        editor.apply()
    }

    private fun saveUserId(id: String) {
        prefs.edit().putString("user_id", id).apply()
    }
}