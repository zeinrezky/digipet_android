package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class BadgesModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("descriptionFr")
    var descriptionFr: String,
    @SerializedName("isOwned")
    var isOwned: Boolean
)