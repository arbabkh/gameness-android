package com.nuevo.gameness.data.model.register

import com.google.gson.annotations.SerializedName

data class Country (
    @SerializedName("CallingCode")
    val callingCode: String,
    @SerializedName("CountryCode")
    val countryCode: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Flag")
    val flag: String
)

data class GetCountriesResponse (
    @SerializedName("Data")
    val data: Array<Country>? = null,
    @SerializedName("Success")
    val success: Boolean? = null,
    @SerializedName("Message")
    val message: String? = null
)