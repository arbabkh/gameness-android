package com.nuevo.gameness.data.model.tournamentrooms

import com.google.gson.annotations.SerializedName

data class NextMatchTeamUsersResponse(
    @SerializedName("Data")
    val data: NextMatchTeams? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class NextMatchTeams(
    @SerializedName("HomeTeam")
    val homeTeam: NextMatchTeamData? = null,
    @SerializedName("AwayTeam")
    val awayTeam: NextMatchTeamData? = null
)

data class NextMatchTeamData(
    @SerializedName("TeamName")
    val teamName: String? = null,
    @SerializedName("TeamUsers")
    val teamUsers: List<NextMatchTeamUserData>? = null
)

data class NextMatchTeamUserData(
    @SerializedName("UserId")
    val userID: String? = null,
    @SerializedName("UserName")
    val userName: String? = null,
    @SerializedName("UserImageId")
    val userImageId: String? = null,
    @SerializedName("UserImageUrl")
    val userImageUrl: String? = null,
)