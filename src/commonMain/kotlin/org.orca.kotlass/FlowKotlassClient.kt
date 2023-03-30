package org.orca.kotlass

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.orca.kotlass.data.*
import org.orca.kotlass.IFlowKotlassClient.*

open class FlowKotlassClient(
    credentials: KotlassClient.CompassClientCredentials,
    private val scope: CoroutineScope,
    override val refreshIntervals: RefreshIntervals = RefreshIntervals(),
    proxyIp: String? = null,
    kotlassClient: IKotlassClient = KotlassClient(
        credentials,
        proxyIp
    )
) : IKotlassClient by kotlassClient, IFlowKotlassClient {
    ////////////////////////////////////////////////////////////////////////////////////
    //                                     Flows!                                     //
    ////////////////////////////////////////////////////////////////////////////////////


    override val defaultSchedule = Pollable.Schedule(refreshIntervals.schedule)
    override val defaultNewsfeed = Pollable.Newsfeed(refreshIntervals.newsfeed)
    override val defaultLearningTasks = Pollable.LearningTasks(refreshIntervals.learningTasks)
    override val defaultTaskCategories = Pollable.TaskCategories(refreshIntervals.taskCategories)

    private suspend fun pollScheduleUpdate(
        schedule: Pollable.Schedule = defaultSchedule
    ) {
        if (schedule.state.value is State.Loading) return
        schedule._state.value = State.Loading()

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val _reply = getCalendarEventsByUser(schedule.startDate.value ?: now, schedule.endDate.value ?: now)
        if (_reply !is NetResponse.Success<*>) {
            schedule._state.value = State.Error(_reply as NetResponse.Error<*>)
            return
        }

        val reply = (_reply as NetResponse.Success<List<CalendarEvent>>).data
            .sortedByDescending { it.start }
            .reversed()

        val list = List(
            reply.size
        ) {
            val event = reply[it]

            return@List when (event.activityType) {
                1 -> {
                    val bannerUrl: MutableStateFlow<State<String>> = MutableStateFlow(State.NotInitiated())
                    val activity: MutableStateFlow<State<Activity>> = MutableStateFlow(State.NotInitiated())
                    val lessonPlan: MutableStateFlow<State<String?>> = MutableStateFlow(State.NotInitiated())

                    val entry = ScheduleEntry.Lesson(event, bannerUrl, activity, lessonPlan)

                    if (schedule.preloadBannerUrls)
                        loadBannerUrl(entry)

                    if (schedule.preloadActivities)
                        loadActivity(entry)

                    if (schedule.preloadLessonPlans)
                        loadLessonPlan(entry)

                    return@List entry
                }
                2 -> {
                    val bannerUrl: MutableStateFlow<State<String>> = MutableStateFlow(State.NotInitiated())
                    val activity: MutableStateFlow<State<Activity>> = MutableStateFlow(State.NotInitiated())

                    val entry = ScheduleEntry.Event(event, bannerUrl, activity)

                    if (schedule.preloadBannerUrls)
                        loadBannerUrl(entry)

                    if (schedule.preloadActivities)
                        loadActivity(entry)

                    return@List entry
                }
                7 -> ScheduleEntry.Notice(event)
                10 -> ScheduleEntry.LearningTask(event)
                else -> {
                    println("Unrecognised activityType ${event.activityType} in event $event")
                    return@List ScheduleEntry.BaseEntry(event)
                }
            }
        }

        val learningTasks = list.filterIsInstance<ScheduleEntry.LearningTask>()
        val _notLearningTasks = list.filter { it !is ScheduleEntry.LearningTask }

        schedule._state.value = State.Success(Pollable.Schedule.ScheduleStateHolder(
            allDay = _notLearningTasks.filter { it.event.allDay },
            normal = _notLearningTasks.filter { !it.event.allDay },
            learningTasks = learningTasks
        ))
    }

    override fun loadBannerUrl(scheduleEntry: ScheduleEntry.ActivityEntry) {

        if (scheduleEntry.bannerUrl.value is State.Loading) return
        scheduleEntry._bannerUrl.value = State.Loading()

        scope.launch {

            val bannerUrl = getHeaderImageUrlByActivityId(scheduleEntry.event.activityId)

            if (bannerUrl is NetResponse.Error<*>) {
                scheduleEntry._bannerUrl.value = State.Error(bannerUrl)
                return@launch
            }

            scheduleEntry._bannerUrl.value = State.Success((bannerUrl as NetResponse.Success).data)
        }
    }

    override fun loadActivity(scheduleEntry: ScheduleEntry.ActivityEntry) {

        if (scheduleEntry.activity.value is State.Loading) return
        scheduleEntry._activity.value = State.Loading()

        scope.launch {

            // we know for an ActivityEntry instanceId will always exist.
            val activity = getLessonsByInstanceIdQuick(scheduleEntry.event.instanceId!!)

            if (activity is NetResponse.Error<*>) {
                scheduleEntry._activity.value = State.Error(activity)
                return@launch
            }

            scheduleEntry._activity.value = State.Success((activity as NetResponse.Success).data)
        }
    }

    override fun loadLessonPlan(scheduleEntry: ScheduleEntry.Lesson) {

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
                scheduleEntry._lessonPlan.value = State.Error(lessonPlan)
                return@launch
            }

            scheduleEntry._lessonPlan.value = State.Success((lessonPlan as NetResponse.Success).data)
        }
    }

    private suspend fun pollNewsfeedUpdate(newsfeed: Pollable.Newsfeed = defaultNewsfeed) {
        if (newsfeed.state.value is State.Loading) return
        newsfeed._state.value = State.Loading()

        val reply = getMyNewsFeedPaged()
        if (reply is NetResponse.Success<DataExtGridDataContainer<NewsItem>>)
            newsfeed._state.value = State.Success(reply.data.data.sortedByDescending { it.postDateTime })
        else
            newsfeed._state.value = State.Error(reply as NetResponse.Error<*>)
    }

    private suspend fun pollLearningTasksUpdate(learningTasks: Pollable.LearningTasks = defaultLearningTasks) {
        if (learningTasks.state.value is State.Loading) return
        learningTasks._state.value = State.Loading()

        val reply = getAllLearningTasksByUserId(learningTasks.academicGroup)
        if (reply is NetResponse.Success<DataExtGridDataContainer<LearningTask>>)
            learningTasks._state.value = State.Success(reply.data.data)
        else
            learningTasks._state.value = State.Error(reply as NetResponse.Error<*>)
    }

    private suspend fun pollTaskCategoriesUpdate(taskCategories: Pollable.TaskCategories = defaultTaskCategories) {
        if (taskCategories.state.value is State.Loading) return
        taskCategories._state.value = State.Loading()

        val reply = getAllTaskCategories()
        if (reply is NetResponse.Success<List<TaskCategory>>)
            taskCategories._state.value = State.Success(reply.data)
        else
            taskCategories._state.value = State.Error(reply as NetResponse.Error<*>)
    }

    private suspend fun pollActivityResourcesUpdate(activityResources: Pollable.ActivityResources) {
        if (activityResources.state.value is State.Loading) return
        activityResources._state.value = State.Loading()

        val reply = getActivityAndSubjectResourcesNode(activityResources.activityId.value)
        if (reply is NetResponse.Success<ResourceNode>)
            activityResources._state.value = State.Success(reply.data)
        else
            activityResources._state.value = State.Error(reply as NetResponse.Error<*>)
    }

    private suspend fun pollItem(item: Pollable<*>) =
        when (item) {
            is Pollable.Schedule -> pollScheduleUpdate(item)
            is Pollable.LearningTasks -> pollLearningTasksUpdate(item)
            is Pollable.Newsfeed -> pollNewsfeedUpdate(item)
            is Pollable.TaskCategories -> pollTaskCategoriesUpdate(item)
            is Pollable.ActivityResources -> pollActivityResourcesUpdate(item)
        }

    override fun manualPoll(item: Pollable<*>) =
        scope.launch { pollItem(item) }

    override fun beginPolling(item: Pollable<*>) {
        if (item.pollingEnabled) return
        item.pollingEnabled = true
        scope.launch {
            while (item.pollingEnabled) {
                pollItem(item)
                delay(item.pollRate)
            }
        }
    }

    override fun finishPolling(item: Pollable<*>) {
        item.pollingEnabled = false
    }
}