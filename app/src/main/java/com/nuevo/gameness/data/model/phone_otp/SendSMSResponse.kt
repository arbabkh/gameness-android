package com.nuevo.gameness.data.model.phone_otp

import com.google.gson.annotations.SerializedName

data class SendSMSResponse(
    @SerializedName("Data") val data: String?,
    @SerializedName("Success") val success: Boolean?,
    @SerializedName("Message") val message: String?,

    )
