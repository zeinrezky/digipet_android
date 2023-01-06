package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class CustomTaskModel(
    @SerializedName("accountId")
    val accountId: Int,
    @SerializedName("challengeCatId")
    val challengeCatId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)