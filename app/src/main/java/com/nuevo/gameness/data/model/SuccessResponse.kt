package com.nuevo.gameness.data.model

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)