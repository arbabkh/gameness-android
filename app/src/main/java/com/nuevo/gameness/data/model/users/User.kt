package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("Surname")
    val surname: String? = null,
    @SerializedName("PhoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("NormalizedEmail")
    val normalizedEmail: String? = null,
    @SerializedName("Email")
    val email: String? = null,
    @SerializedName("NormalizedUserName")
    val normalizedUserName: String? = null,
    @SerializedName("UserName")
    val userName: String? = null,
    @SerializedName("SteamId")
    val steamId: String? = null,
    @SerializedName("DiscordId")
    val discordId: String? = null,
    @SerializedName("BirthDate")
    val birthDate: String? = null,
    @SerializedName("TeamId")
    val teamId: String? = null,
    @SerializedName("IsTeamLeader")
    val isTeamLeader: Boolean = false,
    @SerializedName("ImageId")
    val imageId: String? = null,
    @SerializedName("ImageUrl")
    val imageUrl: String? = null,
    @SerializedName("PhoneNumberConfirmed")
    val phoneNumberConfirmed: Boolean? ,
    @SerializedName("EmailConfirmed")
    val emailConfirmed: Boolean?,
    @SerializedName("phoneNumberCountryCallingCode")
    val phoneNumberCountryCallingCode: String?

)
