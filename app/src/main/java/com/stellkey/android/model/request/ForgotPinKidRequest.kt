package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class ForgotPinKidRequest(
    @SerializedName("kidId")
    val kidId: Int
)