package org.orca.kotlass

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orca.kotlass.data.*

class CompassApiClient(private val domain: String, private val cookiesStorage: ConstantCookiesStorage, private val v: String, private val userId: Int) {
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
    suspend fun getMyNewsFeedPaged(): NewsItemList {
        return makePostRequest(Services.newsFeed, "GetMyNewsFeedPaged?sessionstate=readonly", json.encodeToString(NewsFeedRequest()))
            .body()
    }

    suspend fun getCalendarEventsByUser(): CalendarEventList {
        return makePostRequest(Services.calendar, "GetCalendarEventsByUser", json.encodeToString(CalendarEventsRequest(
            startDate = "2023-01-30",
            endDate = "2023-02-05",
            userId = userId
        ))).body()
    }

    /**
     * Get list of compass alerts (Messages that appear above newsfeed asking for your attention)
     */
    suspend fun getMyAlerts(): AlertList {
        return makePostRequest(Services.newsFeed, "GetMyAlerts?sessionstate=readonly", "")
            .body()
    }

    /**
     * Get list of rooms and their attributes
     */
    suspend fun getAllLocations(num: Int = 25): LocationList {
        return makeGetRequest(Services.referenceDataCache, "GetAllLocations?sessionstate=readonly&v=${v}&page=1&start=0&limit=$num")
            .body()
    }

    /**
     * Get list of school campuses
     */
    suspend fun getAllCampuses(num: Int = 25): CampusList {
        return makeGetRequest(Services.referenceDataCache, "GetAllCampuses?sessionstate=readonly&v=${v}&page=1&start=0&limit=$num")
            .body()
    }
}