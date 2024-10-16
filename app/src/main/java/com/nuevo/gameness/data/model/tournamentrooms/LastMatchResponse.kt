package com.nuevo.gameness.data.model.tournamentrooms

import com.google.gson.annotations.SerializedName

data class LastMatchResponse(
    @SerializedName("Data")
    val data: LastMatchData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class LastMatchData(
    @SerializedName("HomeName")
    val homeName: String? = null,
    @SerializedName("HomeImageId")
    val homeImageId: String? = null,
    @SerializedName("HomeScore")
    val homeScore: Int? = null,
    @SerializedName("HomeLogoUrl")
    val homeLogoUrl: String? = null,
    @SerializedName("AwayName")
    val awayName: String? = null,
    @SerializedName("AwayImageId")
    val awayImageId: String? = null,
    @SerializedName("AwayScore")
    val awayScore: Int? = null,
    @SerializedName("AwayLogoUrl")
    val awayLogoUrl: String? = null
)