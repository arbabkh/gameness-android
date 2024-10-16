package com.nuevo.gameness.data.model.teamusers

import com.google.gson.annotations.SerializedName

data class SendTeamInviteRequest(
    @SerializedName("invitedId")
    val inviteID: String? = null,
    @SerializedName("Email")
    val email: String? = null
)
