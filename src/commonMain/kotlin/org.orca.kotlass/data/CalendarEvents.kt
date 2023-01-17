package org.orca.kotlass.data

import io.ktor.client.plugins.cache.storage.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data to send to get the newsfeed
 */
@Serializable
data class CalendarEventsRequest(
    val userId: Int,
    val startDate: String,
    val endDate: String = startDate,
    val homePage: Boolean = true,
    val start: Int = 0,
    val limit: Int = 25,
    val page: Int = 1,

    val locationId: Int? = null,
    val activityId: Int? = null,
    val staffIds: Int? = null
)

/**
 * Data type received from getCalendarEventsByUser,
 * Contains array of CalendarEvents
 */
@Serializable
data class CalendarEventList(override val h: String? = null, override val d: Array<CalendarEvent>? = null) : CData

/**
 * Singular Calendar Event such as a class or activity
 */
// there are a lot of things which are
// of type unit here, because I don't know what
// their data type is normally or their use.
@Serializable
data class CalendarEvent(
    @SerialName("__type") val dataType: String,
    val activityId: Int,
    val activityImportIdentifier: Unit? = null,
    val activityType: Int,
    val allDay: Boolean,
    val attendanceMode: Int,
    val attendeeUserId: Int,
    val backgroundColor: String,
    val calendarId: Unit? = null,
    val categoryIds: Unit? = null,
    val comment: String? = null,
    val description: String,
    val eventSetupStatus: Unit? = null,
    val start: String,
    val finish: String,
    val guid: String,
    val inClassStatus: Unit? = null,
    val instanceId: String,
    val isRecurring: Boolean,
    val location: Unit? = null,
    val title: String,
    val longTitle: String,
    val longTitleWithoutTime: String,
    val managerId: Int,
    val minutesMeetingId: Unit? = null,
    val recurringStart: String? = null,
    val recurringFinish: String? = null,
    val repeatDays: Unit? = null,
    val repeatForever: Boolean,
    val repeatFrequency: Int,
    val repeatUntil: String? = null,
    val rollMarked: Boolean,
    val runningStatus: Int,
    val targetStudentId: Int,
    val teachingDaysOnly: Boolean,
    val unavailablePd: Unit? = null
)