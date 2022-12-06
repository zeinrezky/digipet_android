package com.stellkey.android.model.request

import com.google.gson.annotations.SerializedName

data class SubscriptionRequest (
    @SerializedName("expiresAt")
    var expiresAt: String,
    @SerializedName("startAt")
    var startAt: String,
    @SerializedName("subscriptionId")
    var subscriptionId: String,
    @SerializedName("status")
    var status: Int
)