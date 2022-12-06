package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class ResendPINRequest (
    @SerializedName("account_id")
    var accountId: Int
)