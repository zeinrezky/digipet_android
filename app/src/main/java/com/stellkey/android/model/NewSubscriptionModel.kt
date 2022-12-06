package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class NewSubscriptionModel(
    @SerializedName("expiresAt")
    var expiresAt: String,
    @SerializedName("startAt")
    var startAt: String,
    @SerializedName("subscriptionId")
    var subscriptionId: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("subscriptionDuration")
    var subscriptionDuration: String,
    @SerializedName("subscriptionDurationType")
    var subscriptionDurationType: String,
    @SerializedName("subscriptionCost")
    var subscriptionCost: String,
    @SerializedName("subscriptionBillDesc")
    var subscriptionBillDesc: String,
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
)