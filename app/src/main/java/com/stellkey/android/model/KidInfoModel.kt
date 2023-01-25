package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class KidInfoModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("loginToken")
    var loginToken: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("age")
    var age: Int,
    @SerializedName("deviceToken")
    var deviceToken: String,
    @SerializedName("iconId")
    var iconId: Int,
    @SerializedName("settingLocale")
    var settingLocale: String,
    @SerializedName("settingMusic")
    var settingMusic: Int,
    @SerializedName("settingNotifications")
    var settingNotifications: Int,
    @SerializedName("settingSoundfx")
    var settingSoundfx: Int,
    @SerializedName("settingTimezone")
    var settingTimezone: Int,
    @SerializedName("profileIcon")
    var profileIcon: ProfileIconModel.ProfileIconModelData,
    @SerializedName("pet")
    var pet: PetModel,
    @SerializedName("activeAssignments")
    var activeAssignments: AllKidsModel.Assignments,
    @SerializedName("activeTasks")
    var activeTasks: AllKidsModel.Assignments,
    @SerializedName("tasksToday")
    var tasksToday: AllKidsModel.Assignments,
    @SerializedName("tasksCompleted")
    var tasksCompleted: Int?,
    @SerializedName("starsSpent")
    var starsSpent: Int = 0,
    @SerializedName("rubiesSpent")
    var rubiesSpent: Int?,
    @SerializedName("level")
    var level: KidLevelModel,
    @SerializedName("badges")
    var badges: ArrayList<BadgesModel>,
    @SerializedName("redemptions")
    var redemptions: ArrayList<Any>,
    @SerializedName("activeDecor")
    var activeDecor: ArrayList<Any>,
    @SerializedName("activeAccessory")
    var activeAccessory: ArrayList<Any>
)
data class PetModel(
    @SerializedName("happiness")
    val happiness: Int = 0,
    @SerializedName("hunger")
    val hunger: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("kidId")
    val kidId: Int,
    @SerializedName("pnSentAt")
    val pnSentAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)