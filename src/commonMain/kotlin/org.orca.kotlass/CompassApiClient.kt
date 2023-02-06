package org.orca.kotlass

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
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
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orca.kotlass.data.*

open class CompassApiClient(
    private val credentials: CompassClientCredentials,
    protected val scope: CoroutineScope,
    protected val refreshIntervals: RefreshIntervals = RefreshIntervals(),
    protected val proxyIp: String? = null
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
        proxyIp?.let {
            engine {
                proxy = ProxyBuilder.http(proxyIp)
            }
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

    fun buildDomainUrlString(endpoint: String): String = "https://${credentials.domain}$endpoint"

    private fun buildApiRequestUrl(endpoint: String, location: String) = buildDomainUrlString("/Services/${endpoint}.svc/${location}")
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
     * Make sure our credentials are valid
     */
    fun validateCredentials(): Boolean = runBlocking {
        val reply = getCalendarEventsByUser()
        return@runBlocking reply is NetResponse.Success
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
    suspend fun getHeaderImageUrlByActivityId(activityId: Int): NetResponse<String> =
        makeApiPostRequest(Services.activity, "GetHeaderImageUrlByActivityId", ActivitySummaryByActivityIdRequest(
            activityId.toString()
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

    data class RefreshIntervals(
        val schedule: Long = 2 * 60 * 1000,
        val newsfeed: Long = 10 * 60 * 1000,
    )
    abstract class Pollable<T>(
        internal val _state: MutableStateFlow<State<T>> = MutableStateFlow(State.NotInitiated()),
        val state: StateFlow<State<T>> = _state,
        var pollingEnabled: Boolean = false,
        val pollRate: Long
    )

    class Schedule(
        pollRate: Long,
        val preloadBannerUrls: Boolean = true,
        val preloadActivities: Boolean = true,
        val preloadLessonPlans: Boolean = false
    ) : Pollable<List<ScheduleEntry>>(pollRate = pollRate)
    class Newsfeed(pollRate: Long) : Pollable<List<NewsItem>>(pollRate = pollRate)

    val defaultSchedule = Schedule(refreshIntervals.schedule)
    val defaultNewsfeed = Newsfeed(refreshIntervals.newsfeed)


    sealed interface State<T> {
        class NotInitiated<T> : State<T>
        class Loading<T> : State<T>
        data class Error<T>(val error: Throwable) : State<T>
        data class Success<T>(val data: T) : State<T>
    }

    sealed class ScheduleEntry(open val event: CalendarEvent) {

        data class BaseEntry(
            override val event: CalendarEvent
        ) : ScheduleEntry(event) // if we dont know what it is
        abstract class ActivityEntry(
            override val event: CalendarEvent,
            internal open val _bannerUrl: MutableStateFlow<State<String>>,
            internal open val _activity: MutableStateFlow<State<Activity>>
        ) : ScheduleEntry(event) {
            val bannerUrl: StateFlow<State<String>> by lazy { _bannerUrl }
            val activity: StateFlow<State<Activity>> by lazy { _activity }
        }
        data class Lesson(
            override val event: CalendarEvent,
            override val _bannerUrl: MutableStateFlow<State<String>>,
            override val _activity: MutableStateFlow<State<Activity>>,
            internal val _lessonPlan: MutableStateFlow<State<String?>>
        ) : ActivityEntry(event, _bannerUrl, _activity) {
            val lessonPlan: StateFlow<State<String?>> = _lessonPlan
        }
        data class Event(
            override val event: CalendarEvent,
            override val _bannerUrl: MutableStateFlow<State<String>>,
            override val _activity: MutableStateFlow<State<Activity>>
        ) : ActivityEntry(event, _bannerUrl, _activity)
        data class LearningTask(
            override val event: CalendarEvent
        ) : ScheduleEntry(event)
    }

    private suspend fun pollScheduleUpdate(
        startDate: LocalDate,
        endDate: LocalDate = startDate,
        schedule: Schedule = defaultSchedule,
        preloadActivities: Boolean = true,
        preloadBannerUrls: Boolean = true,
        preloadLessonPlans: Boolean = false
    ) {
        if (schedule.state is State.Loading<*>) return

        val _reply = getCalendarEventsByUser(startDate, endDate)
        if (_reply !is NetResponse.Success<*>) {
            schedule._state.value = State.Error((_reply as NetResponse.Error<*>).error)
            return
        }

        val reply = _reply as NetResponse.Success<Array<CalendarEvent>>
        reply.data.sortByDescending { it.start }
        reply.data.reverse()

        val list = List(
            reply.data.size
        ) {
            val event = reply.data[it]

            return@List when (event.activityType) {
                1 -> {
                    val bannerUrl: MutableStateFlow<State<String>> = MutableStateFlow(State.NotInitiated())
                    val activity: MutableStateFlow<State<Activity>> = MutableStateFlow(State.NotInitiated())
                    val lessonPlan: MutableStateFlow<State<String?>> = MutableStateFlow(State.NotInitiated())

                    val entry = ScheduleEntry.Lesson(event, bannerUrl, activity, lessonPlan)

                    if (preloadBannerUrls)
                        loadBannerUrl(entry)

                    if (preloadActivities)
                        loadActivity(entry)

                    if (preloadLessonPlans)
                        loadLessonPlan(entry)

                    return@List entry
                }
                2 -> {
                    val bannerUrl: MutableStateFlow<State<String>> = MutableStateFlow(State.NotInitiated())
                    val activity: MutableStateFlow<State<Activity>> = MutableStateFlow(State.NotInitiated())

                    val entry = ScheduleEntry.Event(event, bannerUrl, activity)

                    if (preloadBannerUrls)
                        loadBannerUrl(entry)

                    if (preloadActivities)
                        loadActivity(entry)

                    return@List entry
                }
                10 -> ScheduleEntry.LearningTask(event)
                else -> {
                    println("Unrecognised activityType ${event.activityType} in event $event")
                    return@List ScheduleEntry.BaseEntry(event)
                }
            }
        }

        schedule._state.value = State.Success(list)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun loadBannerUrl(scheduleEntry: ScheduleEntry.ActivityEntry) {

        if (scheduleEntry.bannerUrl.value is State.Loading) return

        scope.launch {

            scheduleEntry._bannerUrl.value = State.Loading()

            val bannerUrl = getHeaderImageUrlByActivityId(scheduleEntry.event.activityId)

            if (bannerUrl is NetResponse.Error<*>) {
                scheduleEntry._bannerUrl.value = State.Error(bannerUrl.error)
                return@launch
            }

            scheduleEntry._bannerUrl.value = State.Success((bannerUrl as NetResponse.Success).data)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun loadActivity(scheduleEntry: ScheduleEntry.ActivityEntry) {

        if (scheduleEntry.activity.value is State.Loading) return

        scope.launch {

            // we know for an ActivityEntry instanceId will always exist.
            val activity = getLessonsByInstanceIdQuick(scheduleEntry.event.instanceId!!)

            if (activity is NetResponse.Error<*>) {
                scheduleEntry._activity.value = State.Error(activity.error)
                return@launch
            }

            scheduleEntry._activity.value = State.Success((activity as NetResponse.Success).data)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun loadLessonPlan(scheduleEntry: ScheduleEntry.Lesson) {

        if (scheduleEntry.lessonPlan.value is State.Loading) return

        scope.launch {

            scheduleEntry._lessonPlan.value = State.Loading()

            // We need the activity to get the lesson plan's file ID
            if (scheduleEntry.activity.value is State.NotInitiated) {
                loadActivity(scheduleEntry)
            }

            while (scheduleEntry.activity.value is State.Loading) {
                delay(10L)
            }

            if (scheduleEntry.activity.value is State.Error) {
                scheduleEntry._lessonPlan.value = State.Error((scheduleEntry.activity.value as State.Error<*>).error)
                return@launch
            }

            val activity = (scheduleEntry.activity.value as State.Success)

            if (activity.data.lessonPlan.fileAssetId == null) {
                scheduleEntry._lessonPlan.value = State.Success(null)
                return@launch
            }

            val lessonPlan = getLessonPlanString(activity.data.lessonPlan)

            if (lessonPlan is NetResponse.Error<*>) {
                scheduleEntry._lessonPlan.value = State.Error(lessonPlan.error)
                return@launch
            }

            scheduleEntry._lessonPlan.value = State.Success((lessonPlan as NetResponse.Success).data)
        }
    }

    private suspend fun pollNewsfeedUpdate(newsfeed: Newsfeed = defaultNewsfeed) {
        if (newsfeed.state is State.Loading<*>) return

        val reply = getMyNewsFeedPaged()
        if (reply is NetResponse.Success<DataExtGridDataContainer<NewsItem>>)
            newsfeed._state.value = State.Success(reply.data.data.sortedByDescending { it.postDateTime })
        else
            newsfeed._state.value = State.Error((reply as NetResponse.Error<*>).error)
    }

    fun manualPollScheduleUpdate(
        startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        endDate: LocalDate = startDate,
        schedule: Schedule = defaultSchedule
    ) {
        scope.launch { pollScheduleUpdate(startDate, endDate, schedule) }
    }

    fun manualPollNewsfeedUpdate(newsfeed: Newsfeed = defaultNewsfeed) {
        scope.launch { pollNewsfeedUpdate(newsfeed) }
    }

    fun beginPollingSchedule(
        schedule: Schedule = defaultSchedule
    ) { if (schedule.pollingEnabled) return
        schedule.pollingEnabled = true
        scope.launch {
            while (schedule.pollingEnabled) {
                pollScheduleUpdate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
                delay(refreshIntervals.schedule)
            }
        }
    }

    fun endPollingSchedule(schedule: Schedule = defaultSchedule) {
        schedule.pollingEnabled = false
    }

    fun beginPollingNewsfeed(newsfeed: Newsfeed = defaultNewsfeed) {
        if (newsfeed.pollingEnabled) return
        newsfeed.pollingEnabled = true
        scope.launch {
            while (newsfeed.pollingEnabled) {
                pollNewsfeedUpdate()
                delay(refreshIntervals.newsfeed)
            }
        }
    }

    fun endPollingNewsfeed(newsfeed: Newsfeed = defaultNewsfeed) {
        newsfeed.pollingEnabled = false
    }


}

interface CompassClientCredentials {
    val domain: String
    val userId: Int
    val cookie: String
}