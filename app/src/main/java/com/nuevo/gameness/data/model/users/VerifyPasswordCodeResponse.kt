package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class VerifyPasswordCodeResponse(
    @SerializedName("Data")
    val data: VerifyPasswordCodeData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class VerifyPasswordCodeData(
    @SerializedName("Token")
    val token: String? = null,
    @SerializedName("ResetToken")
    val resetToken: String? = null,
    @SerializedName("RemainingAttempts")
    val remainingAttempts: Int? = null,
    @SerializedName("ProcessTerminated")
    val processTerminated: Boolean? = null
)
