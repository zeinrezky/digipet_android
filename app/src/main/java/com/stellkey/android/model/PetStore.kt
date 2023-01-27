package com.stellkey.android.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class AllPetStore(
    @SerializedName("items")
    val items: List<PetStore>
)

@Parcelize
data class PetStore(
    @SerializedName("active")
    val active: Int = -1,
    @SerializedName("category")
    val category: String = "",
    @SerializedName("categoryFr")
    val categoryFr: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("owned")
    val owned: Boolean = false,
    @SerializedName("ruby_cost")
    val ruby_cost: Int = -1,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("titleFr")
    val titleFr: String = ""
) : Parcelable