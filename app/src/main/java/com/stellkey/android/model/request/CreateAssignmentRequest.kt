package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class CreateAssignmentRequest(
    @SerializedName("challengeId")
    var challengeId: Int?,
    @SerializedName("globalChallengeId")
    var globalChallengeId: Int?,
    @SerializedName("kidId")
    var kidId: ArrayList<Int>,
    @SerializedName("startDate")
    var startDate: String
)