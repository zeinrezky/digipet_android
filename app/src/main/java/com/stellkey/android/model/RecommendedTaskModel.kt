package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class RecommendedTaskModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("challengeCatId")
    var challengeCatId: Int,
    @SerializedName("maxAge")
    var maxAge: Int,
    @SerializedName("minAge")
    var minAge: Int,
    @SerializedName("titleFr")
    var titleFr: String
)