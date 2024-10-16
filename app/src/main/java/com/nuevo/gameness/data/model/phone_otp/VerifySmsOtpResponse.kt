package com.nuevo.gameness.data.model.phone_otp

import com.google.gson.annotations.SerializedName

data class VerifySmsOtpResponse (
    @SerializedName("Data") var isValid: Boolean?,
    @SerializedName("Success") var success: Boolean?,
    @SerializedName("Message") var message: String?,
) {
    fun isValidOtp(): Boolean {
        return  isValid ?: false
    }
}