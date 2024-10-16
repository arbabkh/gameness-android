package com.nuevo.gameness.data.model.tournamentrooms

import com.google.gson.annotations.SerializedName

data class ScoreboardResponse(
    @SerializedName("Data")
    val data: List<ScoreboardData>? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)

data class ScoreboardData(
    @SerializedName("ParticipantName")
    val participantName: String? = null,
    @SerializedName("ParticipantImageId")
    val participantImageId: String? = null,
    @SerializedName("ParticipantImageUrl")
    val participantImageUrl: String? = null,
    @SerializedName("PlayedMatchCount")
    val playedMatchCount: Int? = null,
    @SerializedName("WonCount")
    val wonCount: Int? = null,
    @SerializedName("DrawCount")
    val drawCount: Int? = null,
    @SerializedName("LostCount")
    val lostCount: Int? = null,
    @SerializedName("Score")
    val score: Int? = null
)