package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class VerifyPasswordCodeRequest(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("passCode")
    val passCode: String? = null
)
