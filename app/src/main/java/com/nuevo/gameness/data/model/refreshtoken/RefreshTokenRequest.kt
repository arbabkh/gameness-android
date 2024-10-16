package com.nuevo.gameness.data.model.refreshtoken

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest (
    @SerializedName("Token")
    val token: String?,
    @SerializedName("RefreshToken")
    val refreshToken: String?,
)