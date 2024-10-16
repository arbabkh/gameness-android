package com.nuevo.gameness.data.model.readymessages

import com.google.gson.annotations.SerializedName

data class SendReadyMessageResponse (
    @SerializedName("Data")
    val data: SendReadyMessageData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class SendReadyMessageData (
    @SerializedName("SenderImageUrl")
    val senderImageUrl: String? = null,
    @SerializedName("ReadyMessageId")
    val readyMessageID: String? = null,
    @SerializedName("LanguageId")
    val languageID: String? = null,
    @SerializedName("Culture")
    val culture: String? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("Id")
    val id: String? = null
)