package org.orca.kotlass.data.calendar

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = CalendarEvent.CalendarEventSerializer::class)
sealed interface CalendarEvent {

    /**
     * Whether the event lasts the entire day.
     */
    val allDay: Boolean

    /**
     * Which student ID the event is for, or null for all.
     */
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
     * Calendar Event which has an instance associated with it.
     * Technically, all do internally, but only [Lesson] seems to use
     * it for the current types encountered.
     */
    @Serializable
    sealed interface Instanced : CalendarEvent {
        val activityId: Int
        val instanceId: String
    }

    /**
     * A lesson belonging to a given [activityId], with a given [instanceId].
     */
    @Serializable
    data class Lesson(
        override val allDay: Boolean,

        override val targetStudentId: Int?,

        override val activityId: Int,

        override val instanceId: String,

        @SerialName("title")
        override val shortName: String,

        @SerialName("longTitleWithoutTime")
        override val name: String,

        /**
         * Whether this lesson currently has a lesson plan.
         */
        @SerialName("lessonPlanConfigured")
        val hasLessonPlan: Boolean,
    ) : Instanced

    @Serializable
    data class Event(
        override val allDay: Boolean,

        override val targetStudentId: Int?,

        @SerialName("title")
        override val shortName: String,

        @SerialName("longTitleWithoutTime")
        override val name: String,
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
        override val allDay: Boolean,

        override val targetStudentId: Int?,

        @SerialName("title")
        override val shortName: String,

        @SerialName("longTitleWithoutTime")
        override val name: String,
    ) : CalendarEvent

    /**
     * Learning task displayed on the Calendar as due.
     */
    @Serializable
    data class LearningTask(
        override val allDay: Boolean,

        override val targetStudentId: Int?,

        @SerialName("title")
        override val shortName: String,

        @SerialName("longTitleWithoutTime")
        override val name: String,

        val learningTaskId: Int
    ) : CalendarEvent

    /**
     * Compass uses the `activityType` field to determine what type of [CalendarEvent] to use
     * for a specific event.
     */
    object CalendarEventSerializer : JsonContentPolymorphicSerializer<CalendarEvent>(CalendarEvent::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out CalendarEvent> =
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