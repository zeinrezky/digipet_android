package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class ChallengeCategoryModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("titleFr")
    var titleFr: String,
    @SerializedName("icon")
    var icon: String
)