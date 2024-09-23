package com.example.api_example

data class HttpQueryParam(
    val appId: String = "tech.crackle.financecalculator",
    val country: String = "in",
    val version: String = "0.0.100"
) {
    // Function to return headers as a Map<String, String>
    fun getParams(): Map<String, String> {
        val headers = mutableMapOf<String, String>()

        // Add default headers
        headers["appId"] = appId
        headers["country"] = country
        headers["version"] = version

        return headers
    }
}
