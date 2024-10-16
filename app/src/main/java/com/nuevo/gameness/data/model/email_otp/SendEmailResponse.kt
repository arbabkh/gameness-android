package com.nuevo.gameness.data.model.email_otp

import com.google.gson.annotations.SerializedName

data class SendEmailResponse(
    @SerializedName("Data") val data: String?,
    @SerializedName("Success") val success: Boolean?,
    @SerializedName("Message") val message: String?,
    )
