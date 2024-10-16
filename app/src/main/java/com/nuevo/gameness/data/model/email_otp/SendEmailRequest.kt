package com.nuevo.gameness.data.model.email_otp

import com.google.gson.annotations.SerializedName

data class SendEmailRequest(
    @SerializedName("emailAddress") val emailAddress: String,
    @SerializedName("language") val language: String
)


