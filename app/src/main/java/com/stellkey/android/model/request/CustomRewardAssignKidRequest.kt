package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version CustomRewardAssignKidRequest, v 0.1 11/01/23 19.40 by Nicolas Manurung
 */
data class CustomRewardAssignKidRequest(
    @SerializedName("kidId")
    val kidId: List<Int>,
    @SerializedName("rewardId")
    val rewardId: Int
)

data class GlobalRewardAssignKidRequest(
    @SerializedName("kidId")
    val kidId: List<Int>,
    @SerializedName("globalRewardId")
    val globalRewardId: Int
)
