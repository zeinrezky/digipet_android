package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class AllKidsModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("age")
    var age: Int,
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("loginToken")
    var loginToken: String,
    @SerializedName("deviceToken")
    var deviceToken: String,
    @SerializedName("iconId")
    var iconId: Int,
    @SerializedName("settingLocale")
    var settingLocale: String,
    @SerializedName("settingNotifications")
    var settingNotifications: Int,
    @SerializedName("settingSoundfx")
    var settingSoundfx: Int,
    @SerializedName("settingMusic")
    var settingMusic: Int,
    @SerializedName("settingTimezone")
    var settingTimezone: Int,
    @SerializedName("profileIcon")
    var profileIcon: ProfileIconModel.ProfileIconModelData,
    @SerializedName("pet")
    var pet: Any,
    @SerializedName("activeAssignments")
    var activeAssignments: Assignments,
    @SerializedName("activeTasks")
    var activeTasks: Assignments,
    @SerializedName("tasksToday")
    var tasksToday: Assignments,
    @SerializedName("tasksCompleted")
    var tasksCompleted: Int,
    @SerializedName("starsSpent")
    var starsSpent: Int,
    @SerializedName("rubiesSpent")
    var rubiesSpent: Int,
    @SerializedName("level")
    var level: KidLevelModel,
    @SerializedName("badges")
    var badges: ArrayList<BadgesModel>,
    @SerializedName("redemptions")
    var redemptions: ArrayList<Any>,
    @SerializedName("activeDecor")
    var activeDecor: ArrayList<Any>,
    @SerializedName("activeAccessory")
    var activeAccessory: ArrayList<Any>,
    @SerializedName("RewardAvailable")
    var RewardAvailable: ArrayList<RewardAvailable> = arrayListOf(),
    var uiAction: UIAction = UIAction(isSelected = false, isEnable = true)
) {

    data class Assignments(
        @SerializedName("notificationsTotal")
        var notificationsTotal: Int,
        @SerializedName("redemptionsPendingTotal")
        var redemptionsPendingTotal: Int,
        @SerializedName("dateRange")
        var dateRange: String,
        @SerializedName("assignments")
        var assignments: ArrayList<AssignmentsModel>,
        @SerializedName("suggested")
        var suggested: Any
    )

    data class UIAction(
        var isSelected: Boolean = false,
        var isEnable: Boolean = true
    )
}