package com.nuevo.gameness.data.model.phone_otp

import com.google.gson.annotations.SerializedName

data class VerifySMSOtpRequest(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("reference") val reference: String,
    @SerializedName("otp") val otp: String
 )
