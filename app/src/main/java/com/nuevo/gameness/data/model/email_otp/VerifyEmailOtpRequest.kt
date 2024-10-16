package com.nuevo.gameness.data.model.email_otp

import com.google.gson.annotations.SerializedName

data class VerifyEmailOtpRequest(
    @SerializedName("emailAddress") var phoneNumber: String,
    @SerializedName("reference") var reference: String,
    @SerializedName("otp") var otp: String
 )
