package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class RegisterConfirmRequest(
    @SerializedName("token")
    var token: String,
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("deviceToken")
    var deviceToken: String,
    @SerializedName("timezone")
    var timezone: Int,
    @SerializedName("locale")
    var locale: String
)