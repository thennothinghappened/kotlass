package org.orca.kotlass.client

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
import org.orca.kotlass.data.academicgroup.AcademicGroup
import org.orca.kotlass.data.activity.Activity
import org.orca.kotlass.data.activity.ActivityInstance
import org.orca.kotlass.data.calendar.CalendarEvent
import org.orca.kotlass.data.activity.CompassGetActivityById
import org.orca.kotlass.data.activity.CompassGetActivityByInstanceId
import org.orca.kotlass.data.common.CompassApiListContainer
import org.orca.kotlass.data.calendar.CompassGetCalendarEventsByUser
import org.orca.kotlass.data.grading.GradingScheme
import org.orca.kotlass.data.learningtask.CompassGetLearningTasksForActivityId
import org.orca.kotlass.data.learningtask.LearningTask
import org.orca.kotlass.data.user.CompassGetStaffRequest
import org.orca.kotlass.data.user.CompassGetUserDetailsRequest
import org.orca.kotlass.data.user.User
import org.orca.kotlass.data.user.UserDetails

class CompassApiClient(
    private val credentials: CompassUserCredentials
) {

    private companion object {
        const val DEFAULT_LIMIT = 2000
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

    /**
     * Get an [ActivityInstance] by its [instanceId].
     */
    suspend fun getLearningTasksForActivity(
        activityId: Int,
        limit: Int = DEFAULT_LIMIT
    ): CompassApiResult<List<LearningTask>> = try {

        val body = CompassGetLearningTasksForActivityId(
            activityId = activityId,
            limit = limit
        )

        val res = client.post {
            url(path = "/Services/LearningTasks.svc/GetAllLearningTasksByActivityId")
            setBody(body)
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<CompassApiListContainer<LearningTask>>>()
                .data
                .data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

    /**
     * Get a given user's [UserDetails], or ourself by default.
     *
     * *As a student*, this will return no data for any user other than their
     * own ID.
     */
    suspend fun getUserDetails(id: Int = credentials.userId): CompassApiResult<UserDetails> = try {

        val body = CompassGetUserDetailsRequest(id)

        val res = client.post {
            url(path = "/Services/User.svc/GetUserDetailsBlobByUserId")
            setBody(body)
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<UserDetails>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

    /**
     * Get the list of all staff members [User].
     */
    suspend fun getAllStaff(limit: Int = DEFAULT_LIMIT): CompassApiResult<List<User>> = try {

        val body = CompassGetStaffRequest(limit)

        val res = client.post {
            url(path = "/Services/User.svc/GetAllStaff")
            setBody(body)
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<List<User>>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

}