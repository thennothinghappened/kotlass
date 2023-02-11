package org.orca.kotlass

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlinx.serialization.decodeFromString
import org.orca.kotlass.data.*
import kotlin.test.*

class CompassApiClientTest {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // for testing the client, create the object SampleClientCredentials and fill in the relevant details from compass' own XHR requests //
    // domain: the domain name of the compass instance                                                                                   //
    // userId: numerical ID corresponding to your user which is sent in requests such as getCalendarEventsByUser                         //
    // testInstanceId: an ID of a class session to test                                                                                  //
    // testActivityId: an ID of a class to test                                                                                          //
    // testAcademicGroup: instance of an AcademicGroup from getAllAcademicGroups                                                         //
    // testFileAssetId: a FileAssetId on the server                                                                                      //
    // cookies: cookie storage of the login cookies that compass issues the user                                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val client = KotlassClient(SampleClientCredentials)

    @Test
    fun testValidateCredentials(): Unit {
        assertTrue(client.validateCredentials(), "Credentials are invalid!")
    }

    // this one must happen sequentially and tests creation, modification and deletion of an item.
    @Test
    fun testTaskItem(): Unit = runBlocking {
        // create the task
        val taskItem = TaskItemRequest.TaskItemRequestBody(taskName = "api_test_task")
        val itemId = client.saveTaskItem(taskItem)
        assertIs<NetResponse.Success<Int>>(itemId, "Error in saveTaskItem")

        // update the task
        delay(500L)
        taskItem.id = itemId.data
        taskItem.taskName = "modified_api_test_task"

        assertIs<NetResponse.Success<*>>(client.updateTaskItem(taskItem), "Error in updateTaskItem")

        // check update successful
        delay(500L)
        val reply = client.getTaskItems()
        assertIs<NetResponse.Success<Array<TaskItem>>>(reply, "Error in getTaskItems")
        val serversideTask = reply.data.filter { it.id == taskItem.id }[0]
        assertEquals(serversideTask.taskName, taskItem.taskName, "Error in updateTaskItem")

        // delete the task
        delay(500L)
        assertIs<NetResponse.Success<*>>(client.deleteTaskItem(taskItem), "Error in deleteTaskItem")
    }

    @Test
    fun testGetStandardClassesOfUserInAcademicGroup(): Unit = runBlocking {
        val reply = client.getStandardClassesOfUserInAcademicGroup(
            StandardClassesOfUserRequest(
                userId = SampleClientCredentials.userId
            )
        )
        assertIs<NetResponse.Success<*>>(reply, "Error in getStandardClassesOfUserInAcademicGroup: $reply")
    }

    @Test
    fun testDownloadFile(): Unit = runBlocking {
        val reply = client.downloadFile(SampleClientCredentials.testFileAssetId)
        assertIs<NetResponse.Success<*>>(reply, "Error in downloadFile: $reply")
    }

    @Test
    fun testGetLessonsByInstanceId(): Unit = runBlocking {
        val reply = client.getLessonsByInstanceId(SampleClientCredentials.testInstanceId)
        assertIs<NetResponse.Success<*>>(reply, "Error in getLessonsByInstanceId: $reply")
    }

    @Test
    fun testGetLessonsByInstanceIdQuick(): Unit = runBlocking {
        val reply = client.getLessonsByInstanceIdQuick(SampleClientCredentials.testInstanceId)
        assertIs<NetResponse.Success<*>>(reply, "Error in getLessonsByInstanceIdQuick: $reply")
    }

    @Test
    fun testGetLessonsByActivityId(): Unit = runBlocking {
        val reply = client.getLessonsByActivityId(SampleClientCredentials.testActivityId)
        assertIs<NetResponse.Success<*>>(reply, "Error in getLessonsByActivityId: $reply")
    }

    @Test
    fun testGetLessonsByActivityIdQuick(): Unit = runBlocking {
        val reply = client.getLessonsByActivityIdQuick(SampleClientCredentials.testActivityId)
        assertIs<NetResponse.Success<*>>(reply, "Error in getLessonsByActivityIdQuick: $reply")
    }

    @Test
    fun testGetAllLearningTasksByActivityId(): Unit = runBlocking {
        val reply = client.getAllLearningTasksByActivityId(SampleClientCredentials.testActivityId)
        assertIs<NetResponse.Success<*>>(reply, "Error in getAllLearningTasksByActivityId: $reply")
    }

    @Test
    fun testGetAllLearningTasksByUserId(): Unit = runBlocking {
        val reply = client.getAllLearningTasksByUserId(SampleClientCredentials.testAcademicGroup)
        assertIs<NetResponse.Success<*>>(reply, "Error in getAllLearningTasksByUserId: $reply")
    }

    @Test
    fun testGetAllTaskCategories(): Unit = runBlocking {
        val reply = client.getAllTaskCategories()
        assertIs<NetResponse.Success<*>>(reply, "Error in getAllTaskCategories: $reply")
    }

    @Test
    fun testGetCalendarEventsByUser(): Unit = runBlocking {
        val reply = client.getCalendarEventsByUser(LocalDate(2023, 1, 30))
        assertIs<NetResponse.Success<*>>(reply, "Error in getCalendarEventsByUser: $reply")
    }

    @Test
    fun testGetCalendarsByUser(): Unit = runBlocking {
        val reply = client.getCalendarsByUser()
        assertIs<NetResponse.Success<*>>(reply, "Error in getCalendarsByUser: $reply")
    }

    @Test
    fun testGetMyNewsFeedPaged(): Unit = runBlocking {
        val reply = client.getMyNewsFeedPaged()
        assertIs<NetResponse.Success<*>>(reply, "Error in getMyNewsFeedPaged: $reply")
    }

    @Test
    fun testGetMyAlerts(): Unit = runBlocking {
        val reply = client.getMyAlerts()
        assertIs<NetResponse.Success<*>>(reply, "Error in getMyAlerts: $reply")
    }

    @Test
    fun testGetAllLocations(): Unit = runBlocking {
        val reply = client.getAllLocations()
        assertIs<NetResponse.Success<*>>(reply, "Error in getAllLocations: $reply")
    }

    @Test
    fun testGetAllCampuses(): Unit = runBlocking {
        val reply = client.getAllCampuses()
        assertIs<NetResponse.Success<*>>(reply, "Error in getAllCampuses: $reply")
    }

    @Test
    fun testGetAllAcademicGroups(): Unit = runBlocking {
        val reply = client.getAllAcademicGroups()
        assertIs<NetResponse.Success<*>>(reply, "Error in getAllAcademicGroups: $reply")
    }

    @Test
    fun testGetHeaderImageUrlByActivityId(): Unit = runBlocking {
        val reply = client.getHeaderImageUrlByActivityId(SampleClientCredentials.testActivityId.toInt())
        assertIs<NetResponse.Success<*>>(reply, "Error in getHeaderImageUrlByActivityId: $reply")
    }
}