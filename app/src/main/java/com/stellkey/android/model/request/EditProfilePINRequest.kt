package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class EditProfilePINRequest (
    @SerializedName("pin")
    var pin: String
)