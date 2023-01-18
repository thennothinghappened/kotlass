package org.orca.kotlass

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orca.kotlass.data.*

class CompassApiClient(private val domain: String, private val cookiesStorage: ConstantCookiesStorage, private val userId: Int) {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.ALL
//        }
        install(HttpCookies) {
            storage = cookiesStorage
        }
    }

    private val json = Json {
        encodeDefaults = true
    }

    companion object {
        private object Services {
            const val referenceDataCache = "ReferenceDataCache.svc"
            const val newsFeed = "NewsFeed.svc"
            const val calendar = "Calendar.svc"
            const val learningTasks = "LearningTasks.svc"
        }
    }

    private suspend fun makeGetRequest(endpoint: String, location: String): HttpResponse {
        return client.get("https://${domain}/Services/${endpoint}/${location}")
    }

    private suspend fun makePostRequest(endpoint: String, location: String, body: String): HttpResponse {
        println(body)
        return client.post("https://${domain}/Services/${endpoint}/${location}") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    /**
     * Get the compass Newsfeed
     */
    suspend fun getAllTaskCategories(): TaskCategoryList {
        return makePostRequest(Services.learningTasks, "GetAllTaskCategories", json.encodeToString(TaskCategoriesRequest()))
            .body()
    }

    /**
     * Get the compass Newsfeed
     */
    suspend fun getMyNewsFeedPaged(): NewsItemList {
        return makePostRequest(Services.newsFeed, "GetMyNewsFeedPaged", json.encodeToString(NewsFeedRequest()))
            .body()
    }

    /**
     * Get the compass Newsfeed
     */
    suspend fun getCalendarsByUser(): CalendarLayerList {
        return makePostRequest(Services.calendar, "GetCalendarsByUser", json.encodeToString(CalendarLayersRequest()))
            .body()
    }

    /**
     * Get a list of items on the schedule between two dates
     */
    suspend fun getCalendarEventsByUser(startDate: String, endDate: String = startDate): CalendarEventList {
        return makePostRequest(Services.calendar, "GetCalendarEventsByUser", json.encodeToString(CalendarEventsRequest(
            startDate = startDate,
            endDate = endDate,
            userId = userId
        ))).body()
    }

    /**
     * Get list of compass alerts (Messages that appear above newsfeed asking for your attention)
     */
    suspend fun getMyAlerts(): AlertList {
        return makePostRequest(Services.newsFeed, "GetMyAlerts", "")
            .body()
    }

    /**
     * Get list of rooms and their attributes
     */
    suspend fun getAllLocations(): LocationList {
        return makeGetRequest(Services.referenceDataCache, "GetAllLocations")
            .body()
    }

    /**
     * Get list of school campuses
     */
    suspend fun getAllCampuses(): CampusList {
        return makeGetRequest(Services.referenceDataCache, "GetAllCampuses")
            .body()
    }
}