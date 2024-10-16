package com.nuevo.gameness.data.model.tournaments

import com.google.gson.annotations.SerializedName

data class IsJoinedTournamentResponse(
    @SerializedName("Data")
    val data: IsJoinedTournamentData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class IsJoinedTournamentData(
    @SerializedName("UserJoinStatus")
    val userJoinStatus: Int? = null
)