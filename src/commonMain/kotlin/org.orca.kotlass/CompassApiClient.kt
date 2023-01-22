package org.orca.kotlass

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orca.kotlass.data.*

class CompassApiClient(private val credentials: CompassClientCredentials) {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000 // it's a slow site
        }
        install(HttpCache) {
            // todo: set this up (different implementations for desktop and android)
        }
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.ALL
//        }
    }

    private val json = Json {
        encodeDefaults = true
    }

    companion object {
        private object Services {
            const val referenceDataCache = "ReferenceDataCache"
            const val newsFeed = "NewsFeed"
            const val calendar = "Calendar"
            const val activity = "Activity"
            const val learningTasks = "LearningTasks"
            const val fileAssets = "FileAssets"
            const val taskService = "TaskService"
        }
    }

    private suspend fun makeGetRequest(endpoint: String, location: String): HttpResponse {
        return client.get("https://${credentials.domain}/Services/${endpoint}.svc/${location}") {
            headers {
                append(HttpHeaders.Cookie, credentials.cookie)
            }
        }
    }

    private suspend fun makePostRequest(endpoint: String, location: String, body: String): HttpResponse {
        return client.post("https://${credentials.domain}/Services/${endpoint}.svc/${location}") {
            headers {
                append(HttpHeaders.Cookie, credentials.cookie)
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    /**
     * Create a new task item
     */
    suspend fun saveTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): CData<Int> =
        makePostRequest(Services.taskService, "SaveTaskItem", json.encodeToString(TaskItemRequest(taskItemRequestBody)))
            .body()

    /**
     * Edit an existing task item
     */
    suspend fun updateTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): CData<Unit?> =
        makePostRequest(Services.taskService, "UpdateTaskItem", json.encodeToString(TaskItemRequest(taskItemRequestBody)))
            .body()

    /**
     * Delete an existing task item
     */
    suspend fun deleteTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): CData<Unit?> =
        makePostRequest(Services.taskService, "DeleteTaskItem", json.encodeToString(TaskItemRequest(taskItemRequestBody)))
            .body()

    /**
     * Get list of user-defined "tasks"
     */
    suspend fun getTaskItems(baseApiRequest: BaseApiRequest = BaseApiRequest()): CData<Array<TaskItem>> =
        makePostRequest(Services.taskService, "GetTaskItems", json.encodeToString(baseApiRequest))
            .body()

    /**
     * Download a file from compass
     */
    suspend fun downloadFile(assetId: String): CData<String> {
        val res = makeGetRequest(Services.fileAssets, "DownloadFile?id=$assetId")
        if (res.status != HttpStatusCode.OK) return res.body()
        return CData(data = res.bodyAsText())
    }

    /**
     * Get list of lessons for a class instance by its ID
     */
    suspend fun getLessonsByInstanceId(instanceId: String): CData<ActivitySummary> =
        makePostRequest(Services.activity, "GetLessonsByInstanceId", json.encodeToString(ActivitySummaryByInstanceIdRequest(instanceId)))
            .body()

    /**
     * Get class instance by its ID
     */
    suspend fun getLessonsByInstanceIdQuick(instanceId: String): CData<Activity> =
        makePostRequest(Services.activity, "GetLessonsByInstanceIdQuick", json.encodeToString(ActivitySummaryByInstanceIdRequest(instanceId)))
            .body()

    /**
     * Get list of lessons for a class by its activity ID
     */
    suspend fun getLessonsByActivityId(activityId: String): CData<ActivitySummary> =
        makePostRequest(Services.activity, "GetLessonsByActivityId", json.encodeToString(ActivitySummaryByActivityIdRequest(activityId)))
            .body()

    /**
     * Get current class instance its activity ID
     */
    suspend fun getLessonsByActivityIdQuick(activityId: String): CData<Activity> =
        makePostRequest(Services.activity, "GetLessonsByActivityIdQuick", json.encodeToString(ActivitySummaryByActivityIdRequest(activityId)))
            .body()

    /**
     * Get list of learning task categories
     */
    suspend fun getAllTaskCategories(baseApiRequest: BaseApiRequest = BaseApiRequest()): CData<Array<TaskCategory>> =
        makePostRequest(Services.learningTasks, "GetAllTaskCategories", json.encodeToString(baseApiRequest))
            .body()

    /**
     * Get list of learning tasks for a class by class ID
     */
    suspend fun getAllLearningTasksByActivityId(activityId: String): CData<DataExtGridDataContainer<LearningTask>> =
        makePostRequest(Services.learningTasks, "GetAllLearningTasksByActivityId", json.encodeToString(LearningTasksByActivityIdRequest(activityId = activityId)))
            .body()

    /**
     * Get list of learning tasks for the user for a year by their ID
     */
    suspend fun getAllLearningTasksByUserId(academicGroupId: Int? = null): CData<DataExtGridDataContainer<LearningTask>> =
        makePostRequest(Services.learningTasks, "GetAllLearningTasksByUserId", json.encodeToString(LearningTasksByUserIdRequest(userId = credentials.userId, academicGroupId = academicGroupId)))
            .body()

    /**
     * Get the compass Newsfeed
     */
    suspend fun getMyNewsFeedPaged(): CData<DataExtGridDataContainer<NewsItem>> =
        makePostRequest(Services.newsFeed, "GetMyNewsFeedPaged", json.encodeToString(NewsFeedRequest()))
            .body()

    /**
     * Get calendar layers
     */
    suspend fun getCalendarsByUser(): CData<Array<CalendarLayer>> =
        makePostRequest(Services.calendar, "GetCalendarsByUser", json.encodeToString(CalendarLayersRequest()))
            .body()

    /**
     * Get a list of items on the schedule between two dates
     */
    suspend fun getCalendarEventsByUser(startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date, endDate: LocalDate = startDate): CData<Array<CalendarEvent>> =
        makePostRequest(Services.calendar, "GetCalendarEventsByUser", json.encodeToString(CalendarEventsRequest(
            startDate = startDate,
            endDate = endDate,
            userId = credentials.userId
        ))).body()

    /**
     * Get list of compass alerts (Messages that appear above newsfeed asking for your attention)
     */
    suspend fun getMyAlerts(): CData<Array<Alert>> =
        makePostRequest(Services.newsFeed, "GetMyAlerts", "")
            .body()

    /**
     * Get list of rooms and their attributes
     */
    suspend fun getAllLocations(): CData<Array<Location>> =
        makeGetRequest(Services.referenceDataCache, "GetAllLocations")
            .body()

    /**
     * Get list of school campuses
     */
    suspend fun getAllCampuses(): CData<Array<Campus>> =
        makeGetRequest(Services.referenceDataCache, "GetAllCampuses")
            .body()

    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Download the lesson plan for a class instance
     */
    suspend fun getLessonPlanString(activityLessonPlan: ActivityLessonPlan): String? {
        if (activityLessonPlan.fileAssetId == null) return null
        return downloadFile(activityLessonPlan.fileAssetId).data
    }
}

interface CompassClientCredentials {
    val domain: String
    val userId: Int
    val cookie: String
}