package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class UpdateLocaleRequest (
    @SerializedName("locale")
    var locale: String
)