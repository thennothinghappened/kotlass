package org.orca.kotlass.data.calendar

import kotlinx.datetime.Instant
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.orca.kotlass.data.common.Manager

@Serializable(with = CalendarEvent.CalendarEventSerializer::class)
sealed interface CalendarEvent {

    /**
     * Whether the event lasts the entire day.
     */
    @SerialName("allDay")
    val allDay: Boolean

    /**
     * Timestamp of the start of this event.
     */
    @SerialName("start")
    val start: Instant

    /**
     * Timestamp of the finish time of this event.
     */
    @SerialName("finish")
    val finish: Instant

    /**
     * Which student ID the event is for, or null for all.
     */
    @SerialName("targetStudentId")
    val targetStudentId: Int?

    /**
     * Short name of the event, though Compass calls it `title`.
     */
    @SerialName("title")
    val shortName: String

    /**
     * Long name of the event, though Compass calls it `longTitleWithoutTime`.
     *
     * There is a matching `longTitle`, but it includes the UTC time in the string,
     * which isn't very helpful.
     */
    @SerialName("longTitleWithoutTime")
    val name: String

    /**
     * List of managers for this event.
     */
    @SerialName("managers")
    val managers: List<Manager>

    /**
     * Calendar Event which has an instance associated with it.
     * Technically, all do internally, but only [Lesson] seems to use
     * it for the current types encountered.
     */
    @Serializable
    sealed interface Instanced : CalendarEvent {
        @SerialName("activityId")
        val activityId: Int

        @SerialName("instanceId")
        val instanceId: String
    }

    /**
     * A lesson belonging to a given [activityId], with a given [instanceId].
     */
    @Serializable
    data class Lesson(
        @SerialName("allDay")
        override val allDay: Boolean,

        @SerialName("start")
        override val start: Instant,

        @SerialName("finish")
        override val finish: Instant,

        @SerialName("targetStudentId")
        override val targetStudentId: Int?,

        @SerialName("activityId")
        override val activityId: Int,

        @SerialName("instanceId")
        override val instanceId: String,

        @SerialName("title")
        override val shortName: String,

        @SerialName("longTitleWithoutTime")
        override val name: String,

        @SerialName("managers")
        override val managers: List<Manager>,

        /**
         * Whether this lesson currently has a lesson plan.
         */
        @SerialName("lessonPlanConfigured")
        val hasLessonPlan: Boolean,
    ) : Instanced

    @Serializable
    data class Event(
        @SerialName("allDay")
        override val allDay: Boolean,

        @SerialName("start")
        override val start: Instant,

        @SerialName("finish")
        override val finish: Instant,

        @SerialName("targetStudentId")
        override val targetStudentId: Int?,

        @SerialName("title")
        override val shortName: String,

        @SerialName("longTitleWithoutTime")
        override val name: String,

        @SerialName("managers")
        override val managers: List<Manager>
    ) : CalendarEvent

    /**
     * TODO: Working name for this type, as it isn't named on Compass.
     * A 'Notice' is a Calendar entry which doesn't have a proper associated
     * activity or instance, it holds minimal data.
     *
     * Currently, we're associating this type with multiple types that seem
     * to serve the same role.
     */
    @Serializable
    data class Notice(
        @SerialName("allDay")
        override val allDay: Boolean,

        @SerialName("start")
        override val start: Instant,

        @SerialName("finish")
        override val finish: Instant,

        @SerialName("targetStudentId")
        override val targetStudentId: Int?,

        @SerialName("title")
        override val shortName: String,

        @SerialName("longTitleWithoutTime")
        override val name: String,

        @SerialName("managers")
        override val managers: List<Manager>,
    ) : CalendarEvent

    /**
     * Learning task displayed on the Calendar as due.
     */
    @Serializable
    data class LearningTask(
        @SerialName("allDay")
        override val allDay: Boolean,

        @SerialName("start")
        override val start: Instant,

        @SerialName("finish")
        override val finish: Instant,

        @SerialName("targetStudentId")
        override val targetStudentId: Int?,

        @SerialName("title")
        override val shortName: String,

        @SerialName("longTitleWithoutTime")
        override val name: String,

        @SerialName("managers")
        override val managers: List<Manager>,

        @SerialName("learningTaskId")
        val learningTaskId: Int
    ) : CalendarEvent

    /**
     * Compass uses the `activityType` field to determine what type of [CalendarEvent] to use
     * for a specific event.
     */
    object CalendarEventSerializer : JsonContentPolymorphicSerializer<CalendarEvent>(CalendarEvent::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<CalendarEvent> =
            when (val it = element.jsonObject["activityType"]?.jsonPrimitive?.int) {
                1 -> Lesson.serializer()
                2 -> Event.serializer()
                5 -> Notice.serializer()
                7 -> Notice.serializer()
                8 -> Notice.serializer()
                10 -> LearningTask.serializer()
                else -> throw IllegalArgumentException("Unknown CalendarEvent activityType '$it'!")
            }
    }
}
