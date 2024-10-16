package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("name")
    val username: String? = null,
    @SerializedName("password")
    val password: String? = null
)
