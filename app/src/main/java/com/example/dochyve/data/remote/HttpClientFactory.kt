package com.example.dochyve.data.remote

import com.example.dochyve.data.Constants
import com.example.dochyve.data.datastore.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.gson.gson

object HttpClientFactory {
    fun create(tokenManager: TokenManager) : HttpClient{
        return HttpClient(Android) { // connect your app to the internet

            install(ContentNegotiation){ // automatically converts Json data into kotlin objects and vice versa
                gson()
            }

            install(HttpTimeout){ // this controls how long the app waits for the server
                socketTimeoutMillis = 30000
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
            }

            install(Logging){
                logger = Logger.ANDROID
                level = LogLevel.BODY
            }

            install(DefaultRequest){ // default setting for all API calls
                url{
                    takeFrom(Constants.BASE_URL)
                    // protocol = URLProtocol.HTTPS
                    // host = "api.dochyve.com"
                }

                contentType(ContentType.Application.Json)

                val token = tokenManager.getToken()
                if (!token.isNullOrEmpty()) {
                    header(
                        HttpHeaders.Authorization, "Bearer $token"
                    )
                }
            }
        }
    }
}