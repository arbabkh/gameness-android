package com.nuevo.gameness.data.model.publicconfigurations

import com.google.gson.annotations.SerializedName

data class PublicConfigurationsResponse (
    @SerializedName("Data") var data: PublicConfiguration?,
    @SerializedName("Success") var success: Boolean?,
    @SerializedName("Message") var message: Boolean?
    )

data class PublicConfiguration(
    @SerializedName("SignUpEnabled") var signUpEnabled: Boolean,
    @SerializedName("SignUpPhoneVerificationRequired") var signUpPhoneVerificationRequired: Boolean,
    @SerializedName("SignUpEMailVerificationRequired") var signUpEMailVerificationRequired: Boolean,
    @SerializedName("AutoRefreshInterval") var autoRefreshInterval: Long,
    @SerializedName("PhoneVerificationRequired") var phoneVerificationRequired: Boolean,
    @SerializedName("EMailVerificationRequired") var emilVerificationRequired: Boolean
 )
