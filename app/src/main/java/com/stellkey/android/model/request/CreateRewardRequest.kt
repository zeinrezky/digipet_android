package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class CreateRewardRequest (
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("star_cost")
    var star_cost: Int,
    @SerializedName("availability")
    var availability: String
)