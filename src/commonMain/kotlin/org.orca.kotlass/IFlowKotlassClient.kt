package org.orca.kotlass

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.orca.kotlass.data.*

/**
 * An extended Kotlass Client with flows.
 */
interface IFlowKotlassClient {

    val refreshIntervals: RefreshIntervals

    val defaultSchedule: Pollable.Schedule
    val defaultNewsfeed: Pollable.Newsfeed
    val defaultLearningTasks: Pollable.LearningTasks
    val defaultTaskCategories: Pollable.TaskCategories

    /**
     * Interval in milliseconds between automatic refreshes when using
     */
    data class RefreshIntervals(
        val schedule: Long = 2 * 60 * 1000,
        val newsfeed: Long = 10 * 60 * 1000,
        val learningTasks: Long = 20 * 60 * 1000,
        val taskCategories: Long = 24 * 60 * 60 * 1000,
        val activityResources: Long = 15 * 60 * 1000
    )

    /**
     * Something that can be 'polled', basically something which has a modifiable state.
     */
    sealed class Pollable<T>(
        internal val _state: MutableStateFlow<State<T>> = MutableStateFlow(
            State.NotInitiated()),
        val state: StateFlow<State<T>> = _state,
        var pollingEnabled: Boolean = false,
        val pollRate: Long
    ) {
        /**
         * Schedule, list of ScheduleEvents, which is a wrapper for CalendarEvents to make them nicer to work with.
         */
        class Schedule(
            pollRate: Long,
            val preloadBannerUrls: Boolean = true,
            val preloadActivities: Boolean = true,
            val preloadLessonPlans: Boolean = false,
            startDate: LocalDate? = null,
            endDate: LocalDate? = startDate
        ) : Pollable<Schedule.ScheduleStateHolder>(pollRate = pollRate) {
            private val _startDate: MutableStateFlow<LocalDate?> = MutableStateFlow(startDate)
            private val _endDate: MutableStateFlow<LocalDate?> = MutableStateFlow(endDate)
            val startDate: StateFlow<LocalDate?> = _startDate
            val endDate: StateFlow<LocalDate?> = _endDate
            fun setDate(startDate: LocalDate, endDate: LocalDate = startDate) {
                _startDate.value = startDate
                _endDate.value = endDate
            }
            class ScheduleStateHolder(
                val normal: List<ScheduleEntry>,
                val allDay: List<ScheduleEntry>,
                val learningTasks: List<ScheduleEntry.LearningTask>
            )
        }

        /**
         * List of NewsItems for the Newsfeed.
         */
        class Newsfeed(pollRate: Long) : Pollable<List<NewsItem>>(pollRate = pollRate)

        /**
         * List of LearningTasks, currently only concerning the whole year's worth. Making that request once rather than
         * redoing it for every subject is nicer since having just one bigger network request tends to be better than a
         * heap of small ones.
         */
        class LearningTasks(
            pollRate: Long,
            val academicGroup: AcademicGroup? = null
        ) : Pollable<Map<Int, List<LearningTask>>>(pollRate = pollRate)

        /**
         * List of TaskCategories, which are used together with learning tasks to define which 'category' they fall under,
         * its name and colour, for sorting. Typically this should only be polled manually once, as there's no reasonable case
         * that needs to be covered for it changing on a day-to-day basis.
         */
        class TaskCategories(
            pollRate: Long
        ) : Pollable<List<TaskCategory>>(pollRate = pollRate)

        /**
         * Resources for a specific activity
         */

        class ActivityResources(
            pollRate: Long,
            activityId: Int
        ) : Pollable<ResourceNode>(pollRate = pollRate) {
            private val _activityId: MutableStateFlow<Int> = MutableStateFlow(activityId)
            val activityId: StateFlow<Int> = _activityId
            fun setActivity(activityId: Int) {
                _activityId.value = activityId
            }
        }
    }

    /**
     * Represents a state for a Pollable.
     */
    sealed interface State<T> {
        /**
         * It has yet to be polled.
         */
        class NotInitiated<T> : State<T>

        /**
         * It is currently waiting for the server.
         */
        class Loading<T> : State<T>

        /**
         * An error was encountered. Holds a NetResponse error, so it can be distinguished if it were a client or network error.
         */
        data class Error<T>(val error: NetResponse.Error<*>) : State<T>

        /**
         * A successful returned value.
         */
        data class Success<T>(val data: T) : State<T>
    }

    /**
     * Wrapper for CalendarEvents to make them easier to work with and bundle their associated other information together.
     */
    sealed class ScheduleEntry(open val event: CalendarEvent) {

        /**
         * Used when the entry is of an unknown type.
         */
        data class BaseEntry(
            override val event: CalendarEvent
        ) : ScheduleEntry(event) // if we dont know what it is

        /**
         * Base class for an entry which has an activity.
         */
        abstract class ActivityEntry(
            override val event: CalendarEvent,
            internal open val _bannerUrl: MutableStateFlow<State<String>>,
            internal open val _activity: MutableStateFlow<State<Activity>>
        ) : ScheduleEntry(event) {
            val bannerUrl: StateFlow<State<String>> by lazy { _bannerUrl }
            val activity: StateFlow<State<Activity>> by lazy { _activity }
        }

        /**
         * A lesson, or class. Has a lesson plan.
         */
        data class Lesson(
            override val event: CalendarEvent,
            override val _bannerUrl: MutableStateFlow<State<String>>,
            override val _activity: MutableStateFlow<State<Activity>>,
            internal val _lessonPlan: MutableStateFlow<State<String?>>
        ) : ActivityEntry(event, _bannerUrl, _activity) {
            val lessonPlan: StateFlow<State<String?>> = _lessonPlan
        }

        /**
         * A Notice, TODO: we don't know what this is actually called internally.
         */
        data class Notice(
            override val event: CalendarEvent
        ) : ScheduleEntry(event)

        /**
         * An Event, such as an excursion or incursion.
         */
        data class Event(
            override val event: CalendarEvent,
            override val _bannerUrl: MutableStateFlow<State<String>>,
            override val _activity: MutableStateFlow<State<Activity>>
        ) : ActivityEntry(event, _bannerUrl, _activity)

        /**
         * A learning task due in the time window for the Schedule. Only has a CalendarEvent - oddly does not link *which*
         * task it refers to, so that must be worked out manually by cross referencing the learning tasks list with its name.
         */
        data class LearningTask(
            override val event: CalendarEvent
        ) : ScheduleEntry(event)
    }

    /**
     * Load the URL for the banner image used for an activity.
     */
    fun loadBannerUrl(scheduleEntry: ScheduleEntry.ActivityEntry)

    /**
     * Load the activity for a CalendarEvent's instanceId
     */
    fun loadActivity(scheduleEntry: ScheduleEntry.ActivityEntry)

    /**
     * Load the lesson plan for a lesson.
     */
    fun loadLessonPlan(scheduleEntry: ScheduleEntry.Lesson)

    /**
     * Manually poll an update for an item once.
     */
    fun manualPoll(item: Pollable<*>): Job

    /**
     * Begin automatically polling an item at its refreshInterval.
     */
    fun beginPolling(item: Pollable<*>)

    /**
     * Stop automatically polling an item.
     */
    fun finishPolling(item: Pollable<*>)

}