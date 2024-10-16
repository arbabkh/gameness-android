package com.nuevo.gameness.data.model.readymessages

import com.google.gson.annotations.SerializedName

data class ReadyMessageHistoryResponse (
    @SerializedName("Data")
    val data: List<MessageHistoryItem>? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class MessageHistoryItem (
    @SerializedName("SenderId")
    val senderID: String? = null,
    @SerializedName("SenderImageId")
    val senderImageID: String? = null,
    @SerializedName("SenderImageUrl")
    val senderImageURL: String? = null,
    @SerializedName("RecipientId")
    val recipientID: String? = null,
    @SerializedName("RecipientImageId")
    val recipientImageID: String? = null,
    @SerializedName("RecipientImageUrl")
    val recipientImageURL: String? = null,
    @SerializedName("Message")
    val message: String? = null,
    @SerializedName("SendDate")
    val sendDate: String? = null
)
