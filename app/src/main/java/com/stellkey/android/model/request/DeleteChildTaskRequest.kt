package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class DeleteChildTaskRequest(
    @SerializedName("cycle")
    var cycle: String,
    @SerializedName("kidId")
    var kidId: Int,
    @SerializedName("challengeId")
    var challengeId: Int?,
    @SerializedName("globalChallengeId")
    var globalChallengeId: Int?
)