package com.nuevo.gameness.data.model.tournaments

import com.google.gson.annotations.SerializedName

data class TournamentResponse(
    @SerializedName("Data")
    val data: TournamentData? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class TournamentData(
    @SerializedName("Id")
    var id: String? = null,
    @SerializedName("ImageId")
    val imageId: String? = null,
    @SerializedName("ImageUrl")
    val imageUrl: String? = null,
    @SerializedName("DetailImageId")
    val detailImageId: String? = null,
    @SerializedName("DetailImageUrl")
    val detailImageUrl: String? = null,
    @SerializedName("PlatformImageId")
    val platformImageId: String?=null,
    @SerializedName("PlatformImageUrl")
    val platformImageUrl: String?=null,
    @SerializedName("GameImageId")
    val gameImageId: String? = null,
    @SerializedName("GameImageUrl")
    val gameImageUrl: String? = null,
    @SerializedName("GameId")
    val gameId: String? = null,
    @SerializedName("GameName")
    val gameName: String? = null,
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
    @SerializedName("TournamentStatus")
    val tournamentStatus: Int? = null,
    @SerializedName("Link")
    val link: String? = null,
    @SerializedName("TeamPlayerCount")
    val teamPlayerCount: Int? = null,
    @SerializedName("TeamSubstitutePlayerCount")
    val teamSubstitutePlayerCount: Int? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("ConditionsOfParticipation")
    val conditionsOfParticipation: String? = null,
    @SerializedName("GeneralInformations")
    val generalInformations: String? = null,
    @SerializedName("ShortDescription")
    val shortDescription: String? = null,
    @SerializedName("CurrentParticipantCount")
    val currentParticipantCount: Int? = null,
    @SerializedName("RandomParticipantImages")
    val randomParticipantImages: List<TournamentDataRandomParticipantImages>? = null,
    @SerializedName("JoinButtonActive")
    val joinButtonActive: Boolean? = null,
    @SerializedName("Awards")
    val awards: List<TournamentDataAwards>? = null,
    @SerializedName("Referees")
    val referees: List<TournamentDataReferees>? = null,
    @SerializedName("UserJoined")
    val userJoined: Boolean? = null,
    @SerializedName("CancelButtonActive")
    val cancelButtonActive: Boolean? = null,
    @SerializedName("UserJoinStatus")
    val userJoinStatus: Int? = null,
    @SerializedName("TournamentAvailability")
    val tournamentAvailability: Int? = null
)

data class TournamentDataRandomParticipantImages(
    @SerializedName("ImageId")
    val imageId: String? = null,
    @SerializedName("ImageUrl")
    val imageUrl: String? = null
)

data class TournamentDataAwards(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("TournamentAwardOrder")
    val tournamentAwardOrder: Int? = null
)

data class TournamentDataReferees(
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("Surname")
    val surname: String? = null
)