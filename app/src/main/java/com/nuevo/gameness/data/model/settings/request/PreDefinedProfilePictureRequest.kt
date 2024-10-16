package com.nuevo.gameness.data.model.settings.request

import com.google.gson.annotations.SerializedName

data class PreDefinedProfilePictureRequest (
    @SerializedName("fileId")
    val fileId: String? = null

)