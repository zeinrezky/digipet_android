package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class GroupedChallengesModel (
    @SerializedName("recommendedTasks")
    var recommendedTasks: ArrayList<RecommendedTaskModel>,
    @SerializedName("categories")
    var categories: ArrayList<CategorizedTaskModel>
)