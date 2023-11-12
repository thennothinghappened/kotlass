package org.orca.kotlass.client

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.orca.kotlass.data.common.AcademicGroup
import org.orca.kotlass.data.common.Activity
import org.orca.kotlass.data.common.ActivityInstance
import org.orca.kotlass.data.common.CalendarEvent
import org.orca.kotlass.data.common.CompassGetActivityById
import org.orca.kotlass.data.common.CompassGetActivityByInstanceId
import org.orca.kotlass.data.common.CompassGetCalendarEventsByUser
import org.orca.kotlass.data.common.GradingScheme

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

    private fun handleError(error: Throwable): CompassApiError =
        when (error) {
            is JsonConvertException -> CompassApiError.ParseError(error)
            else -> CompassApiError.ClientError(error)
        }

    /**
     * Get the [CalendarEvent]s for our [credentials]' `userId`, optionally
     * for a given [activityId], otherwise for all activities from [startDate] to [endDate].
     */
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

    /**
     * Get a given [Activity] by an [instanceId] belonging to it.
     */
    suspend fun getActivity(instanceId: String): CompassApiResult<Activity> = try {

        val body = CompassGetActivityByInstanceId(instanceId)

        val res = client.post {
            url(path = "/Services/Activity.svc/GetLessonsByInstanceId")
            setBody(body)
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<Activity>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

    /**
     * Get an [Activity] by its [activityId].
     */
    suspend fun getActivity(activityId: Int): CompassApiResult<Activity> = try {

        val body = CompassGetActivityById(activityId)

        val res = client.post {
            url(path = "/Services/Activity.svc/GetLessonsByActivityId")
            setBody(body)
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<Activity>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

    /**
     * Get an [ActivityInstance] by its [instanceId].
     */
    suspend fun getActivityInstance(instanceId: String): CompassApiResult<ActivityInstance> = try {

        val body = CompassGetActivityByInstanceId(instanceId)

        val res = client.post {
            url(path = "/Services/Activity.svc/GetLessonsByInstanceIdQuick")
            setBody(body)
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<ActivityInstance>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

    /**
     * Get applicable [GradingScheme] list used for Learning Tasks.
     */
    suspend fun getGradingSchemesForLearningTasks(): CompassApiResult<List<GradingScheme>> = try {

        val res = client.get {
            url(path = "/Services/ReferenceDataCache.svc/GetGradingSchemesForLearningTasks")
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<List<GradingScheme>>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

    /**
     * Get list of [AcademicGroup]s.
     */
    suspend fun getAcademicGroups(): CompassApiResult<List<AcademicGroup>> = try {

        val res = client.get {
            url(path = "/Services/ReferenceDataCache.svc/GetAllAcademicGroups")
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<List<AcademicGroup>>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

}