package com.nuevo.gameness.data.model.teamusers

import com.google.gson.annotations.SerializedName

data class UserToTeamInvitedListResponse(
    @SerializedName("Data")
    val data: List<UserToTeamInvitedListData>? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class UserToTeamInvitedListData(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("UserId")
    val userID: String? = null,
    @SerializedName("UserImageId")
    val userImageID: String? = null,
    @SerializedName("UserImageUrl")
    val userImageURL: String? = null,
    @SerializedName("UserName")
    val username: String? = null,
    @SerializedName("Status")
    val status: String? = null,
    @SerializedName("StatusId")
    val statusID: Int? = null
)