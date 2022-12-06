package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class ResendCodeModel (
    @SerializedName("resent_to")
    var resentTo: String
)