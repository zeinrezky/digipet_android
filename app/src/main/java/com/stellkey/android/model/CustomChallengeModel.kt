package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version CustomChallengeModel, v 0.1 08/01/23 23.12 by Nicolas Manurung
 */
data class CustomChallengeModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("challengeCatId")
    var challengeCatId: Int,
    @SerializedName("challengeCat")
    var challengeCat: ChallengeCategoryModel
)