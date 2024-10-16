package com.nuevo.gameness.data.model.tournaments

import com.google.gson.annotations.SerializedName

data class TournamentIdRequest(
    @SerializedName("tournamentId")
    val tournamentId: String? = null
)