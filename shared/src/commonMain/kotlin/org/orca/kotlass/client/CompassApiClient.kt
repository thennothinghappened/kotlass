package org.orca.kotlass.client

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToString
import kotlinx.serialization.serializer
import org.orca.kotlass.data.common.CalendarEvent
import org.orca.kotlass.data.common.CompassGetCalendarEventsByUser

class CompassApiClient(
    private val credentials: CompassUserCredentials
) {

    private companion object {

    }

    private val client = HttpClient {
        defaultRequest {

            url {
                host = credentials.domain
                protocol = URLProtocol.HTTPS
            }

            header(HttpHeaders.Cookie, credentials.cookie)
            contentType(ContentType.Application.Json)
        }

        expectSuccess = true

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private fun handleError(e: Throwable): CompassApiError {
        throw e // TODO
    }

    suspend fun getCalendarEvents(
        startDate: LocalDate,
        endDate: LocalDate = startDate,
        activityId: Int? = null
    ): CompassApiResult<List<CalendarEvent>> = try {

        val body = CompassGetCalendarEventsByUser(
            userId = credentials.userId,
            startDate = startDate,
            endDate = endDate,
            activityId = activityId
        )

        val res = client.post {
            url(path = "/Services/Calendar.svc/GetCalendarEventsByUser")
            setBody(body)
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<List<CalendarEvent>>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }
}