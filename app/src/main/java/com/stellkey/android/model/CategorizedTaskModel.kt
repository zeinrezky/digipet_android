package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class CategorizedTaskModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("titleFr")
    var titleFr: String,
    @SerializedName("tasks")
    var tasks: ArrayList<TaskModel>
)