package com.nuevo.gameness.data.model.tournaments

import com.google.gson.annotations.SerializedName

data class TournamentFixtureResponse(
    @SerializedName("Data")
    val data: List<TournamentFixtureData>?,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class TournamentFixtureData(
    @SerializedName("StageText")
    var stageText: String? = null,
    @SerializedName("IsLowerBracket")
    val isLowerBracket: Boolean? = null,
    @SerializedName("Matches")
    val matches: List<TournamentFixtureMatches>? =  null
)

data class TournamentFixtureMatches(
    @SerializedName("AwayImageId")
    val awayImageId: String? =  null,
    @SerializedName("AwayImageUrl")
    val awayImageUrl: String? =  null,
    @SerializedName("AwayName")
    val awayName: String? =  null,
    @SerializedName("AwayScore")
    val awayScore: Int? =  null,
    @SerializedName("HomeImageId")
    val homeImageId: String? =  null,
    @SerializedName("HomeImageUrl")
    val homeImageUrl: String? =  null,
    @SerializedName("HomeName")
    val homeName: String? =  null,
    @SerializedName("HomeScore")
    val homeScore: Int? =  null,
    @SerializedName("MatchNumber")
    val matchNumber: Int? =  null,
    @SerializedName("NextMatchNumber")
    val nextMatchNumber: Int? =  null
)