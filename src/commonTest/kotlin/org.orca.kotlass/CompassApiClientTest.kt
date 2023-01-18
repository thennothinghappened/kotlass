package org.orca.kotlass

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertNull

class CompassApiClientTest {
    // for testing the client, create the object ClientCredentials and fill in the relevant details from compass.
    private val domain = ClientCredentials.domain
    //private val domain = "localhost:5000"

    private val client = CompassApiClient(domain, ClientCredentials.cookies, ClientCredentials.userId)

    @Test
    fun testGetLessonsByInstanceId() = runBlocking {
        assertNull(client.getLessonsByInstanceId(ClientCredentials.testInstanceId).h, "Error in getLessonsByInstanceId")
    }

    @Test
    fun testGetAllTaskCategories() = runBlocking {
        assertNull(client.getAllTaskCategories().h, "Error in getAllTaskCategories")
    }

    @Test
    fun testGetCalendarEventsByUser() = runBlocking {
        assertNull(client.getCalendarEventsByUser("2022-01-30", "2022-02-05").h, "Error in getCalendarEventsByUser")
    }

    @Test
    fun testGetCalendarsByUser() = runBlocking {
        assertNull(client.getCalendarsByUser().h, "Error in getCalendarsByUser")
    }

    @Test
    fun testGetMyNewsFeedPaged() = runBlocking {
        assertNull(client.getMyNewsFeedPaged().h, "Error in getMyNewsFeedPaged")
    }

    @Test
    fun testGetMyAlerts() = runBlocking {
        assertNull(client.getMyAlerts().h, "Error in getMyAlerts")
    }

    @Test
    fun testGetAllLocations() = runBlocking {
        assertNull(client.getAllLocations().h, "Error in getAllLocations")
    }

    @Test
    fun testGetAllCampuses() = runBlocking {
        assertNull(client.getAllCampuses().h, "Error in getAllCampuses")
    }

    @Test
    fun testTime() {

    }
}