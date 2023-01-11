package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class GlobalRewardsRequest(
    @SerializedName("star_cost")
    val star_cost: Int? = null
)