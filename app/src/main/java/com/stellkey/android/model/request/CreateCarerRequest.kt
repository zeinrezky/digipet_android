package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class CreateCarerRequest (
    @SerializedName("name")
    var name: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("canCreate")
    var canCreate: Boolean,
    @SerializedName("iconId")
    var iconId: Int
)