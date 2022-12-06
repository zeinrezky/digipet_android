package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class RegisterEmailRequest (
    @SerializedName("email")
    var email: String
)