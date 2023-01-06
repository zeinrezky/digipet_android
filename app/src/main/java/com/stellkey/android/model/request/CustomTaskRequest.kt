package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class CustomTaskRequest(
    @SerializedName("challengeCatId")
    val challengeCatId: Int,
    @SerializedName("title")
    val title: String
)