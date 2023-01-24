package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class KidLevelModel(
    @SerializedName("level")
    var level: Int,
    @SerializedName("percentageToNextLevel")
    var percentageToNextLevel: Int,
    @SerializedName("levelName")
    var levelName: String,
    @SerializedName("levelNameFr")
    var levelNameFr: String,
    @SerializedName("bgColor")
    var bgColor: String,
    @SerializedName("starsTotal")
    var starsTotal: Int = 0
)