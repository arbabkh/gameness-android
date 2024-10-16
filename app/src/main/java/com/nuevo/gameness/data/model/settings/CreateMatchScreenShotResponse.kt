package com.nuevo.gameness.data.model.settings

import com.google.gson.annotations.SerializedName

data class CreateMatchScreenShotResponse (
    @SerializedName("Data")
    val data: ScreenShotItem? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
    )