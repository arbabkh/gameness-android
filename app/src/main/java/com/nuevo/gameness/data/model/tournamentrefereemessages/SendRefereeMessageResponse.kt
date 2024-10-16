package com.nuevo.gameness.data.model.tournamentrefereemessages

import com.google.gson.annotations.SerializedName

data class SendRefereeMessageResponse(
   @SerializedName("Data")
    val data: TournamentRefereeUserChatItem? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)
