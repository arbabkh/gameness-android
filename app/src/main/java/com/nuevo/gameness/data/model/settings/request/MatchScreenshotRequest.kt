package com.nuevo.gameness.data.model.settings.request

import com.google.gson.annotations.SerializedName

data class MatchScreenshotRequest (
    @SerializedName("TournamentId")
    val tournamentId: String? = null,
    @SerializedName("SelfPartyId")
    val selfPartyId: String? = null,
    @SerializedName("MatchId")
    val matchId: String? = null,
        )