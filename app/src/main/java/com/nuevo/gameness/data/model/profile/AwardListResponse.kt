package com.nuevo.gameness.data.model.profile

import com.google.gson.annotations.SerializedName


data class AwardListResponse (
    @SerializedName("TotalCount")
    val totalCount: Long,
    @SerializedName("Items")
    val items: List<AwardsItem>
)

data class AwardsItem (
    @SerializedName("Id")
    val id: String,
    @SerializedName("TournamentName")
    val tournamentName: String,
    @SerializedName("AwardName")
    val awardName: String,
    @SerializedName("GameName")
    val gameName: String,
    @SerializedName("GameImageId")
    val gameImageID: String,
    @SerializedName("GameImageUrl")
    val gameImageURL: String,
    @SerializedName("IconImageUrl")
    val iconImageURL: String,
    @SerializedName("AcquisitionDate")
    val acquisitionDate: String,
    @SerializedName("IconImageId")
    val iconImageID: String
)
