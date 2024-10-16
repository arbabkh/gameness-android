package com.nuevo.gameness.data.model.teamusers

import com.google.gson.annotations.SerializedName

data class TeamInviteDecisionRequest(
    @SerializedName("invitedId")
    val inviteID: String? = null,
    @SerializedName("accepted")
    val accepted: Boolean? = null
)