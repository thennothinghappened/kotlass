package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.utils.InstantNullableSerializer
import org.orca.kotlass.utils.LocalDateSerializer

/**
 * Data to send to get the list of CalendarEvents
 */
@Serializable
data class CalendarEventsRequest(
    val userId: Int,
    @Serializable(LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val endDate: LocalDate = startDate,
    val homePage: Boolean = true,
    val start: Int = 0,
    val limit: Int = 25,
    val page: Int = 1,

    val locationId: Int? = null,
    val activityId: Int? = null,
    val staffIds: Int? = null
)

/**
 * Singular Calendar Event such as a class or activity
 */
// there are a lot of things which are
// of type unit here, because I don't know what
// their data type is normally or their use.
@Serializable
data class CalendarEvent(
    @SerialName("__type") private val dataType: String,
    val activityId: Int,
    val activityType: Int,
    val backgroundColor: String,
    val description: String,
    @Serializable(InstantNullableSerializer::class)
    val start: Instant?,
    @Serializable(InstantNullableSerializer::class)
    val finish: Instant?,
    val instanceId: String? = null,
    val title: String,
    val longTitleWithoutTime: String,
    val managerId: Int? = null,
    private val activityImportIdentifier: Unit? = null,
    private val allDay: Boolean,
    private val attendanceMode: Int,
    private val attendeeUserId: Int,
    private val calendarId: Unit? = null,
    private val categoryIds: Unit? = null,
    private val comment: String? = null,
    private val eventSetupStatus: Int? = null,
    private val guid: String,
    private val inClassStatus: Unit? = null,
    private val isRecurring: Boolean,
    private val location: Unit? = null,
    private val longTitle: String? = null,
    private val minutesMeetingId: Unit? = null,
    private val recurringStart: String? = null,
    private val recurringFinish: String? = null,
    private val repeatDays: Unit? = null,
    private val repeatForever: Boolean,
    private val repeatFrequency: Int,
    private val repeatUntil: String? = null,
    private val rollMarked: Boolean,
    private val runningStatus: Int,
    private val targetStudentId: Int? = null,
    private val teachingDaysOnly: Boolean,
    private val unavailablePd: Unit? = null
)