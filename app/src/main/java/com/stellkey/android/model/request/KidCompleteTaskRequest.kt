package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class KidCompleteTaskRequest(
    @SerializedName("assignmentId")
    var assignmentId: Int
)