package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version EditCustomChallenge, v 0.1 10/01/23 10.19 by Nicolas Manurung
 */
data class EditCustomChallenge(
    @SerializedName("challengeCatId")
    var challengeCatId: Int = -1,
    @SerializedName("id")
    var id: Int = -1,
    @SerializedName("title")
    var title: String = ""
)