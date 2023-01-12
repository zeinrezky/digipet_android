package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class RewardAvailable(
    @SerializedName("globalReward")
    val globalReward: GlobalReward? = GlobalReward(),
    @SerializedName("globalRewardId")
    val globalRewardId: Int = -1,
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("kidId")
    val kidId: Int = -1,
    @SerializedName("reward")
    val reward: Reward? = Reward(),
    @SerializedName("rewardId")
    val rewardId: Int = -1
)

data class GlobalReward(
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("star_cost")
    val star_cost: Int = -1,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("titleFr")
    val titleFr: String = ""
)

data class Reward(
    @SerializedName("accountId")
    val accountId: Int = -1,
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("star_cost")
    val star_cost: Int = -1,
    @SerializedName("title")
    val title: String = ""
)

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