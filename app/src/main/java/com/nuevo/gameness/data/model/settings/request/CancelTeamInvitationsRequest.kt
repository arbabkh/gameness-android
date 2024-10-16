package com.nuevo.gameness.data.model.settings.request

import com.google.gson.annotations.SerializedName

data class CancelTeamInvitationsRequest (
    @SerializedName("invitationIds")
    val invitationIds: List<String?>
)
