package com.nuevo.gameness.data.model.phone_otp

import com.google.gson.annotations.SerializedName

data class SendSMSRequest(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("language") val language: String
)


