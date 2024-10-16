package com.nuevo.gameness.data.model.tournaments

import com.google.gson.annotations.SerializedName

data class UserTournamentCountDownResponse(
    @SerializedName("Data")
    val data: UserTournamentCountDownData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class UserTournamentCountDownData(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("ImageId")
    val imageId: String? = null,
    @SerializedName("DetailImageId")
    val detailImageId: String? = null,
    @SerializedName("GameId")
    val gameId: String? = null,
    @SerializedName("StartDate")
    val startDate: String? = null,
    @SerializedName("EndDate")
    val endDate: String? = null,
    @SerializedName("RegistrationStartDate")
    val registrationStartDate: String? = null,
    @SerializedName("RegistrationEndDate")
    val registrationEndDate: String? = null,
    @SerializedName("MaxParticipantsCount")
    val maxParticipantsCount: Int? = null,
    @SerializedName("TournamentType")
    val tournamentType: Int? = null,
    @SerializedName("TournamentParticipationType")
    val tournamentParticipationType: Int? = null,
    @SerializedName("Status")
    val status: Int? = null,
    @SerializedName("TournamentStatus")
    val tournamentStatus: Int? = null,
    @SerializedName("Link")
    val link: String? = null,
    @SerializedName("TeamPlayerCount")
    val teamPlayerCount: Int? = null,
    @SerializedName("TeamSubstitutePlayerCount")
    val teamSubstitutePlayerCount: Int? = null
)