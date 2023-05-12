package org.orca.kotlass

import io.ktor.util.*
import io.ktor.utils.io.*
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

    private val client = KotlassClient(SampleClientCredentials, devMode = true)

    @Test
    fun testValidateCredentials(): Unit {
        doCheck(client.validateCredentials(), "credential validation, credentials are NOT VALID!")
    }

    // this one must happen sequentially and tests creation, modification and deletion of an item.
    @Test
    fun testTaskItem(): Unit = runBlocking {
        // create the task
        val taskItem = TaskItemRequest.TaskItemRequestBody(taskName = "api_test_task")
        val itemId = doCheck(client.saveTaskItem(taskItem), "saveTaskItem")

        // update the task
        delay(500L)
        taskItem.id = itemId.data
        taskItem.taskName = "modified_api_test_task"

        doCheck(client.updateTaskItem(taskItem), "updateTaskItem")

        // check update successful
        delay(500L)
        val reply = doCheck(client.getTaskItems(), "getTaskItems")
        val serversideTask = reply.data.filter { it.id == taskItem.id }[0]
        assertEquals(serversideTask.taskName, taskItem.taskName, "Error in updateTaskItem")

        // delete the task
        delay(500L)
        doCheck(client.deleteTaskItem(taskItem), "deleteTaskItem")
    }

    @Test
    fun testGetStandardClassesOfUserInAcademicGroup(): Unit = runBlocking {
        doCheck(client.getStandardClassesOfUserInAcademicGroup(
            StandardClassesOfUserRequest(
                userId = SampleClientCredentials.userId
            )
        ), "getStandardClassesOfUserInAcademicGroup")
    }

    @Test
    fun testDownloadFile(): Unit = runBlocking {
        doCheck(client.downloadFile(SampleClientCredentials.testFileAssetId), "downloadFile")
    }

    @Test
    fun testGetLessonsByInstanceId(): Unit = runBlocking {
        doCheck(client.getLessonsByInstanceId(SampleClientCredentials.testInstanceId), "getLessonsByInstanceId")
    }

    @Test
    fun testGetLessonsByInstanceIdQuick(): Unit = runBlocking {
        doCheck(client.getLessonsByInstanceIdQuick(SampleClientCredentials.testInstanceId), "getLessonsByInstanceIdQuick")
    }

    @Test
    fun testGetLessonsByActivityId(): Unit = runBlocking {
        doCheck(client.getLessonsByActivityId(SampleClientCredentials.testActivityId), "getLessonsByActivityId")
    }

    @Test
    fun testGetLessonsByActivityIdQuick(): Unit = runBlocking {
        doCheck(client.getLessonsByActivityIdQuick(SampleClientCredentials.testActivityId), "getLessonsByActivityIdQuick")
    }

    @Test
    fun testGetAllLearningTasksByActivityId(): Unit = runBlocking {
        doCheck(client.getAllLearningTasksByActivityId(SampleClientCredentials.testActivityId), "getAllLearningTasksByActivityId")
    }

    @Test
    fun testGetAllLearningTasksByUserId(): Unit = runBlocking {
        doCheck(client.getAllLearningTasksByUserId(SampleClientCredentials.testAcademicGroup), "getAllLearningTasksByUserId")
    }

    @Test
    fun testGetAllTaskCategories(): Unit = runBlocking {
        doCheck(client.getAllTaskCategories(), "getAllTaskCategories")
    }

    @Test
    fun testGetCalendarEventsByUser(): Unit = runBlocking {
        doCheck(client.getCalendarEventsByUser(LocalDate(2023, 1, 30)), "getCalendarEventsByUser")
    }

    @Test
    fun testGetCalendarsByUser(): Unit = runBlocking {
        doCheck(client.getCalendarsByUser(), "getCalendarsByUser")
    }

    @Test
    fun testGetMyNewsFeedPaged(): Unit = runBlocking {
        doCheck(client.getMyNewsFeedPaged(), "getMyNewsFeedPaged")
    }

    @Test
    fun testGetMyAlerts(): Unit = runBlocking {
        doCheck(client.getMyAlerts(), "getMyAlerts")
    }

    @Test
    fun testGetAllLocations(): Unit = runBlocking {
        doCheck(client.getAllLocations(), "getAllLocations")
    }

    @Test
    fun testGetAllCampuses(): Unit = runBlocking {
        doCheck(client.getAllCampuses(), "getAllCampuses")
    }

    @Test
    fun testGetAllAcademicGroups(): Unit = runBlocking {
        doCheck(client.getAllAcademicGroups(), "getAllAcademicGroups")
    }

    @Test
    fun testGetEvents(): Unit = runBlocking {
        doCheck(client.getEvents(), "getEvents")
    }

    @Test
    fun testGetHeaderImageUrlByActivityId(): Unit = runBlocking {
        doCheck(client.getHeaderImageUrlByActivityId(SampleClientCredentials.testActivityId), "getHeaderImageUrlByActivityId")
    }

    @Test
    fun testGetActivityAndSubjectResourcesNode(): Unit = runBlocking {
        doCheck(client.getActivityAndSubjectResourcesNode(SampleClientCredentials.testActivityId), "getActivityAndSubjectResourcesNode")
    }

    private inline fun <reified T> doCheck(reply: NetResponse<T>, name: String): NetResponse.Success<T> {
        if (reply is NetResponse.Error<*>) {
            throw reply.error
        }
        return reply as NetResponse.Success<T>;
    }
}