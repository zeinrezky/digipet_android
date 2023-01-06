package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class CarerLogModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("kidId")
    var kidId: Int,
    @SerializedName("carerId")
    var carerId: Int,
    @SerializedName("challengeId")
    var challengeId: Int,
    @SerializedName("globalChallengeId")
    var globalChallengeId: Int,
    @SerializedName("globalRewardId")
    var globalRewardId: Int,
    @SerializedName("hide")
    var hide: Int,
    @SerializedName("rewardId")
    var rewardId: Int,
    @SerializedName("assignmentCycle")
    var assignmentCycle: String,
    @SerializedName("kid")
    var kid: AllKidsModel,
    @SerializedName("challenge")
    var challenge: Any,
    @SerializedName("globalChallenge")
    var globalChallenge: GlobalChallengeModel,
    @SerializedName("carer")
    var carer: AllCarersModel,
    @SerializedName("reward")
    var reward: Any,
    @SerializedName("dateRange")
    var dateRange: String
)