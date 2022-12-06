package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class KidGlobalChallengeModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("titleFr")
    var titleFr: String,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("challengeCatId")
    var challengeCatId: Int,
    @SerializedName("minAge")
    var minAge: Int,
    @SerializedName("maxAge")
    var maxAge: Int,
    @SerializedName("challengeCat")
    var challengeCat: ChallengeCategory,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
){
    data class ChallengeCategory(
        @SerializedName("id")
        var id: Int,
        @SerializedName("title")
        var title: String,
        @SerializedName("titleFr")
        var titleFr: String,
        @SerializedName("icon")
        var icon: String
    )
}