package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class AssignmentsModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("isActive")
    var isActive: Boolean,
    @SerializedName("assignDate")
    var assignDate: String,
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("challengeId")
    var challengeId: Int,
    @SerializedName("globalChallengeId")
    var globalChallengeId: Int,
    @SerializedName("kidId")
    var kidId: Int,
    @SerializedName("createdById")
    var createdById: Int,
    @SerializedName("confirmedById")
    var confirmedById: Int,
    @SerializedName("declinedById")
    var declinedById: Int,
    @SerializedName("completedAt")
    var completedAt: String?,
    @SerializedName("confirmedAt")
    var confirmedAt: String?,
    @SerializedName("declinedAt")
    var declinedAt: String?,
    @SerializedName("cycle")
    var cycle: String,
    @SerializedName("pnSentAt")
    var pnSentAt: String,
    @SerializedName("remindedTimes")
    var remindedTimes: Int,
    @SerializedName("remindedAt")
    var remindedAt: String,
    @SerializedName("challenge")
    var challenge: Any,
    @SerializedName("globalChallenge")
    var globalChallenge: GlobalChallengeModel,
    @SerializedName("formattedDate")
    var formattedDate: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("icon")
    var icon: String

)