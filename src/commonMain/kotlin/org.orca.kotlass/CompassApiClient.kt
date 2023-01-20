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
            const val referenceDataCache = "ReferenceDataCache.svc"
            const val newsFeed = "NewsFeed.svc"
            const val calendar = "Calendar.svc"
            const val activity = "Activity.svc"
            const val learningTasks = "LearningTasks.svc"
        }
    }

    private suspend fun makeGetRequest(endpoint: String, location: String): HttpResponse {
        return client.get("https://${credentials.domain}/Services/${endpoint}/${location}") {
            headers {
                append(HttpHeaders.Cookie, credentials.cookie)
            }
        }
    }

    private suspend fun makePostRequest(endpoint: String, location: String, body: String): HttpResponse {
        println(body)
        return client.post("https://${credentials.domain}/Services/${endpoint}/${location}") {
            headers {
                append(HttpHeaders.Cookie, credentials.cookie)
            }
            contentType(ContentType.Application.Json)
            setBody(body)

        }
    }

    /**
     * Get list of lessons for a class instance by its ID
     */
    suspend fun getLessonsByInstanceId(instanceId: String): CData<ActivitySummary> {
        return makePostRequest(Services.activity, "GetLessonsByInstanceId", json.encodeToString(ActivitySummaryRequest(instanceId)))
            .body()
    }

    /**
     * Get list of lessons for a class instance by its ID
     */
    suspend fun getLessonsByInstanceIdQuick(instanceId: String): CData<Activity> {
        return makePostRequest(Services.activity, "GetLessonsByInstanceIdQuick", json.encodeToString(ActivitySummaryRequest(instanceId)))
            .body()
    }

    /**
     * Get list of learning task categories
     */
    suspend fun getAllTaskCategories(): CData<Array<TaskCategory>> {
        return makePostRequest(Services.learningTasks, "GetAllTaskCategories", json.encodeToString(TaskCategoriesRequest()))
            .body()
    }

    /**
     * Get list of learning tasks for a class by class ID
     */
    suspend fun getAllLearningTasksByActivityId(instanceId: String): CData<DataExtGridDataContainer<LearningTask>> {
        return makePostRequest(Services.learningTasks, "GetAllLearningTasksByActivityId", json.encodeToString(LearningTasksByActivityIdRequest(activityId = instanceId)))
            .body()
    }

    /**
     * Get list of learning tasks for the user for a year by their ID
     */
    suspend fun getAllLearningTasksByUserId(academicGroupId: Int? = null): CData<DataExtGridDataContainer<LearningTask>> {
        return makePostRequest(Services.learningTasks, "GetAllLearningTasksByUserId", json.encodeToString(LearningTasksByUserIdRequest(userId = credentials.userId, academicGroupId = academicGroupId)))
            .body()
    }

    /**
     * Get the compass Newsfeed
     */
    suspend fun getMyNewsFeedPaged(): CData<DataExtGridDataContainer<NewsItem>> {
        return makePostRequest(Services.newsFeed, "GetMyNewsFeedPaged", json.encodeToString(NewsFeedRequest()))
            .body()
    }

    /**
     * Get calendar layers
     */
    suspend fun getCalendarsByUser(): CData<Array<CalendarLayer>> {
        return makePostRequest(Services.calendar, "GetCalendarsByUser", json.encodeToString(CalendarLayersRequest()))
            .body()
    }

    /**
     * Get a list of items on the schedule between two dates
     */
    suspend fun getCalendarEventsByUser(startDate: String, endDate: String = startDate): CData<Array<CalendarEvent>> {
        return makePostRequest(Services.calendar, "GetCalendarEventsByUser", json.encodeToString(CalendarEventsRequest(
            startDate = startDate,
            endDate = endDate,
            userId = credentials.userId
        ))).body()
    }

    /**
     * Get list of compass alerts (Messages that appear above newsfeed asking for your attention)
     */
    suspend fun getMyAlerts(): CData<Array<Alert>> {
        return makePostRequest(Services.newsFeed, "GetMyAlerts", "")
            .body()
    }

    /**
     * Get list of rooms and their attributes
     */
    suspend fun getAllLocations(): CData<Array<Location>> {
        return makeGetRequest(Services.referenceDataCache, "GetAllLocations")
            .body()
    }

    /**
     * Get list of school campuses
     */
    suspend fun getAllCampuses(): CData<Array<Campus>> {
        return makeGetRequest(Services.referenceDataCache, "GetAllCampuses")
            .body()
    }
}

interface CompassClientCredentials {
    val domain: String
    val userId: Int
    val cookie: String
}