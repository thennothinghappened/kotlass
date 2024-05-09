package org.orca.kotlass.data.common

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

/**
 * Compass uses the term 'Manager' to refer to teachers, or any other people
 * in charge of an [Activity], [CalendarEvent] etc.
 */
@Serializable(with = ManagerSerializer::class)
sealed interface Manager {

    /**
     * Readable name of this manager.
     */
    @SerialName("ManagerName")
    val name: String?

    /**
     * Path to the display photo for this manager.
     * Note that this is *not* an asset ID, but generally points
     * to the full (on server) path of one anyway.
     */
    @SerialName("ManagerPhotoPath")
    val photoPath: String?

    /**
     * Unique ID for this manager.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("ManagerUserId")
    @JsonNames("ManagerUserId", "managerUserId")
    val id: Int

    @Serializable
    data class NormalManager(

        @SerialName("ManagerName")
        override val name: String? = null,

        @SerialName("ManagerPhotoPath")
        override val photoPath: String? = null,

        @OptIn(ExperimentalSerializationApi::class)
        @SerialName("ManagerUserId")
        @JsonNames("ManagerUserId", "managerUserId")
        override val id: Int

    ) : Manager

    @Serializable
    data class CoveredManager(

        @SerialName("ManagerName")
        override val name: String,

        @SerialName("ManagerPhotoPath")
        override val photoPath: String,

        @OptIn(ExperimentalSerializationApi::class)
        @SerialName("ManagerUserId")
        @JsonNames("ManagerUserId", "managerUserId")
        override val id: Int,

        @SerialName("CoveringName")
        val coveringName: String? = null,

        @SerialName("CoveringPhotoPath")
        val coveringPhotoPath: String? = null,

        @SerialName("CoveringUserId")
        val coveringId: Int,

    ) : Manager

}

object ManagerSerializer : JsonContentPolymorphicSerializer<Manager>(Manager::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Manager> =
        element.jsonObject.let {
            if (it["CoveringUserId"]?.jsonPrimitive?.contentOrNull != null) {
                return@let Manager.CoveredManager.serializer()
            }

            Manager.NormalManager.serializer()
        }
}