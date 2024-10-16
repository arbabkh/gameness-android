package com.nuevo.gameness.data.model.settings

import com.google.gson.annotations.SerializedName

data class IconChangeResponse(
    @SerializedName("Items")
    val items: List<IconItem>? = null
)

data class IconItem(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("FilePath")
    val filePath: String? = null,

    var selected: Boolean = false
)

