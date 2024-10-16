package com.nuevo.gameness.data.model.settings.request

import com.google.gson.annotations.SerializedName

data class ChangeTeamGameUserRequestElement (
    @SerializedName("gameId")
    var gameID: String? = null,
    @SerializedName("selectedTeamUserIds")
    var selectedTeamUserIDs: List<String>? = null
)
