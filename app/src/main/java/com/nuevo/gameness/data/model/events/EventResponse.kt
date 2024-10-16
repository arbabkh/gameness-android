package com.nuevo.gameness.data.model.events

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("Data")
    val data: EventItem? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)
