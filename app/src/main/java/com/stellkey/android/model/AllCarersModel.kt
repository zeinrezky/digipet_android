package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class AllCarersModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("activatedAt")
    var activatedAt: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("isMain")
    var isMain: Boolean,
    @SerializedName("canCreate")
    var canCreate: Boolean,
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("loginToken")
    var loginToken: String,
    @SerializedName("deviceToken")
    var deviceToken: String,
    @SerializedName("iconId")
    var iconId: Int,
    @SerializedName("setupComplete")
    var setupComplete: Int,
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
    @SerializedName("setupCompleteReminder")
    var setupCompleteReminder: Int,
    @SerializedName("profileIcon")
    var profileIcon: ProfileIconModel.ProfileIconModelData,
)