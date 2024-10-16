package com.nuevo.gameness.data.model.settings.request

import com.google.gson.annotations.SerializedName

data class ChangeUsernameRequest(
    @SerializedName("OldUserName")
    val oldUserName: String? = null,
    @SerializedName("NewUserName")
    val newUserName: String? = null
)
