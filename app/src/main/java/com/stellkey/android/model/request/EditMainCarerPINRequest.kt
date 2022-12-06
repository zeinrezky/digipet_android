package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class EditMainCarerPINRequest (
    @SerializedName("oldPin")
    var oldPin: String,
    @SerializedName("pin")
    var pin: String,
    @SerializedName("confirmPin")
    var confirmPin: String
)