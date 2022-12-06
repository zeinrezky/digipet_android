package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("locale")
    var locale: String,
    @SerializedName("timezone")
    var timezone: Int,
    @SerializedName("deviceToken")
    var deviceToken: String
)