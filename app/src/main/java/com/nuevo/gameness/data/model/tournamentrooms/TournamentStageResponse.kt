package com.nuevo.gameness.data.model.tournamentrooms

import com.google.gson.annotations.SerializedName

data class TournamentStageResponse(
    @SerializedName("Data")
    val data: TournamentStageData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class TournamentStageData(
    @SerializedName("Stage")
    val stage: Int? = null
)