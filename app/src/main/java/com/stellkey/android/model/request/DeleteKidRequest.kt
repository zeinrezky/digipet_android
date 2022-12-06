package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class DeleteKidRequest (
    @SerializedName("id")
    var id: Int
)