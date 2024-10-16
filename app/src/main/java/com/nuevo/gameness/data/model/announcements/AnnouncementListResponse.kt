package com.nuevo.gameness.data.model.announcements

import com.google.gson.annotations.SerializedName

data class AnnouncementListResponse(
    @SerializedName("TotalCount")
    val totalCount: Long,
    @SerializedName("Items")
    val items: List<AnnouncementItem>? = null
)

data class AnnouncementItem(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("ImageId")
    val imageId: String? = null,
    @SerializedName("ImageUrl")
    val imageUrl: String? = null,
    @SerializedName("IconId")
    val iconId: String? = null,
    @SerializedName("IconUrl")
    val iconUrl: String? = null,
    @SerializedName("GameId")
    val gameId: String? = null,
    @SerializedName("GameName")
    val gameName: String? = null,
    @SerializedName("Link")
    val link: String? = null,
    @SerializedName("StartDate")
    val startDate: String? = null,
    @SerializedName("EndDate")
    val endDate: String? = null,
    @SerializedName("Title")
    val title: String? = null,
    @SerializedName("Description")
    val description: String? = null,

    var dateTime: String? = null
)
