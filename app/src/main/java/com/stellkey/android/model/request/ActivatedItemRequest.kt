package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName


/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version ActivatedItemRequest, v 0.1 27/01/23 21.26 by Nicolas Manurung
 */
data class ActivatedItemRequest(
    @SerializedName("itemId")
    val itemId: Int
)