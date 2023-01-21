package org.orca.kotlass

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import org.orca.kotlass.data.CData
import org.orca.kotlass.data.TaskItem
import org.orca.kotlass.data.TaskItemRequest
import kotlin.test.Test
import kotlin.test.assertNull

class CompassApiClientTest {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // for testing the client, create the object SampleClientCredentials and fill in the relevant details from compass' own XHR requests //
    // domain: the domain name of the compass instance                                                                                   //
    // userId: numerical ID corresponding to your user which is sent in requests such as getCalendarEventsByUser                         //
    // testInstanceId: an ID of a class session to test                                                                                  //
    // testActivityId: an ID of a class to test                                                                                          //
    // testAcademicYear: a number corresponding to an ID from getAllAcademicGroups                                                       //
    // testFileAssetId: a FileAssetId on the server                                                                                      //
    // cookies: cookie storage of the login cookies that compass issues the user                                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val client = CompassApiClient(SampleClientCredentials)
    private val now = Clock.System.now()

    @Test
    fun testGetTaskItems() = runBlocking {
        assertNull(client.getTaskItems().error, "Error in getTaskItems")
    }

    // this one must happen sequentially and tests creation, modification and deletion of an item.
    @Test
    fun testTaskItem() = runBlocking {
        // create the task
        val taskItem = TaskItemRequest.TaskItemRequestBody(taskName = "api_test_task")
        val itemId = client.saveTaskItem(taskItem)
        assertNull(itemId.error, "Error in saveTaskItem")

        // update the task
        taskItem.id = itemId.data!!
        taskItem.taskName = "modified_api_test_task"
        assertNull(client.updateTaskItem(taskItem).error, "Error in updateTaskItem")

        println(client.getTaskItems().data?.get(0))

        // delete the task
        assertNull(client.deleteTaskItem(taskItem).error, "Error in deleteTaskItem")
    }

    @Test
    fun testDownloadFile() = runBlocking {
        assertNull(client.downloadFile(SampleClientCredentials.testFileAssetId).error, "Error in downloadFile")
    }

    @Test
    fun testGetLessonsByInstanceId() = runBlocking {
        assertNull(client.getLessonsByInstanceId(SampleClientCredentials.testInstanceId).error, "Error in getLessonsByInstanceId")
    }

    @Test
    fun testGetLessonsByInstanceIdQuick() = runBlocking {
        assertNull(client.getLessonsByInstanceIdQuick(SampleClientCredentials.testInstanceId).error, "Error in getLessonsByInstanceIdQuick")
    }

    @Test
    fun testGetLessonsByActivityId() = runBlocking {
        assertNull(client.getLessonsByActivityId(SampleClientCredentials.testActivityId).error, "Error in getLessonsByActivityId")
    }

    @Test
    fun testGetLessonsByActivityIdQuick() = runBlocking {
        assertNull(client.getLessonsByActivityIdQuick(SampleClientCredentials.testActivityId).error, "Error in getLessonsByActivityIdQuick")
    }

    @Test
    fun testGetAllLearningTasksByActivityId() = runBlocking {
        assertNull(client.getAllLearningTasksByActivityId(SampleClientCredentials.testActivityId).error, "Error in getAllLearningTasksByActivityId")
    }

    @Test
    fun testGetAllLearningTasksByUserId() = runBlocking {
        assertNull(client.getAllLearningTasksByUserId(SampleClientCredentials.testAcademicYear).error, "Error in getAllLearningTasksByUserId")
    }

    @Test
    fun testGetAllTaskCategories() = runBlocking {
        assertNull(client.getAllTaskCategories().error, "Error in getAllTaskCategories")
    }

    @Test
    fun testGetCalendarEventsByUser() = runBlocking {
        assertNull(client.getCalendarEventsByUser(now.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()).error, "Error in getCalendarEventsByUser")
    }

    @Test
    fun testGetCalendarsByUser() = runBlocking {
        assertNull(client.getCalendarsByUser().error, "Error in getCalendarsByUser")
    }

    @Test
    fun testGetMyNewsFeedPaged() = runBlocking {
        assertNull(client.getMyNewsFeedPaged().error, "Error in getMyNewsFeedPaged")
    }

    @Test
    fun testGetMyAlerts() = runBlocking {
        assertNull(client.getMyAlerts().error, "Error in getMyAlerts")
    }

    @Test
    fun testGetAllLocations() = runBlocking {
        assertNull(client.getAllLocations().error, "Error in getAllLocations")
    }

    @Test
    fun testGetAllCampuses() = runBlocking {
        assertNull(client.getAllCampuses().error, "Error in getAllCampuses")
    }
}