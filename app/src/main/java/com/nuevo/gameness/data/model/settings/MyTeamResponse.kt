package com.nuevo.gameness.data.model.settings

import com.google.gson.annotations.SerializedName


data class MyTeamResponse (
    @SerializedName("Data")
    val data: TeamData,
    @SerializedName("Success")
    val success: Boolean,
    @SerializedName("Message")
    val message: String? = null
)

data class TeamData (
    @SerializedName("Name")
    val name: String,
    @SerializedName("CreatedBy")
    val createdBy: String,
    @SerializedName("LogoId")
    val logoID: String,
    @SerializedName("Status")
    val status: Long,
    @SerializedName("LogoUrl")
    val logoURL: String,
    @SerializedName("Id")
    val id: String
)
