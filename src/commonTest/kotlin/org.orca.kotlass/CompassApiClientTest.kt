package org.orca.kotlass

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertNull

class CompassApiClientTest {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // for testing the client, create the object SampleClientCredentials and fill in the relevant details from compass' own XHR requests //
    // domain: the domain name of the compass instance                                                                                   //
    // userId: numerical ID corresponding to your user which is sent in requests such as getCalendarEventsByUser                         //
    // testInstanceId: an ID of a class session to test                                                                                  //
    // testActivityId: an ID of a class to test                                                                                          //
    // cookies: cookie storage of the login cookies that compass issues the user                                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val client = CompassApiClient(SampleClientCredentials)
    private val now = Clock.System.now()

    @Test
    fun testGetLessonsByInstanceId() = runBlocking {
        assertNull(client.getLessonsByInstanceId(SampleClientCredentials.testInstanceId).h, "Error in getLessonsByInstanceId")
    }

    @Test
    fun testGetLessonsByInstanceIdQuick() = runBlocking {
        assertNull(client.getLessonsByInstanceIdQuick(SampleClientCredentials.testInstanceId).h, "Error in getLessonsByInstanceIdQuick")
    }

    @Test
    fun testGetAllLearningTasksByActivityId() = runBlocking {
        assertNull(client.getAllLearningTasksByActivityId(SampleClientCredentials.testActivityId).h, "Error in getAllLearningTasksByActivityId")
    }

    @Test
    fun testGetAllLearningTasksByUserId() = runBlocking {
        assertNull(client.getAllLearningTasksByUserId(SampleClientCredentials.testAcademicYear).h, "Error in getAllLearningTasksByUserId")
    }

    @Test
    fun testGetAllTaskCategories() = runBlocking {
        assertNull(client.getAllTaskCategories().h, "Error in getAllTaskCategories")
    }

    @Test
    fun testGetCalendarEventsByUser() = runBlocking {
        assertNull(client.getCalendarEventsByUser(now.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()).h, "Error in getCalendarEventsByUser")
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