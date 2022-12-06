package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class LocaleModel(
    @SerializedName("locale")
    var locale: String
)