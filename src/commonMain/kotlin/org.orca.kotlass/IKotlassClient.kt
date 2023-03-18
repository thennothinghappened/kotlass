package org.orca.kotlass

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.orca.kotlass.data.*

interface IKotlassClient {

    /**
     * Returns a URL which starts with the client's domain.
     */
    fun buildDomainUrlString(endpoint: String): String

    /**
     * Make sure our credentials are valid.
     * If the result is NetResponse.Success, our credentials are valid.
     *
     * If the result is NetResponse.ClientError, we probably don't have internet.
     *
     * If the result is NetResponse.RequestFailure, there's something wrong with our credentials, or the Compass server.
     */
    fun validateCredentials():
            NetResponse<Unit?>

    /**
     * Create a new task item
     */
    suspend fun saveTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody):
            NetResponse<Int>

    /**
     * Edit an existing task item
     */
    suspend fun updateTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody):
            NetResponse<Unit?>

    /**
     * Delete an existing task item
     */
    suspend fun deleteTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody):
            NetResponse<Unit?>

    /**
     * Get list of user-defined "tasks"
     */
    suspend fun getTaskItems(baseApiRequest: BaseApiRequest = BaseApiRequest()):
            NetResponse<List<TaskItem>>

    /**
     * Get the list of classes that the user is in
     */
    suspend fun getStandardClassesOfUserInAcademicGroup(standardClassesOfUserRequest: StandardClassesOfUserRequest):
            NetResponse<DataExtGridDataContainer<StandardClass>>

    /**
     * Download a file from compass
     */
    suspend fun downloadFile(assetId: String):
            NetResponse<String>

    /**
     * Get list of lessons for a class instance by its ID
     */
    suspend fun getLessonsByInstanceId(instanceId: String):
            NetResponse<ActivitySummary>

    /**
     * Get class instance by its ID
     */
    suspend fun getLessonsByInstanceIdQuick(instanceId: String):
            NetResponse<Activity>

    /**
     * Get list of lessons for a class by its activity ID
     */
    suspend fun getLessonsByActivityId(activityId: Int):
            NetResponse<ActivitySummary>

    /**
     * Get current class instance its activity ID
     */
    suspend fun getLessonsByActivityIdQuick(activityId: Int): NetResponse<Activity>

    /**
     * Get list of learning task categories
     */
    suspend fun getAllTaskCategories(baseApiRequest: BaseApiRequest = BaseApiRequest()):
            NetResponse<List<TaskCategory>>

    /**
     * Get list of learning tasks for a class by class ID
     */
    suspend fun getAllLearningTasksByActivityId(activityId: Int):
            NetResponse<DataExtGridDataContainer<LearningTask>>

    /**
     * Get list of learning tasks for the user for a year by their ID
     */
    suspend fun getAllLearningTasksByUserId(academicGroup: AcademicGroup? = null):
            NetResponse<DataExtGridDataContainer<LearningTask>>

    /**
     * Get the compass Newsfeed
     */
    suspend fun getMyNewsFeedPaged():
            NetResponse<DataExtGridDataContainer<NewsItem>>

    /**
     * Get calendar layers
     */
    suspend fun getCalendarsByUser():
            NetResponse<List<CalendarLayer>>

    /**
     * Get a list of items on the schedule between two dates
     */
    suspend fun getCalendarEventsByUser(
        startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        endDate: LocalDate = startDate
    ): NetResponse<List<CalendarEvent>>

    /**
     * Get list of compass alerts (Messages that appear above newsfeed asking for your attention)
     */
    suspend fun getMyAlerts():
            NetResponse<List<Alert>>

    /**
     * Get list of rooms and their attributes
     */
    suspend fun getAllLocations():
            NetResponse<List<Location>>

    /**
     * Get list of school campuses
     */
    suspend fun getAllCampuses():
            NetResponse<List<Campus>>

    /**
     * Get list of Academic Groups
     */
    suspend fun getAllAcademicGroups():
            NetResponse<List<AcademicGroup>>

    /**
     * Get the URL for the banner image for an activity.
     */
    suspend fun getHeaderImageUrlByActivityId(activityId: Int):
            NetResponse<String>

    /**
     *
     */

    /**
     * Download the lesson plan for a class instance
     */
    suspend fun getLessonPlanString(activityLessonPlan: ActivityLessonPlan):
            NetResponse<String>


}