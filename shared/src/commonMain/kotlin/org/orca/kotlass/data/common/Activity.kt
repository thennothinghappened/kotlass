package org.orca.kotlass.data.common

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Compass representation of a given activity, which has a list
 * of instances. Activities are generally a [Class], but others exist.
 */
@Serializable(with = ActivitySerializer::class)
sealed interface Activity {

    /**
     * Name of this activity.
     */
    @SerialName("ActivityDisplayName")
    val name: String

    /**
     * Unique ID of this activity.
     */
    @SerialName("ActivityId")
    val id: Int

    /**
     * List of [ActivityInstance]s belonging to this Activity.
     */
    @SerialName("Instances")
    val instances: List<ActivityInstance>

    /**
     * A given Class instance which has lessons.
     */
    @Serializable
    data class Class(
        @SerialName("ActivityDisplayName")
        override val name: String,

        @SerialName("ActivityId")
        override val id: Int,

        @SerialName("Instances")
        override val instances: List<ActivityInstance.ClassInstance>,

        /**
         * Readable name of this subject.
         */
        @SerialName("SubjectName")
        val subjectName: String,
    ) : Activity

}

@Serializable
sealed interface ActivityInstance {

    /**
     * Short 'code' name of the parent [Activity].
     */
    @SerialName("ActivityDisplayName")
    val name: String

    /**
     * Unique ID of the parent [Activity].
     */
    @SerialName("ActivityId")
    val id: Int

    /**
     * Activity Instance of a given Class - i.e. a lesson, which
     * may have a lesson plan, and has a subject name.
     */
    @Serializable
    data class ClassInstance(
        @SerialName("ActivityDisplayName")
        override val name: String,

        @SerialName("ActivityId")
        override val id: Int,

        /**
         * Readable name of the subject this instance belongs to.
         */
        @SerialName("SubjectName")
        val subjectName: String,

        /**
         * [FileAsset] pointing to the location of the lesson plan download.
         */
        @SerialName("lp")
        val lessonPlanFile: FileAsset,
    ) : ActivityInstance
}

/**
 * Compass differentiates activities by a series of "`Is...`" boolean properties.
 */
object ActivitySerializer : JsonContentPolymorphicSerializer<Activity>(Activity::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Activity> =
        element.jsonObject.let {
            if (it["IsStandardClass"]?.jsonPrimitive?.booleanOrNull == true) {
                return Activity.Class.serializer()
            }

            throw IllegalArgumentException("Activity belongs to no activity type!")
        }
}

/**
 * Request for getting a given [Activity] by its [Activity.id].
 */
@Serializable
internal data class CompassGetActivityById(
    val activityId: Int
)

/**
 * Request for getting a given [Activity] by an [instanceId] belonging to it.
 */
@Serializable
internal data class CompassGetActivityByInstanceId(
    val instanceId: String
)