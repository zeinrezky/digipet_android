package com.stellkey.android.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class KidLogModel(
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("assignmentCycle")
    val assignmentCycle: String,
    @SerializedName("carer")
    val carer: Carer,
    @SerializedName("carerId")
    val carerId: Int,
    @SerializedName("challenge")
    val challenge: Challenge? = null,
    @SerializedName("challengeId")
    val challengeId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("globalChallenge")
    val globalChallenge: GlobalChallenge? = null,
    @SerializedName("globalChallengeId")
    val globalChallengeId: Int,
    @SerializedName("globalReward")
    val globalReward: GlobalReward? = null,
    @SerializedName("globalRewardId")
    val globalRewardId: Int,
    @SerializedName("hide")
    val hide: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("kid")
    val kid: Kid,
    @SerializedName("kidId")
    val kidId: Int,
    @SerializedName("reward")
    val reward: Reward? = null,
    @SerializedName("rewardId")
    val rewardId: Int
)

data class Carer(
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("activatedAt")
    val activatedAt: Any,
    @SerializedName("canCreate")
    val canCreate: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("iconId")
    val iconId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isMain")
    val isMain: Boolean,
    @SerializedName("loginToken")
    val loginToken: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pin")
    val pin: String,
    @SerializedName("settingLocale")
    val settingLocale: String,
    @SerializedName("settingMusic")
    val settingMusic: Int,
    @SerializedName("settingNotifications")
    val settingNotifications: Int,
    @SerializedName("settingSoundfx")
    val settingSoundfx: Int,
    @SerializedName("settingTimezone")
    val settingTimezone: Int,
    @SerializedName("setupComplete")
    val setupComplete: Int,
    @SerializedName("setupCompleteReminder")
    val setupCompleteReminder: Int,
    @SerializedName("status")
    val status: Int
)

data class Challenge(
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("challengeCatId")
    val challengeCatId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)

data class GlobalChallenge(
    @SerializedName("challengeCatId")
    val challengeCatId: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("maxAge")
    val maxAge: Int,
    @SerializedName("minAge")
    val minAge: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("titleFr")
    val titleFr: String
)

data class Kid(
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("age")
    val age: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deviceToken")
    val deviceToken: String,
    @SerializedName("iconId")
    val iconId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("loginToken")
    val loginToken: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pin")
    val pin: String,
    @SerializedName("settingLocale")
    val settingLocale: String,
    @SerializedName("settingMusic")
    val settingMusic: Int,
    @SerializedName("settingNotifications")
    val settingNotifications: Int,
    @SerializedName("settingSoundfx")
    val settingSoundfx: Int,
    @SerializedName("settingTimezone")
    val settingTimezone: Int,
    @SerializedName("status")
    val status: Int
)