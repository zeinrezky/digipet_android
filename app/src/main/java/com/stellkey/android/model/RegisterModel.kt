package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class RegisterModel (
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("email")
    var email: String,
)