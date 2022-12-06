package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class FamilyTypeModel (
    @SerializedName("type")
    var type: String,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)