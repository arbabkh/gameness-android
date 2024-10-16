package com.nuevo.gameness.data.model.sliders

import com.google.gson.annotations.SerializedName

data class SliderListResponse (
    @SerializedName("TotalCount")
    val totalCount: Long,
    @SerializedName("Items")
    val items: List<SliderListItem>? = null
)

data class SliderListItem(
    @SerializedName("Title")
    val title: String? = null,
    @SerializedName("Description")
    val description: String? = null
)