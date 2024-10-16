package com.nuevo.gameness.data.model.readymessages

data class SendReadyMessageRequest (
    val tournamentId: String? = null,
    val readyMessageId: String? = null,
    val recipientId: String? = null
)
