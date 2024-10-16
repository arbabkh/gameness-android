package com.nuevo.gameness.data.model.games

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GameListResponse(
    @SerializedName("TotalCount")
    val totalCount: Long,
    @SerializedName("Items")
    val items: List<GameListItem>? = null
)

@Parcelize
data class GameListItem(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("ImageUrl")
    val imageUrl: String? = null,
    @SerializedName("UniqueID")
    var uniqueID: String?=null,

    var selected: Boolean = false
): Parcelable
