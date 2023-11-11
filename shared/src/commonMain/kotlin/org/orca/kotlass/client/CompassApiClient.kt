package org.orca.kotlass.client

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class CompassApiClient(
    private val credentials: CompassUserCredentials
) {

    companion object {

    }

    private val client = HttpClient {
        defaultRequest {
            host = credentials.domain
            url { protocol = URLProtocol.HTTPS }
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


}