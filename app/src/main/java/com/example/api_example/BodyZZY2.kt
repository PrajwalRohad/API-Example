package com.example.api_example

import org.json.JSONObject

data class BodyZZY2(
    val a: String,
    val b: String,
    val c: String,
    val d: String
) {
    fun getBody() : JSONObject {
        val jsonObject = JSONObject().apply {
            put("errorMsg", a)
            put("errorCode", b)
            put("adId", c)
            put("version", d)
        }

        return jsonObject
    }
}