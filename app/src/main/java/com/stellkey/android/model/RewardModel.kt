package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class RewardModel(
    @SerializedName("id")
    var id: Int = -1,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("star_cost")
    var star_cost: Int = -1,
    @SerializedName("titleFr")
    var titleFr: String = "",
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)