package com.nuevo.gameness.data.model.tournamentrefereemessages

import com.google.gson.annotations.SerializedName

data class RefereeMessageAsReadRequest(
    @SerializedName("tournamentId")
    val tournamentId: String? = null
)