package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class ForgotPinCarerRequest(
    @SerializedName("carerId")
    val carerId: Int
)