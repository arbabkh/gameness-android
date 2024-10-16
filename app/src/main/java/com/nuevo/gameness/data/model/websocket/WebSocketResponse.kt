package com.nuevo.gameness.data.model.websocket

import com.google.gson.annotations.SerializedName

data class WebSocketResponse(
    @SerializedName("arguments")
    val arguments: List<String>? = null,
    @SerializedName("target")
    val target: String? = null,
    @SerializedName("type")
    val type: Int? = null
)