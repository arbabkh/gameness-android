package com.nuevo.gameness.data.model.events

import com.google.gson.annotations.SerializedName

data class EventListResponse (
    @SerializedName("TotalCount")
    val totalCount: Long,
    @SerializedName("Items")
    val items: List<EventItem>? = null
)

data class EventItem (
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("ImageId")
    val imageID: String? = null,
    @SerializedName("ImageUrl")
    val imageURL: String? = null,
    @SerializedName("DetailImageId")
    val detailImageID: String? = null,
    @SerializedName("DetailImageUrl")
    val detailImageURL: String? = null,
    @SerializedName("GameId")
    val gameID: String? = null,
    @SerializedName("GameName")
    val gameName: String? = null,
    @SerializedName("Link")
    val link: String? = null,
    @SerializedName("StartDate")
    val startDate: String? = null,
    @SerializedName("EndDate")
    val endDate: String? = null,
    @SerializedName("EventDate")
    val eventDate: String? = null,
    @SerializedName("Culture")
    val culture: String? = null,
    @SerializedName("Title")
    val title: String? = null,
    @SerializedName("ShortDescription")
    val shortDescription: String? = null,
    @SerializedName("EventInformations")
    val eventInformations: String? = null,
    @SerializedName("ConditionsOfParticipation")
    val conditionsOfParticipation: String? = null,
    @SerializedName("Place")
    val place: String? = null
)
