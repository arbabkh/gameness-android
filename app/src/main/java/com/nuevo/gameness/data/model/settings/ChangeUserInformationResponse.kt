package com.nuevo.gameness.data.model.settings

import com.google.gson.annotations.SerializedName


data class ChangeUserInformationResponse (
    @SerializedName("Data")
    val data: Data,
    @SerializedName("Success")
    val success: Boolean,
    @SerializedName("Message")
    val message: String? = null
)

data class Data (
    @SerializedName("Name")
    val name: String,
    @SerializedName("Surname")
    val surname: String,
    @SerializedName("PhoneNumber")
    val phoneNumber: String,
    @SerializedName("NormalizedEmail")
    val normalizedEmail: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("NormalizedUserName")
    val normalizedUserName: String,
    @SerializedName("UserName")
    val userName: String,
    @SerializedName("SteamId")
    val steamID: String? = null,
    @SerializedName("DiscordId")
    val discordID: String? = null,
    @SerializedName("BirthDate")
    val birthDate: String,
    @SerializedName("TeamId")
    val teamID: String? = null,
    @SerializedName("IsTeamLeader")
    val isTeamLeader: Boolean? = null,
    @SerializedName("ImageId")
    val imageID: String? = null,
    @SerializedName("ImageUrl")
    val imageURL: String? = null,
    @SerializedName("Id")
    val id: String
)

