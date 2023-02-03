package org.orca.kotlass

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orca.kotlass.data.*

open class CompassApiClient(
    private val credentials: CompassClientCredentials,
    private val scope: CoroutineScope,
    private val refreshIntervals: RefreshIntervals = RefreshIntervals()
) {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000 // it's a slow site
        }
        install(HttpCache) {
            // todo: set this up (different implementations for desktop and android)
        }
    }

    private object Services {
        const val referenceDataCache = "ReferenceDataCache"
        const val newsFeed = "NewsFeed"
        const val calendar = "Calendar"
        const val activity = "Activity"
        const val learningTasks = "LearningTasks"
        const val fileAssets = "FileAssets"
        const val taskService = "TaskService"
        const val subjects = "Subjects"
    }

    private fun buildApiRequestUrl(endpoint: String, location: String) = "https://${credentials.domain}/Services/${endpoint}.svc/${location}"
    private suspend inline fun <reified T> makeApiGetRequestPlain(endpoint: String, location: String): NetResponse<T> {
        val reply: HttpResponse

        try {
            reply = client.get(buildApiRequestUrl(endpoint, location)) {
                headers {
                    append(HttpHeaders.Cookie, credentials.cookie)
                }
            }
            if (reply.status != HttpStatusCode.OK)
                return NetResponse.RequestFailure(reply.status)

        } catch (e: Throwable) {
            return NetResponse.ClientError(e)
        }

        return NetResponse.Success(reply.body())
    }

    private suspend inline fun <reified T, reified B> makeApiPostRequestPlain(endpoint: String, location: String, body: B): NetResponse<T> {
        val reply: HttpResponse

        try {
            reply = client.post(buildApiRequestUrl(endpoint, location)) {
                headers {
                    append(HttpHeaders.Cookie, credentials.cookie)
                }
                contentType(ContentType.Application.Json)
                setBody(json.encodeToString(body))
            }

            if (reply.status != HttpStatusCode.OK)
                return NetResponse.RequestFailure(reply.status)

        } catch (e: Throwable) {
            return NetResponse.ClientError(e)
        }

        return NetResponse.Success(reply.body())
    }

    private suspend inline fun <reified T> makeApiGetRequest(endpoint: String, location: String): NetResponse<T> {
        val reply: CData<T>

        try {
            val _reply = client.get(buildApiRequestUrl(endpoint, location)) {
                headers {
                    append(HttpHeaders.Cookie, credentials.cookie)
                }
            }

            if (_reply.status != HttpStatusCode.OK)
                return NetResponse.RequestFailure(_reply.status)

            reply = _reply.body()
        } catch (e: Throwable) {
            return NetResponse.ClientError(e)
        }

        return NetResponse.Success(reply.data!!)
    }

    private suspend inline fun <reified T, reified B> makeApiPostRequest(endpoint: String, location: String, body: B): NetResponse<T> {
        val reply: CData<T>

        try {
            val _reply = client.post(buildApiRequestUrl(endpoint, location)) {
                headers {
                    append(HttpHeaders.Cookie, credentials.cookie)
                }
                contentType(ContentType.Application.Json)
                setBody(json.encodeToString(body))
            }

            if (_reply.status != HttpStatusCode.OK)
                return NetResponse.RequestFailure(_reply.status)

            reply = _reply.body()
        } catch (e: Throwable) {
            return NetResponse.ClientError(e)
        }

        return NetResponse.Success(reply.data!!)
    }

    /**
     * Create a new task item
     */
    suspend fun saveTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Int> =
        makeApiPostRequest(Services.taskService, "SaveTaskItem", TaskItemRequest(taskItemRequestBody))

    /**
     * Edit an existing task item
     */
    suspend fun updateTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Unit?> =
        makeApiPostRequestPlain(
            Services.taskService,
            "UpdateTaskItem",
            TaskItemRequest(taskItemRequestBody)
        )

    /**
     * Delete an existing task item
     */
    suspend fun deleteTaskItem(taskItemRequestBody: TaskItemRequest.TaskItemRequestBody): NetResponse<Unit?> =
        makeApiPostRequestPlain(
            Services.taskService,
            "DeleteTaskItem",
            TaskItemRequest(taskItemRequestBody)
        )

    /**
     * Get list of user-defined "tasks"
     */
    suspend fun getTaskItems(baseApiRequest: BaseApiRequest = BaseApiRequest()): NetResponse<Array<TaskItem>> =
        makeApiPostRequest(
            Services.taskService,
            "GetTaskItems",
            baseApiRequest
        )

    /**
     * Get the list of classes that the user is in
     */
    suspend fun getStandardClassesOfUserInAcademicGroup(standardClassesOfUserRequest: StandardClassesOfUserRequest): NetResponse<DataExtGridDataContainer<StandardClass>> =
        makeApiPostRequest(
            Services.subjects,
            "GetStandardClassesOfUserInAcademicGroup",
            standardClassesOfUserRequest
        )

    /**
     * Download a file from compass
     */
    suspend fun downloadFile(assetId: String): NetResponse<String> =
        makeApiGetRequestPlain(Services.fileAssets, "DownloadFile?id=$assetId")

    /**
     * Get list of lessons for a class instance by its ID
     */
    suspend fun getLessonsByInstanceId(instanceId: String): NetResponse<ActivitySummary> =
        makeApiPostRequest(Services.activity, "GetLessonsByInstanceId", ActivitySummaryByInstanceIdRequest(instanceId))

    /**
     * Get class instance by its ID
     */
    suspend fun getLessonsByInstanceIdQuick(instanceId: String): NetResponse<Activity> =
        makeApiPostRequest(Services.activity, "GetLessonsByInstanceIdQuick", ActivitySummaryByInstanceIdRequest(instanceId))

    /**
     * Get list of lessons for a class by its activity ID
     */
    suspend fun getLessonsByActivityId(activityId: String): NetResponse<ActivitySummary> =
        makeApiPostRequest(Services.activity, "GetLessonsByActivityId", ActivitySummaryByActivityIdRequest(activityId))

    /**
     * Get current class instance its activity ID
     */
    suspend fun getLessonsByActivityIdQuick(activityId: String): NetResponse<Activity> =
        makeApiPostRequest(Services.activity, "GetLessonsByActivityIdQuick", ActivitySummaryByActivityIdRequest(activityId))

    /**
     * Get list of learning task categories
     */
    suspend fun getAllTaskCategories(baseApiRequest: BaseApiRequest = BaseApiRequest()): NetResponse<Array<TaskCategory>> =
        makeApiPostRequest(Services.learningTasks, "GetAllTaskCategories", baseApiRequest)

    /**
     * Get list of learning tasks for a class by class ID
     */
    suspend fun getAllLearningTasksByActivityId(activityId: String): NetResponse<DataExtGridDataContainer<LearningTask>> =
        makeApiPostRequest(Services.learningTasks, "GetAllLearningTasksByActivityId", LearningTasksByActivityIdRequest(activityId = activityId))

    /**
     * Get list of learning tasks for the user for a year by their ID
     */
    suspend fun getAllLearningTasksByUserId(academicGroup: AcademicGroup?): NetResponse<DataExtGridDataContainer<LearningTask>> =
        makeApiPostRequest(Services.learningTasks, "GetAllLearningTasksByUserId", LearningTasksByUserIdRequest(userId = credentials.userId, academicGroupId = academicGroup?.id))

    /**
     * Get the compass Newsfeed
     */
    suspend fun getMyNewsFeedPaged(): NetResponse<DataExtGridDataContainer<NewsItem>> =
        makeApiPostRequest(Services.newsFeed, "GetMyNewsFeedPaged", NewsFeedRequest())

    /**
     * Get calendar layers
     */
    suspend fun getCalendarsByUser(): NetResponse<Array<CalendarLayer>> =
        makeApiPostRequest(Services.calendar, "GetCalendarsByUser", CalendarLayersRequest())

    /**
     * Get a list of items on the schedule between two dates
     */
    suspend fun getCalendarEventsByUser(
        startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        endDate: LocalDate = startDate
    ): NetResponse<Array<CalendarEvent>> =
        makeApiPostRequest(Services.calendar, "GetCalendarEventsByUser", CalendarEventsRequest(
            startDate = startDate,
            endDate = endDate,
            userId = credentials.userId
        ))

    /**
     * Get list of compass alerts (Messages that appear above newsfeed asking for your attention)
     */
    suspend fun getMyAlerts(): NetResponse<Array<Alert>> =
        makeApiPostRequest(Services.newsFeed, "GetMyAlerts", "")

    /**
     * Get list of rooms and their attributes
     */
    suspend fun getAllLocations(): NetResponse<Array<Location>> =
        makeApiGetRequest(Services.referenceDataCache, "GetAllLocations")

    /**
     * Get list of school campuses
     */
    suspend fun getAllCampuses(): NetResponse<Array<Campus>> =
        makeApiGetRequest(Services.referenceDataCache, "GetAllCampuses")

    /**
     * Get list of Academic Groups
     */
    suspend fun getAllAcademicGroups(): NetResponse<Array<AcademicGroup>> =
        makeApiGetRequest(Services.referenceDataCache, "GetAllAcademicGroups")

    /**
     * Get the URL for the banner image for an activity.
     */
    suspend fun getHeaderImageUrlByActivityId(activityId: String): NetResponse<String> =
        makeApiPostRequest(Services.activity, "GetHeaderImageUrlByActivityId", ActivitySummaryByActivityIdRequest(
            activityId
        ))

        /**
     * Download the lesson plan for a class instance
     */
    suspend fun getLessonPlanString(activityLessonPlan: ActivityLessonPlan): NetResponse<String> {
        if (activityLessonPlan.fileAssetId == null) return NetResponse.ClientError(Throwable("The file asset does not exist"))
        return downloadFile(activityLessonPlan.fileAssetId)
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //                                     Flows!                                     //
    ////////////////////////////////////////////////////////////////////////////////////

    private val _schedule: MutableStateFlow<State<Array<ScheduleEntry>>> = MutableStateFlow(State.NotInitiated())
    val schedule: StateFlow<State<Array<ScheduleEntry>>> = _schedule
    private var schedulePollingEnabled = false

    private val _newsfeed: MutableStateFlow<State<List<NewsItem>>> = MutableStateFlow(State.NotInitiated())
    val newsfeed: StateFlow<State<List<NewsItem>>> = _newsfeed
    private var newsfeedPollingEnabled = false

    sealed interface State<T> {
        class NotInitiated<T> : State<T>
        class Loading<T> : State<T>
        data class Error<T>(val error: Throwable) : State<T>
        data class Success<T>(val data: T) : State<T>
    }

    data class RefreshIntervals(
        val schedule: Long = 2 * 60 * 1000,
        val newsfeed: Long = 10 * 60 * 1000,
    )

    sealed class ScheduleEntry(open val event: CalendarEvent) {

        abstract class ActivityEntry(
            override val event: CalendarEvent,
            open var activity: State<Activity> = State.NotInitiated()
        ) : ScheduleEntry(event)
        data class BaseEntry(
            override val event: CalendarEvent
        ) : ScheduleEntry(event) // if we dont know what it is
        data class Lesson(
            override val event: CalendarEvent,
            override var activity: State<Activity> = State.NotInitiated(),
            var lessonPlan: State<String?> = State.NotInitiated()
        ) : ActivityEntry(event, activity)
        data class Event(
            override val event: CalendarEvent,
            override var activity: State<Activity> = State.NotInitiated()
        ) : ActivityEntry(event, activity)
        data class LearningTask(
            override val event: CalendarEvent
        ) : ScheduleEntry(event)
    }

    private suspend fun pollScheduleUpdate(
        startDate: LocalDate,
        endDate: LocalDate = startDate,
        preloadActivities: Boolean = true,
        preloadLessonPlans: Boolean = false
    ) {
        if (schedule is State.Loading<*>) return

        val _reply = getCalendarEventsByUser(startDate, endDate)
        if (_reply !is NetResponse.Success<*>) {
            _schedule.value = State.Error((_reply as NetResponse.Error<*>).error)
            return
        }

        val reply = _reply as NetResponse.Success<Array<CalendarEvent>>
        reply.data.sortByDescending { it.start }
        reply.data.reverse()

        val array = Array(
            reply.data.size
        ) {
            val event = reply.data[it]

            return@Array when (event.activityType) {
                1 -> ScheduleEntry.Lesson(event)
                2 -> ScheduleEntry.Event(event)
                10 -> ScheduleEntry.LearningTask(event)
                else -> {
                    println("Unrecognised activityType ${event.activityType} in event $event")
                    return@Array ScheduleEntry.BaseEntry(event)
                }
            }
        }

        _schedule.value = State.Success(array)

        if (!preloadActivities) return

        array.forEachIndexed { index, it ->
            scope.launch {
                if (
                    it !is ScheduleEntry.Lesson &&
                    it !is ScheduleEntry.Event
                ) return@launch

                ((_schedule.value as State.Success<Array<ScheduleEntry>>).data[index] as ScheduleEntry.ActivityEntry)
                    .activity = State.Loading()

                val activityReply = getLessonsByInstanceIdQuick(it.event.instanceId!!)
                if (activityReply !is NetResponse.Success)
                    return@launch

                // i really hope there's a better way to do this...
                ((_schedule.value as State.Success<Array<ScheduleEntry>>).data[index] as ScheduleEntry.ActivityEntry)
                    .activity = State.Success(activityReply.data)

                if (!preloadLessonPlans || it is ScheduleEntry.Event) return@launch

                loadLessonPlan(index)
            }
        }
    }

    fun loadLessonPlan(scheduleIndex: Int) {
        scope.launch {
            ((_schedule.value as State.Success<Array<ScheduleEntry>>).data[scheduleIndex] as ScheduleEntry.Lesson)
                .lessonPlan = State.Loading()

            val activity = ((_schedule.value as State.Success<Array<ScheduleEntry>>).data[scheduleIndex] as ScheduleEntry.Lesson)
                .activity
            if (activity !is State.Success) return@launch

            val reply = getLessonPlanString(activity.data.lessonPlan)

            ((_schedule.value as State.Success<Array<ScheduleEntry>>).data[scheduleIndex] as ScheduleEntry.Lesson)
                .lessonPlan = if (reply is NetResponse.Success)
                State.Success(reply.data)
            else
                State.Error((reply as NetResponse.Error<*>).error)
        }
    }

    private suspend fun pollNewsfeedUpdate() {
        if (newsfeed is State.Loading<*>) return

        val reply = getMyNewsFeedPaged()
        if (reply is NetResponse.Success<*>)
            @Suppress("UNCHECKED_CAST")
            _newsfeed.value = State.Success((reply.data as DataExtGridDataContainer<NewsItem>).data)
        else
            _newsfeed.value = State.Error((reply as NetResponse.Error<*>).error)
    }

    fun manualPollScheduleUpdate() {
        scope.launch { pollScheduleUpdate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) }
    }

    fun manualPollNewsfeedUpdate() {
        scope.launch { pollNewsfeedUpdate() }
    }

    fun beginPollingSchedule() {
        schedulePollingEnabled = true
        scope.launch {
            while (schedulePollingEnabled) {
                pollScheduleUpdate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
                delay(refreshIntervals.schedule)
            }
        }
    }

    fun endPollingSchedule() {
        schedulePollingEnabled = false
    }

    fun beginPollingNewsfeed() {
        newsfeedPollingEnabled = true
        scope.launch {
            while (newsfeedPollingEnabled) {
                pollNewsfeedUpdate()
                delay(refreshIntervals.newsfeed)
            }
        }
    }

    fun endPollingNewsfeed() {
        newsfeedPollingEnabled = false
    }


}

interface CompassClientCredentials {
    val domain: String
    val userId: Int
    val cookie: String
}