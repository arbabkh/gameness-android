package com.nuevo.gameness.data.model.users

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserGamesForRegisterRequest(
    @SerializedName("steamId")
    val steamId: String? = null,
    @SerializedName("discordId")
    val discordId: String? = null,
    @SerializedName("selectedGames")
    val selectedGames: List<SelectedGameRequest>? = null
)

@Parcelize
data class SelectedGameRequest(
    @SerializedName("gameId")
    val gameId: String? = null,
    @SerializedName("gameUniqueId")
    val gameUniqueId: String? = null
): Parcelable