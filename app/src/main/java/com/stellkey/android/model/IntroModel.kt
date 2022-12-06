package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class IntroModel(
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("image_url")
    var imageUrl: Int,
    @SerializedName("index")
    var index: Int? = null
)