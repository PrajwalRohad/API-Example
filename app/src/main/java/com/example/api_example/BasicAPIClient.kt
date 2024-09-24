package com.example.api_example

import android.os.AsyncTask
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject

class BasicAPIClient private constructor(private val baseUrl: String) {

    companion object {
        @Volatile
        private var instance: BasicAPIClient? = null

        // Singleton access method
        fun getInstance(baseUrl: String): BasicAPIClient {
            return instance ?: synchronized(this) {
                instance ?: BasicAPIClient(baseUrl).also { instance = it }
            }
        }

        // Method to destroy the instance (release the singleton)
        fun destroyInstance() {
            instance = null
        }
    }

    // GET Request with headers and query parameters
    suspend fun get(endpoint: String, headers: Map<String, String>? = null, queryParams: Map<String, String>? = null): String? {
        return request("GET", endpoint, headers, null, queryParams)
    }

    // POST Request with headers, body, and query parameters
    suspend fun post(endpoint: String, body: JSONObject?, headers: Map<String, String>? = null, queryParams: Map<String, String>? = null): String? {
        return request("POST", endpoint, headers, body, queryParams)
    }

    // PUT Request with headers, body, and query parameters
    suspend fun put(endpoint: String, body: JSONObject?, headers: Map<String, String>? = null, queryParams: Map<String, String>? = null): String? {
        return request("PUT", endpoint, headers, body, queryParams)
    }

    // DELETE Request with headers and query parameters
    suspend fun delete(endpoint: String, headers: Map<String, String>? = null, queryParams: Map<String, String>? = null): String? {
        return request("DELETE", endpoint, headers, null, queryParams)
    }

    // Core request method with headers, body, and query parameters
    private suspend fun request(
        method: String,
        endpoint: String,
        headers: Map<String, String>? = null,
        body: JSONObject? = null,
        queryParams: Map<String, String>? = null
    ): String? {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                // Construct the full URL, including query parameters
                val urlWithParams = URL(buildUrlWithQueryParams(baseUrl + endpoint, queryParams))
                Log.d("API_TAG", "URL : $urlWithParams")

                connection = urlWithParams.openConnection() as HttpURLConnection
                Log.d("API_TAG", "Connection Opened")

                // Set HTTP method (GET, POST, etc.)
                connection.requestMethod = method
                Log.d("API_TAG", "requested method: $method")

                //Set Timeout
                connection.connectTimeout = 10000 //10 sec until connection
                connection.readTimeout = 10000  //10 sec until response

                // Set default headers (Content-Type, Accept, etc.)
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")
                Log.d("API_TAG", "request set")

                // Add custom headers if provided
                headers?.forEach { (key, value) ->
                    connection.setRequestProperty(key, value)
                }
                Log.d("API_TAG", "connection headers set")

                // If it's a POST or PUT request, write the body data
                if (method == "POST" || method == "PUT") {
                    connection.doOutput = true
                    val outputStream = BufferedOutputStream(connection.outputStream)
                    val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                    writer.write(body.toString())
                    writer.flush()
                    writer.close()
                    outputStream.close()
                    Log.d("API_TAG", "write success")
                }

                // Connect to the server
                Log.d("API_TAG", "Connecting...")
                connection.connect()
                Log.d("API_TAG", "Connected..!")

                // Get the response
                val responseCode = connection.responseCode
                Log.d("API_TAG", "Response Code: $responseCode")
                return@withContext if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    // Read the response if it's 200 (OK) or 201 (Created)
                    Log.d("API_TAG", "Response OK")
                    val inputStream = BufferedInputStream(connection.inputStream)
                    inputStream.bufferedReader().use { it.readText() }


                } else {
                    Log.d("API_TAG", "Response null")
                    null
                }

            } catch (e: Exception) {
                Log.d("API_TAG", "Exception: $e")
                e.printStackTrace()
                return@withContext null
            } finally {
                Log.d("API_TAG", "Connection disconnected.")
                connection?.disconnect()
            }
        }


    }

    // Helper function to build the URL with query parameters
    private fun buildUrlWithQueryParams(url: String, queryParams: Map<String, String>?): String {
        if (queryParams == null || queryParams.isEmpty()) {
            return url
        }

        val queryString = queryParams.map { (key, value) ->
            "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
        }.joinToString("&")

        Log.d("API_TAG", "query string : $queryString")

        return "$url?$queryString"
    }
}
