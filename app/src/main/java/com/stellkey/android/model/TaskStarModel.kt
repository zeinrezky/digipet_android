package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class TaskStarModel (
    @SerializedName("isCompleted")
    var isCompleted: Boolean
)