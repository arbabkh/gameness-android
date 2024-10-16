package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class UserListByUserNameResponse(
    @SerializedName("TotalCount")
    val totalCount: Int? = null,
    @SerializedName("Items")
    val items: List<UserListByUserNameItem>? = null
)

data class UserListByUserNameItem(
    @SerializedName("Id")
    val id: String? = null,
    @SerializedName("UserName")
    val username: String? = null,
    @SerializedName("ImageId")
    val imageID: String? = null,
    @SerializedName("ImageUrl")
    val imageURL: String? = null
)