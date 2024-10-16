package com.nuevo.gameness.data.model.tournaments

import com.google.gson.annotations.SerializedName

data class TournamentListResponse(
    @SerializedName("TotalCount")
    val totalCount: Long,
    @SerializedName("Items")
    val items: List<TournamentItem>? = null
)

data class TournamentItem(
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
    @SerializedName("CurrentParticipantCount")
    val currentParticipantCount: Int? = null,
    @SerializedName("RandomParticipantImages")
    val randomParticipantImages: List<TournamentItemRandomParticipantImages>? = null,
    @SerializedName("JoinButtonActive")
    val joinButtonActive: Boolean? = null,
    @SerializedName("UserJoined")
    val userJoined: Boolean? = null,
    @SerializedName("ShortDescription")
    val shortDescription: String? = null,
    @SerializedName("CancelButtonActive")
    val cancelButtonActive: Boolean? = null,
    @SerializedName("UserJoinStatus")
    val userJoinStatus: Int? = null
)

data class TournamentItemRandomParticipantImages(
    @SerializedName("ImageId")
    val imageId: String? = null,
    @SerializedName("ImageUrl")
    val imageUrl: String? = null
)