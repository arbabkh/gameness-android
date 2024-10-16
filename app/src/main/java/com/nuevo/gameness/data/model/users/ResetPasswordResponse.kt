package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
    @SerializedName("Data")
    val data: User? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)
