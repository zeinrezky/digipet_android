package com.stellkey.android.model

import com.google.gson.annotations.SerializedName
import com.stellkey.android.helper.extension.emptyString

data class ConfirmAccountModel(
    @SerializedName("data")
    var data: ConfirmAccountModelData
) {
    data class ConfirmAccountModelData(
        @SerializedName("id")
        var id: Int,
        @SerializedName("createdAt")
        var createdAt: String,
        @SerializedName("updatedAt")
        var updatedAt: String,
        @SerializedName("status")
        var status: Int,
        @SerializedName("email")
        var email: String,
        @SerializedName("subsExpired")
        var subsExpired:  String? = emptyString,
        @SerializedName("subsId")
        var subsId:  String? = emptyString,
        @SerializedName("canonicalEmail")
        var canonicalEmail: String,
        @SerializedName("firstname")
        var firstname:  String? = emptyString,
        @SerializedName("lastname")
        var lastname:  String? = emptyString,
        @SerializedName("passwordReset")
        var passwordReset:  String? = emptyString,
        @SerializedName("resetToken")
        var resetToken:  String? = emptyString,
        @SerializedName("resetTokenExpires")
        var resetTokenExpires: String? = emptyString,
        @SerializedName("verificationToken")
        var verificationToken: String,
        @SerializedName("verificationTokenExpires")
        var verificationTokenExpires: String,
        @SerializedName("verified")
        var verified: Boolean,
        @SerializedName("loginToken")
        var loginToken: String,
        @SerializedName("trialEmail1Sent")
        var trialEmail1Sent: Int,
        @SerializedName("trialEmail2Sent")
        var trialEmail2Sent: Int,
        @SerializedName("subscriptionStatus")
        var subscriptionStatus: Int,
        @SerializedName("carers")
        var carers: ArrayList<CarersModel>,
        @SerializedName("carerToken")
        var carerToken: String
    )
}