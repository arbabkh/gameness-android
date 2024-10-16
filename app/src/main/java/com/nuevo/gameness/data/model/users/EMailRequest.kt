package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class EMailRequest(
    @SerializedName("email")
    val email: String? = null
)
