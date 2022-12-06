package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class EditPasswordRequest (
    @SerializedName("oldPassword")
    var oldPassword: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("confirmPassword")
    var confirmPassword: String
)