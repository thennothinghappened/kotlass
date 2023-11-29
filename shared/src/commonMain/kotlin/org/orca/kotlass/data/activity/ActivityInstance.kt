package org.orca.kotlass.data.activity

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.orca.kotlass.data.common.fileasset.FileAsset
import org.orca.kotlass.data.common.LocationContainer
import org.orca.kotlass.data.common.Manager

@Serializable(with = ActivityInstance.ActivityInstanceSerializer::class)
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
     * List of locations for this instance.
     *
     * TODO: in what case can there be multiple?
     */
    @SerialName("locations")
    val locations: List<LocationContainer>

    /**
     * List of [Manager]s for this instance.
     *
     * TODO: in what case can there be multiple?
     */
    @SerialName("managers")
    val managers: List<Manager>

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

        @SerialName("locations")
        override val locations: List<LocationContainer>,

        @SerialName("managers")
        override val managers: List<Manager>,

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

    /**
     * Compass differentiates activities by a series of "`Is...`" boolean properties.
     */
    object ActivityInstanceSerializer : JsonContentPolymorphicSerializer<ActivityInstance>(ActivityInstance::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ActivityInstance> =
            element.jsonObject.let {
                if (it["IsStandardClass"]?.jsonPrimitive?.booleanOrNull == true) {
                    return ClassInstance.serializer()
                }

                throw IllegalArgumentException("Activity belongs to no activity type!")
            }
    }
}