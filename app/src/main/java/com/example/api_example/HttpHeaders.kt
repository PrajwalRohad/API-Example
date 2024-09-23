package com.example.api_example

data class HttpHeaders(
    val connection: String = "close",
    val requestId: String = "gdhfhfbdhdf"
) {

    companion object {
        val customHeaders: Map<String, String> = emptyMap()
    }

    // Function to return headers as a Map<String, String>
    fun getHeaders(): Map<String, String> {
        val headers = mutableMapOf<String, String>()

        // Add default headers
        headers["Connection"] = connection
        headers["requestId"] = requestId

        // Add any custom headers
        headers.putAll(customHeaders)

        return headers
    }
}
