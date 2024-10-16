package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("Data")
    val data: RegisterData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class RegisterData(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("PhoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("NormalizedEmail")
    val normalizedEmail: String? = null,
    @SerializedName("Email")
    val email: String? = null,
    @SerializedName("NormalizedUserName")
    val normalizedUserName: String? = null,
    @SerializedName("UserName")
    val userName: String? = null,
    @SerializedName("SteamId")
    val steamId: String? = null,
    @SerializedName("DiscordId")
    val discordId: String? = null,
    @SerializedName("BirthDate")
    val birthDate: String? = null,
    @SerializedName("Token")
    val token: String? = null,
    @SerializedName("RefreshToken")
    val refreshToken: String? = null,
    @SerializedName("ExpiresAt")
    val expiresAt: String? = null,
    @SerializedName("RefreshTokenExpiresAt")
    val refreshTokenExpiresAt: String? = null
)