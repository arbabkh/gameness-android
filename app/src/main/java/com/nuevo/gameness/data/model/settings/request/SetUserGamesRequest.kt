package com.nuevo.gameness.data.model.settings.request

import com.google.gson.annotations.SerializedName


data class SetUserGamesRequest (
    @SerializedName("selectedGames")
    val selectedGames: List<SelectedGame>
)

data class SelectedGame (
    @SerializedName("gameId")
    val gameID: String,
    @SerializedName("gameUniqueId")
    val gameUniqueID: String?
)
