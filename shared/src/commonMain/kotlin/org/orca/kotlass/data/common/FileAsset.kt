package org.orca.kotlass.data.common

import co.touchlab.kermit.Logger
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Compass identifier for a given file.
 */
@Serializable(with = FileAssetSerializer::class)
sealed interface FileAsset {
    /**
     * Author of this file.
     */
    val author: String?

    /**
     * Description of this file.
     */
    val description: String?

    /**
     * Filename of this file.
     */
    val filename: String?

    /**
     * Readable name of this file.
     */
    val name: String?

    @Serializable
    data class InternalAsset(
        override val author: String? = null,

        override val description: String? = null,

        override val filename: String? = null,

        override val name: String?,

        /**
         * Internal reference ID for this asset to download from Compass.
         */
        @SerialName("fileAssetId")
        val assetId: String
    ) : FileAsset

    @Serializable
    data class ExternalAsset(
        override val author: String? = null,

        override val description: String? = null,

        override val filename: String? = null,

        override val name: String?,

        /**
         * URI to the file location.
         */
        val uri: String
    ) : FileAsset

    /**
     * This exists as Compass sometimes just returns an empty asset instead
     * of just returning a proper `null`.
     */
    @Serializable
    data class NoAsset(
        override val author: String? = null,

        override val description: String? = null,

        override val filename: String? = null,

        override val name: String? = null
    ) : FileAsset
}

/**
 * Compass file assets differentiate between internal IDs and external URIs.
 */
object FileAssetSerializer : JsonContentPolymorphicSerializer<FileAsset>(FileAsset::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FileAsset> =
        element.jsonObject.let {
            if (it["fileAssetId"]?.jsonPrimitive?.contentOrNull != null) {
                return FileAsset.InternalAsset.serializer()
            }

            if (it["uri"]?.jsonPrimitive?.contentOrNull != null) {
                return FileAsset.ExternalAsset.serializer()
            }

            return FileAsset.NoAsset.serializer()
        }
}