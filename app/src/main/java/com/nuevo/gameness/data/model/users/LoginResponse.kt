package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("User")
    val user: User? = null,
    @SerializedName("Token")
    val token: String? = null,
    @SerializedName("ExpiresAt")
    val expiresAt: String? = null,
    @SerializedName("RefreshToken")
    val refreshToken: String? = null,
    @SerializedName("RefreshTokenExpiresAt")
    val refreshTokenExpiresAt: String? = null,
)

