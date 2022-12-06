package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class CreateChildRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("iconId")
    var iconId: Int,
    @SerializedName("age")
    var age: Int,
    @SerializedName("settingLocale")
    var settingLocale: String,
    @SerializedName("settingTimezone")
    var settingTimezone: Int
)