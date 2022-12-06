package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class AllProfileModel (
    @SerializedName("dbResult")
    var dbResult: AllProfileResponse
){
    data class AllProfileResponse(
        @SerializedName("id")
        var id: Int,
        @SerializedName("email")
        var email: String,
        @SerializedName("family")
        var family: ArrayList<Family>,
    )
    data class Family(
        @SerializedName("id")
        var id: Int,
        @SerializedName("name")
        var name: String,
        @SerializedName("profileIcon")
        var profileIcon: ProfileIconModel.ProfileIconModelData,
        @SerializedName("isMain")
        var isMain: Boolean,
        @SerializedName("loginToken")
        var loginToken: String,
        @SerializedName("pin")
        var pin: String,
        @SerializedName("email")
        var email: String,
        @SerializedName("role")
        var role: String,
        @SerializedName("age")
        var age: Int,
        @SerializedName("levelInfo")
        var levelInfo: KidLevelModel
    )
}