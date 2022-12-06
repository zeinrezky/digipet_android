package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class ProfileIconModel (
    @SerializedName("data")
    var data: ArrayList<ProfileIconModelData>
) {
    data class ProfileIconModelData(
        @SerializedName("id")
        var id: Int,
        @SerializedName("icon")
        var icon: String,
        @SerializedName("type")
        var type: String
    )
}