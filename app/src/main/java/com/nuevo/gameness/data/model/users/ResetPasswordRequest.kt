package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("resetToken")
    val resetToken: String? = null,
    @SerializedName("newPassword")
    val newPassword: String? = null
)
