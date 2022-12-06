package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class KidLoginRequest(
    @SerializedName("loginToken")
    var loginToken: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("deviceToken")
    var deviceToken: String,
    @SerializedName("settingTimezone")
    var settingTimezone: Int,
    @SerializedName("settingLocale")
    var settingLocale: String
)