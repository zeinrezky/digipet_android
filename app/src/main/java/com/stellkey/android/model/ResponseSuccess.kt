package com.stellkey.android.model

import com.google.gson.annotations.SerializedName

data class ResponseSuccess<T>(
    @SerializedName("data")
    val data: T,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("statusCode")
    val status: StatusCode
) {

    data class Pagination(
        @SerializedName("total")
        val total: Int,
        @SerializedName("nav")
        val nav: Any
    )

    data class StatusCode(
        @SerializedName("code")
        val code: Int,
        @SerializedName("msg")
        val message: String,
        @SerializedName("debug")
        val debug: String
    )

    data class SuccessDelete(
        @SerializedName("count")
        val count: Int
    )
}