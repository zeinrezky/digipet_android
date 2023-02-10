package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class TaskModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("titleFr")
    var titleFr: String = "",
    @SerializedName("icon")
    var icon: String,
    @SerializedName("isGlobal")
    var isGlobal: Boolean,
    @SerializedName("challengeCatId")
    var challengeCatId: Int
)