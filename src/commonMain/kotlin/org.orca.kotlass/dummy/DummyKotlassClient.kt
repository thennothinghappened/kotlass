package org.orca.kotlass.dummy

import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.datetime.*
import org.orca.kotlass.IKotlassClient
import org.orca.kotlass.data.*
import kotlin.random.Random

open class DummyKotlassClient(
    private val domain: String,
    private val fakeWaitTime: Long = 0L
) : IKotlassClient {

    private val random = Random(0)

    /**
     * Serverside TaskItem list
     */
    private val taskItems = MutableList(
        2
    ) { id ->
        TaskItem(
            id = id,
            taskName = "Test task #$id"
        )
    }

    /**
     * Serverside StandardClass list
     */
    private val standardClasses = listOf(
        createDummyStandardClass("ENG", "EN0001", "English", "TEA"),
        createDummyStandardClass("MAT", "MA0001", "Mathematics", "TEA"),
        createDummyStandardClass("SCI", "SC0001", "Science", "TEA")
    )

    /**
     * Serverside NewsItem list
     */
    private val newsfeed = listOf(
        createDummyNewsItem(
            title = "Some news item",
            content = "A thingo",
            userName = "Someone important"
        )
    )

    /**
     * Serverside Schedule
     */
    private val timeZone = TimeZone.currentSystemDefault()
    private val startTime = Clock.System.now()
        .toLocalDateTime(timeZone).date
        .atTime(9, 0, 0)
        .toInstant(timeZone)

    private val schedule = listOf(
        ScheduleEntry.BaseEntry(
            createDummyCalendarEvent(
                instanceId = "1",
                start = startTime,
                finish = startTime.plus(1, DateTimeUnit.HOUR),
                title = "English"
            )
        ),
        ScheduleEntry.BaseEntry(
            createDummyCalendarEvent(
                instanceId = "1",
                start = startTime.plus(1, DateTimeUnit.HOUR),
                finish = startTime.plus(2, DateTimeUnit.HOUR),
                title = "Mathematics"
            )
        ),
        ScheduleEntry.BaseEntry(
            createDummyCalendarEvent(
                instanceId = "1",
                start = startTime.plus(2, DateTimeUnit.HOUR),
                finish = startTime.plus(3, DateTimeUnit.HOUR),
                title = "Science"
            )
        )
    )

    /**
     * Bundle to group CalendarEntries with their other associated info.
     */
    sealed class ScheduleEntry(
        val calendarEvent: CalendarEvent,
    ) {

        class BaseEntry(
            calendarEvent: CalendarEvent
        ) : ScheduleEntry(calendarEvent)
        class ActivityEntry(
            calendarEvent: CalendarEvent,
            val activity: Activity
        ) : ScheduleEntry(calendarEvent)
    }

    /**
     * Generate a dummy StandardClass
     */
    private fun createDummyStandardClass(
        facultyName: String,
        name: String,
        longName: String,
        managerName: String,
        start: Instant = LocalDateTime(
            2023, 1, 1, 0, 0, 0, 0
        ).toInstant(TimeZone.currentSystemDefault()),
        finish: Instant = LocalDateTime(
            2023, 12, 31, 0, 0, 0, 0
        ).toInstant(TimeZone.currentSystemDefault())
    ) = StandardClass(
        facultyName = facultyName,
        start = start,
        finish = finish,
        activityId = random.nextInt(10000, 30000),
        importIdentifier = name,
        locationId = null,
        managerId = random.nextInt(1, 1000),
        managerShortName = managerName,
        name = name,
        subjectId = random.nextInt(5000),
        subjectLongName = longName,
        yearLevelShortName = "Year ${random.nextInt(7, 13)}",
        attendanceModeDefault = 1,
        checkInEnabledDefault = 0,
        extendedStatusId = 110,
        haparaSyncEnabled = false,
        importTeachers = false,
        layerAllowsImport = true,
        layerId = 2,
        rollTapThreshold = 0,
        subjectImportIdentifier = name,
        timetableStructureId = 2
    )

    /**
     * Generate a dummy NewsItem
     */
    private fun createDummyNewsItem(
        title: String,
        content: String,
        attachments: List<NewsItemAttachment> = emptyList(),
        priority: Boolean = false,
        createdByAdmin: Boolean = false,
        userImageUrl: String = "",
        userName: String,
        newsItemGroupTargets: List<NewsItemGroupTarget> = emptyList(),

    ) = NewsItem(
        dataType = "",
        title = title,
        content1 = content,
        content2 = null,
        attachments = attachments,
        communicationType = 0,
        priority = priority,
        createdByAdmin = createdByAdmin,
        userImageUrl = userImageUrl,
        userName = userName,
        newsItemGroupTargets = newsItemGroupTargets,
        showImagesFullscreen = false,
        userId = 0,
        emailSentDate = "",
        postDateTime = null,
        startDate = null,
        finishDate = null,
        locked = true,
        newsItemCustomGroupTargets = NewsItemCustomGroupTarget(
            "",
            emptyList(),
            emptyList()
        ),
        newsItemId = "",
        publicWebsite = false,
        publishToLinkedSchools = false,
        publishToTalkingPoints = false,
        startFinishString = "",
        talkingPointsTags = emptyList()
    )

    /**
     * Generate a dummy CalendarEvent
     */
    private fun createDummyCalendarEvent(
        activityId: Int = 0,
        activityType: Int = 1,
        backgroundColor: Long = 0xDCE6F4,
        description: String = "",
        start: Instant,
        finish: Instant,
        instanceId: String,
        title: String = "",
        longTitleWithoutTime: String = title

    ) = CalendarEvent(
        dataType = "",
        activityId = activityId,
        activityType = activityType,
        backgroundColor = backgroundColor,
        description = description,
        start = start,
        finish = finish,
        instanceId = instanceId,
        title = title,
        longTitleWithoutTime = longTitleWithoutTime,
        allDay = false,
        attendanceMode = 1,
        attendeeUserId = 0,
        guid = "",
        isRecurring = false,
        repeatForever = false,
        repeatFrequency = 0,
        rollMarked = true,
        runningStatus = 1,
        teachingDaysOnly = false
    )

    private fun findTaskById(id: Int) =
        taskItems.indexOfFirst { it.id == id }

    override fun buildDomainUrlString(endpoint: String): String =
        "https://$domain$endpoint"

    override fun buildDomainFileDownloadString(fileAssetId: String, originalFileName: String): String {
        TODO("Not yet implemented")
    }

    override fun validateCredentials(): NetResponse<Unit?> = NetResponse.Success(null)
    override suspend fun saveTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Int> {
        val newTaskId = taskItems.size

        taskItems.add(TaskItem(
            id = newTaskId,
            taskName = taskItemRequestBody.taskName,
            dueDate = taskItemRequestBody.dueDate,
            status = taskItemRequestBody.status
        ))

        delay(fakeWaitTime)
        return NetResponse.Success(newTaskId)
    }

    override suspend fun updateTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Unit?> {
        val item = findTaskById(taskItemRequestBody.id)

        if (item == -1)
            return NetResponse.RequestFailure(HttpStatusCode.InternalServerError)

        taskItems[item] = taskItems[item].copy(
            taskName = taskItemRequestBody.taskName,
            dueDate = taskItemRequestBody.dueDate,
            status = taskItemRequestBody.status
        )

        delay(fakeWaitTime)
        return NetResponse.Success(null)
    }

    override suspend fun deleteTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Unit?> {
        val item = findTaskById(taskItemRequestBody.id)

        if (item == -1)
            return NetResponse.RequestFailure(HttpStatusCode.InternalServerError)

        taskItems.removeAt(item)

        delay(fakeWaitTime)
        return NetResponse.Success(null)
    }

    override suspend fun getTaskItems(baseApiRequest: BaseApiRequest): NetResponse<List<TaskItem>> {
        delay(fakeWaitTime)
        return NetResponse.Success(taskItems)
    }

    override suspend fun getStandardClassesOfUserInAcademicGroup(standardClassesOfUserRequest: StandardClassesOfUserRequest): NetResponse<DataExtGridDataContainer<StandardClass>> {
        delay(fakeWaitTime)
        return NetResponse.Success(DataExtGridDataContainer(data = standardClasses))
    }

    override suspend fun downloadFile(assetId: String): NetResponse<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getLessonsByInstanceId(instanceId: String): NetResponse<ActivitySummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getLessonsByInstanceIdQuick(instanceId: String): NetResponse<Activity> {
        TODO("Not yet implemented")
    }

    override suspend fun getLessonsByActivityId(activityId: Int): NetResponse<ActivitySummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getLessonsByActivityIdQuick(activityId: Int): NetResponse<Activity> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTaskCategories(baseApiRequest: BaseApiRequest): NetResponse<List<TaskCategory>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLearningTasksByActivityId(activityId: Int): NetResponse<DataExtGridDataContainer<LearningTask>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLearningTasksByUserId(academicGroup: AcademicGroup?): NetResponse<DataExtGridDataContainer<LearningTask>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyNewsFeedPaged(): NetResponse<DataExtGridDataContainer<NewsItem>> {
        delay(fakeWaitTime)
        return NetResponse.Success(
            DataExtGridDataContainer(
            data = newsfeed
        )
        )
    }

    override suspend fun getCalendarsByUser(): NetResponse<List<CalendarLayer>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCalendarEventsByUser(
        startDate: LocalDate,
        endDate: LocalDate
    ): NetResponse<List<CalendarEvent>> {
        delay(fakeWaitTime)
        return NetResponse.Success(schedule.map { entry -> entry.calendarEvent })
    }

    override suspend fun getMyAlerts(): NetResponse<List<Alert>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLocations(): NetResponse<List<Location>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCampuses(): NetResponse<List<Campus>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAcademicGroups(): NetResponse<List<AcademicGroup>> {
        TODO("Not yet implemented")
    }

    override suspend fun getHeaderImageUrlByActivityId(activityId: Int): NetResponse<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getActivityAndSubjectResourcesNode(activityId: Int): NetResponse<ResourceNode> {
        TODO("Not yet implemented")
    }

    override suspend fun getLessonPlanString(activityLessonPlan: ActivityLessonPlan): NetResponse<String> {
        TODO("Not yet implemented")
    }

}