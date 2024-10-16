package com.nuevo.gameness.data.model.email_otp

import com.google.gson.annotations.SerializedName

data class EmailVerificationResponse(
    @SerializedName("Message") var message: String?,
    @SerializedName("Data") var data: Boolean?,
    @SerializedName("Success") var success: Boolean?,
)
