package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class SubscriptionModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("accountId")
    var accountId: Int,
    @SerializedName("expiresAt")
    var expiresAt: String,
    @SerializedName("startAt")
    var startAt: String,
    @SerializedName("subscriptionId")
    var subscriptionId: String,
    @SerializedName("status")
    var status: Int
)