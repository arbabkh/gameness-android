package com.nuevo.gameness.data.model.settings

import com.google.gson.annotations.SerializedName

data class ScreenShotsResponse(
    @SerializedName("TotalCount")
    val totalCount: Int? = null,
    @SerializedName("Items")
    val items: List<ScreenShotItem>? = null
)

data class ScreenShotItem(
    @SerializedName("ParticipationType")
    val participationType: Int? = null,
    @SerializedName("TournamentId")
    val tournamentId: String? = null,
    @SerializedName("SelfPartyId")
    val selfPartyId: String? = null,
    @SerializedName("MatchId")
    val matchId: String? = null,
    @SerializedName("TournamentName")
    val tournamentName: String? = null,
    @SerializedName("MatchLabel")
    val matchLabel: String? = null,
    @SerializedName("MatchDate")
    val matchDate: String? = null,
    @SerializedName("FileId")
    val fileId: String? = null,
    @SerializedName("ImageUrl")
    val imageUrl: String? = null,

    ){
    override fun toString(): String {
        return matchLabel?:""
    }
}
