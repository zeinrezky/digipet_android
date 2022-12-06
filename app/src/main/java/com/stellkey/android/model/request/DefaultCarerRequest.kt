package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class DefaultCarerRequest (
    @SerializedName("name")
    var name: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("iconId")
    var iconId: Int
)