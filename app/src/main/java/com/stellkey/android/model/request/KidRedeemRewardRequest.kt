package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class KidRedeemRewardRequest(
    @SerializedName("globalRewardId")
    val globalRewardId: Int?,
    @SerializedName("rewardId")
    val rewardId: Int?
)