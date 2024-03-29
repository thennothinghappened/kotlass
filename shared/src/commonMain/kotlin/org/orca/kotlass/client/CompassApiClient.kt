package org.orca.kotlass.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.orca.kotlass.client.requests.*
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
import org.orca.kotlass.data.learningtask.CompassGetLearningTasksForUserId
import org.orca.kotlass.data.learningtask.LearningTask
import org.orca.kotlass.data.user.CompassGetStaffRequest
import org.orca.kotlass.data.user.CompassGetUserDetailsRequest
import org.orca.kotlass.data.user.User
import org.orca.kotlass.data.user.UserDetails

/**
 * Client for talking to the Compass API!
 */
class CompassApiClient(private var credentials: CompassUserCredentials) :
    ICalendarEventsClient,
    IActivitiesClient,
    IGradingSchemesClient,
    IAcademicGroupsClient,
    ILearningTasksClient,
    IUsersClient,
    IAuthClient {

    companion object {
        private fun createHttpClient(credentials: CompassUserCredentials): HttpClient {
            return HttpClient {
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
        }
    }

    private var client = createHttpClient(credentials)

    override fun setCredentials(credentials: CompassUserCredentials) {
        this.credentials = credentials
        client.cancel("Reloading HTTP client: credentials changed!")

        client = createHttpClient(credentials)
    }

    /**
     * Handle errors to give a more descriptive container for callers
     * to know what went wrong.
     *
     * TODO: unfortunately, errors differ by platform, so we don't currently give useful info for connection errors.
     */
    private fun handleError(error: Throwable): CompassApiError =
        when (error) {
            is JsonConvertException -> CompassApiError.ParseError(error)
            is ServerResponseException -> CompassApiError.CompassError
            else -> CompassApiError.ClientError(error)
        }

    override suspend fun getCalendarEvents(
        startDate: LocalDate,
        endDate: LocalDate,
        activityId: Int?
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

    override suspend fun getActivity(instanceId: String): CompassApiResult<Activity> = try {

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

    override suspend fun getActivity(activityId: Int): CompassApiResult<Activity> = try {

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

    override suspend fun getActivity(instance: ActivityInstance): CompassApiResult<Activity> {
        return getActivity(instance.activityId)
    }

    override suspend fun getActivityInstance(instanceId: String): CompassApiResult<ActivityInstance> = try {

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

    override suspend fun getActivityInstance(calendarEvent: CalendarEvent.Instanced): CompassApiResult<ActivityInstance> = try {

        val body = CompassGetActivityByInstanceId(calendarEvent.instanceId)

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

    override suspend fun getGradingSchemesForLearningTasks(): CompassApiResult<List<GradingScheme>> = try {

        val res = client.get {
            url(path = "/Services/ReferenceDataCache.svc/GetGradingSchemesForLearningTasks")
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<List<GradingScheme>>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

    override suspend fun getAcademicGroups(): CompassApiResult<List<AcademicGroup>> = try {

        val res = client.get {
            url(path = "/Services/ReferenceDataCache.svc/GetAllAcademicGroups")
        }

        CompassApiResult.Success(
            res.body<ResponseWrapper<List<AcademicGroup>>>().data
        )

    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

    override suspend fun getLearningTasksForActivity(
        activityId: Int,
        limit: Int
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

    override suspend fun getLearningTasksForUserId(
        userId: Int,
        limit: Int
    ): CompassApiResult<List<LearningTask>> = try {

        val body = CompassGetLearningTasksForUserId(
            userId = userId,
            limit = limit
        )

        val res = client.post {
            url(path = "/Services/LearningTasks.svc/GetAllLearningTasksByUserId")
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

    override suspend fun getLearningTasks(limit: Int): CompassApiResult<List<LearningTask>> {
        return getLearningTasksForUserId(credentials.userId, limit)
    }

    override suspend fun getUserDetails(userId: Int): CompassApiResult<UserDetails> = try {

        val body = CompassGetUserDetailsRequest(userId)

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

    override suspend fun getMyUserDetails(): CompassApiResult<UserDetails> {
        return getUserDetails(credentials.userId)
    }

    override suspend fun getAllStaff(limit: Int): CompassApiResult<List<User>> = try {

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

    override suspend fun checkAuthentication(): CompassApiResult<Unit?> = try {
        client.post {
            url(path = "/Services/Mobile.svc/TestAuth")
        }

        CompassApiResult.Success(null)
    } catch (e: Throwable) {
        CompassApiResult.Failure(handleError(e))
    }

}