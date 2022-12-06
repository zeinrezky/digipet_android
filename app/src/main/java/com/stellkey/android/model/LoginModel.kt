package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("firstname")
    var firstname: String,
    @SerializedName("lastname")
    var lastname: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("loginToken")
    var loginToken: String,
    @SerializedName("carers")
    var carers: ArrayList<LoginCarersModel>,
    @SerializedName("kids")
    var kids: ArrayList<LoginKidsModel>,
    @SerializedName("token")
    var token: String,
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("age")
    var age: Int,
    @SerializedName("pet")
    var pet: Any
) {
    data class LoginCarersModel(
        @SerializedName("name")
        var name: String,
        @SerializedName("profileIcon")
        var profileIcon: ProfileIconModel.ProfileIconModelData,
        @SerializedName("isMain")
        var isMain: Boolean,
        @SerializedName("id")
        var id: Int,
        @SerializedName("loginToken")
        var loginToken: String
    )

    data class LoginKidsModel(
        @SerializedName("name")
        var name: String,
        @SerializedName("profileIcon")
        var profileIcon: ProfileIconModel.ProfileIconModelData,
        @SerializedName("loginToken")
        var loginToken: String
    )
}