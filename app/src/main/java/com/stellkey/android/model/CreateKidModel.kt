package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class CreateKidModel(
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
    @SerializedName("pet")
    var pet: KidPet
) {
    data class KidPet(
        @SerializedName("id")
        var id: Int,
        @SerializedName("updatedAt")
        var updatedAt: String,
        @SerializedName("hunger")
        var hunger: Int,
        @SerializedName("happiness")
        var happiness: Int,
        @SerializedName("kidId")
        var kidId: Int,
        @SerializedName("pnSentAt")
        var pnSentAt: String
    )
}