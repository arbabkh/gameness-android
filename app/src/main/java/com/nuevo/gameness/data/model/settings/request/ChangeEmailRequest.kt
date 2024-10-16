package com.nuevo.gameness.data.model.settings.request

import com.google.gson.annotations.SerializedName

data class ChangeEmailRequest (
    @SerializedName("oldEmail")
    val oldEmail: String? = null,
    @SerializedName("newEmail")
    val newEmail: String? = null
)