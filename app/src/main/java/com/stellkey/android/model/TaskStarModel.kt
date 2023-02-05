package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class TaskStarModel(
    @SerializedName("isCompleted")
    var isCompleted: Boolean,
    @SerializedName("taskChildModel")
    var taskChildModel: TaskChildModel = TaskChildModel()
)

data class TaskChildModel(
    @SerializedName("idTask")
    var idTask: Int = -1,
    @SerializedName("dateAssignments")
    var dateAssignments: String = "",
    @SerializedName("completedAt")
    var completedAt: String? = null,
    @SerializedName("confirmedAt")
    var confirmedAt: String? = null
)