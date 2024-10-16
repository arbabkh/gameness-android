package com.nuevo.gameness.data.model.readymessages

import com.google.gson.annotations.SerializedName

data class ReadyMessageAsReadRequest(
    @SerializedName("tournamentId")
    val tournamentId: String? = null,
    @SerializedName("recipientId")
    val recipientId: String? = null
)