package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class RewardListModel (
    @SerializedName("rewards")
    var rewards: ArrayList<RewardModel>
)