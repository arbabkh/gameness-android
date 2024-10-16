package com.nuevo.gameness.data.model.tournamentrooms

import com.google.gson.annotations.SerializedName

data class NextMatchResponse(
    @SerializedName("Data")
    val data: NextMatchData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class NextMatchData(
    @SerializedName("HomeId")
    val homeId: String? = null,
    @SerializedName("HomeName")
    val homeName: String? = null,
    @SerializedName("HomeImageId")
    val homeImageId: String? = null,
    @SerializedName("HomeLogoUrl")
    val homeLogoImageUrl: String? = null,
    @SerializedName("AwayId")
    val awayId: String? = null,
    @SerializedName("AwayName")
    val awayName: String? = null,
    @SerializedName("AwayImageId")
    val awayImageId: String? = null,
    @SerializedName("AwayLogoUrl")
    val awayLogoUrl: String? = null,
    @SerializedName("NextMatchDate")
    val nextMatchDate: String? = null
)