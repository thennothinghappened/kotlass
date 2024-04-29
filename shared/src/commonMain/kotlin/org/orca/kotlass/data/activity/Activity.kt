package org.orca.kotlass.data.activity

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
@Serializable(with = Activity.ActivitySerializer::class)
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

    /**
     * Compass differentiates activities by a series of "`Is...`" boolean properties.
     */
    object ActivitySerializer : JsonContentPolymorphicSerializer<Activity>(Activity::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Activity> =
            element.jsonObject.let {
                if (it["IsStandardClass"]?.jsonPrimitive?.booleanOrNull == true) {
                    return Class.serializer()
                }

                throw IllegalArgumentException("Activity belongs to no activity type!")
            }
    }
}