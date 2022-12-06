package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class CarerLoginRequest (
    @SerializedName("loginToken")
    var loginToken: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("deviceToken")
    var deviceToken: String,
    @SerializedName("timezone")
    var timezone: Int,
    @SerializedName("locale")
    var locale: String
)