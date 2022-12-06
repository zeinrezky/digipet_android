package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class DeleteLogRequest (
    @SerializedName("days")
    var days: Int
)