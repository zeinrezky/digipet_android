package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class ChildAgeModel(
    @SerializedName("index")
    var index: Int,
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
)