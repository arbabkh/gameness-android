package com.nuevo.gameness.data.model.settings.request

import com.google.gson.annotations.SerializedName

data class ChangeUserInformationRequest (
    @SerializedName("birthDate")
    val birthDate: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null
)