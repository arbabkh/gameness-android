package com.nuevo.gameness.data.model.readymessages

import com.google.gson.annotations.SerializedName

data class ReadyMessageResponse (
    @SerializedName("TotalCount")
    val totalCount: Long? = null,
    @SerializedName("Items")
    val items: List<MessageItem>? = null
)

data class MessageItem (
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("Culture")
    val culture: String? = null
)
