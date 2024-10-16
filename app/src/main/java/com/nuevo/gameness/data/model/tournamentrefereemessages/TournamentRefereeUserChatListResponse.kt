package com.nuevo.gameness.data.model.tournamentrefereemessages

import com.google.gson.annotations.SerializedName

data class TournamentRefereeUserChatListResponse(
    @SerializedName("TotalCount")
    val totalCount: Long? = null,
    @SerializedName("Items")
    val items: List<TournamentRefereeUserChatItem>? = null
)

data class TournamentRefereeUserChatItem(
    @SerializedName("Message")
    val message: String? = null,
    @SerializedName("IsAdminResponse")
    val isAdminResponse: Boolean? = null,
    @SerializedName("SendDate")
    val sendDate: String? = null,
    @SerializedName("MessageType")
    val messageType: Int
)