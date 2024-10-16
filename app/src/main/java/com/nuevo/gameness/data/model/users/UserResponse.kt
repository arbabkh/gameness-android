package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("Data")
    val data: User? = null,
    @SerializedName("Success")
    val success: String?,
    @SerializedName("Message")
    val message: String? = null
)
