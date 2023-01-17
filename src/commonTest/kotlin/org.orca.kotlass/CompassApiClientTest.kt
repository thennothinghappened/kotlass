package org.orca.kotlass

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNull

class CompassApiClientTest {
    private val domain = ClientCredentials.domain
    //private val domain = "localhost:5000"

    private val client = CompassApiClient(domain, ClientCredentials.cookies, ClientCredentials.v, ClientCredentials.userId)

    @Test
    fun testGetCalendarEventsByUser() = runBlocking {
        assertNull(client.getCalendarEventsByUser().h, "Error in getMyNewsFeedPaged")
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
}