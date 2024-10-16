package com.nuevo.gameness.data.model.settings

import com.google.gson.annotations.SerializedName
import com.nuevo.gameness.data.model.users.User


data class TeamInvitationResponse(
    @SerializedName("Data")
    val teamInvitationItemList: List<TeamInvitationItem>,
    @SerializedName("Success")
    val success: Boolean,
    @SerializedName("Message")
    val message: String? = null
)
data class TeamInvitationItem(
    @SerializedName("Id")
    val id:String,
    @SerializedName("TeamName")
    val teamName:String? = null,
    @SerializedName("IconUrl")
    val iconUrl:String? = null
)
data class TeamUserListResponse (
    @SerializedName("Data")
    val teamUserItemList: List<TeamUserItem>,
    @SerializedName("Success")
    val success: Boolean,
    @SerializedName("Message")
    val message: String? = null

)

data class TeamUserItem (
    @SerializedName("TeamUserId")
    val teamUserID: String,
    @SerializedName("User")
    val user: User,
    var selected:Boolean=false
)
