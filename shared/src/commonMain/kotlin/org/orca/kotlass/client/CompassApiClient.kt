package org.orca.kotlass.client

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class CompassApiClient(
    domain: String
) {

    companion object {

    }

    private val client = HttpClient {
        defaultRequest {
            host = domain
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