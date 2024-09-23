package com.example.api_example

import com.google.gson.annotations.SerializedName

data class ZZY2(
    @SerializedName("errorMsg")
    val a: String,
    @SerializedName("errorCode")
    val b: String,
    @SerializedName("adId")
    val c: String,
    @SerializedName("version")
    val d: String
)