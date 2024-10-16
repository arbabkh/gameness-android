package com.nuevo.gameness.data.model.users

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("surname")
    val surname: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
    @SerializedName("userName")
    val userName: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("phoneNumberCountryCallingCode")
    val phoneNumberCountryCallingCode: String? = null
)
