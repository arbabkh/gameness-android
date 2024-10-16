package com.nuevo.gameness.data.model.settings

import com.google.gson.annotations.SerializedName

data class TeamUsersResponse (
    @SerializedName("Data")
    val data: List<Datum>?= null,
    @SerializedName("Success")
    val success: Boolean?= null,
    @SerializedName("Message")
    val message: String? = null
)

data class Datum (
    @SerializedName("GameName")
    val gameName: String? = null,
    @SerializedName("GameId")
    val gameID: String? = null,
    @SerializedName("TeamUsers")
    var teamUserItemList: List<TeamUserItem>?= null
)


