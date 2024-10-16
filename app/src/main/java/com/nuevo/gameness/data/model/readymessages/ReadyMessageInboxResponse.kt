package com.nuevo.gameness.data.model.readymessages

import com.google.gson.annotations.SerializedName

data class ReadyMessageInboxResponse(
    @SerializedName("Data")
    val data: List<ReadyMessageInboxData>? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class ReadyMessageInboxData(
    @SerializedName("ChatUserId")
    val chatUserId: String? = null,
    @SerializedName("ChatUserName")
    val chatUserName: String? = null,
    @SerializedName("ChatUserImageId")
    val chatUserImageId: String? = null,
    @SerializedName("ChatUserImageUrl")
    val chatUserImageUrl: String? = null,
    @SerializedName("LastMessage")
    val lastMessage: String? = null,
    @SerializedName("LastMessageDate")
    val lastMessageDate: String? = null,
    @SerializedName("IsReaded")
    val isReaded: Boolean? = null
)