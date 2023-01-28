package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName


data class AssignmentReminderRequest(
    @SerializedName("assignmentId")
    val assignmentId: Int
)