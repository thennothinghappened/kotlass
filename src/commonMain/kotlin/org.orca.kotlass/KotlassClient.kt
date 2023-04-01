package org.orca.kotlass

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orca.kotlass.data.*

open class KotlassClient(
    private val credentials: CompassClientCredentials,
    private val proxyIp: String? = null
) : IKotlassClient {

    interface CompassClientCredentials {
        val domain: String
        val userId: Int
        val cookie: String
    }

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }

        install(HttpCache)

        // In combination with Timeout, since Compass has very inconsistent response times ranging from
        // a few hundred millis to <1m, we drop all our connections which take an unreasonable time and
        // try again, to avoid them getting lost in the Compass networking abyss.
        install(HttpRequestRetry) {
            maxRetries = 3
            retryOnException(retryOnTimeout = true)
            exponentialDelay()
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }

        proxyIp?.let {
            engine {
                proxy = ProxyBuilder.http(proxyIp)
            }
        }
    }

    private object Services {
        const val referenceDataCache = "ReferenceDataCache"
        const val newsFeed = "NewsFeed"
        const val calendar = "Calendar"
        const val activity = "Activity"
        const val learningTasks = "LearningTasks"
        const val fileAssets = "FileAssets"
        const val taskService = "TaskService"
        const val subjects = "Subjects"
        const val wiki = "Wiki"
    }

    override fun buildDomainUrlString(endpoint: String) =
        "https://${credentials.domain}$endpoint"

    override fun buildDomainFileDownloadString(fileAssetId: String, originalFileName: String) =
        buildApiRequestUrl(Services.fileAssets, "DownloadFile?id=$fileAssetId&originalFileName=$originalFileName")

    private fun buildApiRequestUrl(endpoint: String, location: String) =
        buildDomainUrlString("/Services/${endpoint}.svc/${location}")
    private suspend inline fun <reified T> makeApiGetRequestPlain(endpoint: String, location: String): NetResponse<T> {
        val reply: HttpResponse

        try {
            reply = client.get(buildApiRequestUrl(endpoint, location)) {
                headers {
                    append(HttpHeaders.Cookie, credentials.cookie)
                }
            }
            if (reply.status != HttpStatusCode.OK)
                return NetResponse.RequestFailure(reply.status)

        } catch (e: Throwable) {
            return NetResponse.ClientError(e)
        }

        return NetResponse.Success(reply.body())
    }

    private suspend inline fun <reified T, reified B> makeApiPostRequestPlain(endpoint: String, location: String, body: B): NetResponse<T> {
        val reply: HttpResponse

        try {
            reply = client.post(buildApiRequestUrl(endpoint, location)) {
                headers {
                    append(HttpHeaders.Cookie, credentials.cookie)
                }
                contentType(ContentType.Application.Json)
                setBody(json.encodeToString(body))
            }

            if (reply.status != HttpStatusCode.OK)
                return NetResponse.RequestFailure(reply.status)

        } catch (e: Throwable) {
            return NetResponse.ClientError(e)
        }

        return NetResponse.Success(reply.body())
    }

    private suspend inline fun <reified T> makeApiGetRequest(endpoint: String, location: String): NetResponse<T> {
        val reply: CData<T>

        try {
            val _reply = client.get(buildApiRequestUrl(endpoint, location)) {
                headers {
                    append(HttpHeaders.Cookie, credentials.cookie)
                }
            }

            if (_reply.status != HttpStatusCode.OK)
                return NetResponse.RequestFailure(_reply.status)

            reply = _reply.body()
        } catch (e: Throwable) {
            return NetResponse.ClientError(e)
        }

        return NetResponse.Success(reply.data!!)
    }

    private suspend inline fun <reified T, reified B> makeApiPostRequest(endpoint: String, location: String, body: B): NetResponse<T> {
        val reply: CData<T>

        try {
            val _reply = client.post(buildApiRequestUrl(endpoint, location)) {
                headers {
                    append(HttpHeaders.Cookie, credentials.cookie)
                }
                contentType(ContentType.Application.Json)
                setBody(json.encodeToString(body))
            }

            if (_reply.status != HttpStatusCode.OK)
                return NetResponse.RequestFailure(_reply.status)

            reply = _reply.body()
        } catch (e: Throwable) {
            return NetResponse.ClientError(e)
        }

        return NetResponse.Success(reply.data!!)
    }

    override fun validateCredentials(): NetResponse<Unit?> = runBlocking {
        // We use getAllCampuses here since it'll generally have the smallest reply, and takes no input,
        // eliminating API changes as a possible issue if we fail.
        return@runBlocking when(val reply = getAllCampuses()) {
            is NetResponse.Success -> NetResponse.Success(null)
            is NetResponse.ClientError -> NetResponse.ClientError(reply.error)
            is NetResponse.RequestFailure -> NetResponse.RequestFailure(reply.httpStatusCode)
        }
    }

    override suspend fun saveTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Int> =
        makeApiPostRequest(Services.taskService, "SaveTaskItem", TaskItemRequest(taskItemRequestBody))

    override suspend fun updateTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Unit?> =
        makeApiPostRequestPlain(
            Services.taskService,
            "UpdateTaskItem",
            TaskItemRequest(taskItemRequestBody)
        )

    override suspend fun deleteTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Unit?> =
        makeApiPostRequestPlain(
            Services.taskService,
            "DeleteTaskItem",
            TaskItemRequest(taskItemRequestBody)
        )

    override suspend fun getTaskItems(baseApiRequest: BaseApiRequest): NetResponse<List<TaskItem>> =
        makeApiPostRequest(
            Services.taskService,
            "GetTaskItems",
            baseApiRequest
        )

    override suspend fun getStandardClassesOfUserInAcademicGroup(standardClassesOfUserRequest: StandardClassesOfUserRequest): NetResponse<DataExtGridDataContainer<StandardClass>> =
        makeApiPostRequest(
            Services.subjects,
            "GetStandardClassesOfUserInAcademicGroup",
            standardClassesOfUserRequest
        )

    override suspend fun downloadFile(assetId: String): NetResponse<String> =
        makeApiGetRequestPlain(Services.fileAssets, "DownloadFile?id=$assetId")

    override suspend fun getLessonsByInstanceId(instanceId: String): NetResponse<ActivitySummary> =
        makeApiPostRequest(Services.activity, "GetLessonsByInstanceId", ActivitySummaryByInstanceIdRequest(instanceId))

    override suspend fun getLessonsByInstanceIdQuick(instanceId: String): NetResponse<Activity> =
        makeApiPostRequest(Services.activity, "GetLessonsByInstanceIdQuick", ActivitySummaryByInstanceIdRequest(instanceId))

    override suspend fun getLessonsByActivityId(activityId: Int): NetResponse<ActivitySummary> =
        makeApiPostRequest(Services.activity, "GetLessonsByActivityId", ActivitySummaryByActivityIdRequest(activityId.toString()))

    override suspend fun getLessonsByActivityIdQuick(activityId: Int): NetResponse<Activity> =
        makeApiPostRequest(Services.activity, "GetLessonsByActivityIdQuick", ActivitySummaryByActivityIdRequest(activityId.toString()))

    override suspend fun getAllTaskCategories(baseApiRequest: BaseApiRequest): NetResponse<List<TaskCategory>> =
        makeApiPostRequest(Services.learningTasks, "GetAllTaskCategories", baseApiRequest)

    override suspend fun getAllLearningTasksByActivityId(activityId: Int): NetResponse<DataExtGridDataContainer<LearningTask>> =
        makeApiPostRequest(Services.learningTasks, "GetAllLearningTasksByActivityId", LearningTasksByActivityIdRequest(activityId = activityId.toString()))

    override suspend fun getAllLearningTasksByUserId(academicGroup: AcademicGroup?): NetResponse<DataExtGridDataContainer<LearningTask>> =
        makeApiPostRequest(Services.learningTasks, "GetAllLearningTasksByUserId", LearningTasksByUserIdRequest(userId = credentials.userId, academicGroupId = academicGroup?.id))

    override suspend fun getMyNewsFeedPaged(): NetResponse<DataExtGridDataContainer<NewsItem>> =
        makeApiPostRequest(Services.newsFeed, "GetMyNewsFeedPaged", NewsFeedRequest())

    override suspend fun getCalendarsByUser(): NetResponse<List<CalendarLayer>> =
        makeApiPostRequest(Services.calendar, "GetCalendarsByUser", CalendarLayersRequest())

    override suspend fun getCalendarEventsByUser(
        startDate: LocalDate,
        endDate: LocalDate
    ): NetResponse<List<CalendarEvent>> =
        makeApiPostRequest(Services.calendar, "GetCalendarEventsByUser", CalendarEventsRequest(
            startDate = startDate,
            endDate = endDate,
            userId = credentials.userId
        ))

    override suspend fun getMyAlerts(): NetResponse<List<Alert>> =
        makeApiPostRequest(Services.newsFeed, "GetMyAlerts", "")

    override suspend fun getAllLocations(): NetResponse<List<Location>> =
        makeApiGetRequest(Services.referenceDataCache, "GetAllLocations")

    override suspend fun getAllCampuses(): NetResponse<List<Campus>> =
        makeApiGetRequest(Services.referenceDataCache, "GetAllCampuses")

    override suspend fun getAllAcademicGroups(): NetResponse<List<AcademicGroup>> =
        makeApiGetRequest(Services.referenceDataCache, "GetAllAcademicGroups")

    override suspend fun getHeaderImageUrlByActivityId(activityId: Int): NetResponse<String> =
        makeApiPostRequest(Services.activity, "GetHeaderImageUrlByActivityId", ActivitySummaryByActivityIdRequest(
            activityId.toString()
        ))
    override suspend fun getActivityAndSubjectResourcesNode(activityId: Int): NetResponse<ResourceNode> =
        makeApiPostRequest(Services.wiki, "GetActivityAndSubjectResourcesNode", ActivityResourcesRequest(
            activityId.toString()
        ))

    override suspend fun getLessonPlanString(activityLessonPlan: ActivityLessonPlan): NetResponse<String> {
        if (activityLessonPlan.fileAssetId == null) return NetResponse.ClientError(Throwable("The file asset does not exist"))
        return downloadFile(activityLessonPlan.fileAssetId)
    }
}