package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class EditKidRequest (
    @SerializedName("name")
    var name: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("age")
    var age: Int,
    @SerializedName("iconId")
    var iconId: Int,
    @SerializedName("settingTimezone")
    var settingTimezone: Int,
    @SerializedName("settingLocale")
    var settingLocale: String
)