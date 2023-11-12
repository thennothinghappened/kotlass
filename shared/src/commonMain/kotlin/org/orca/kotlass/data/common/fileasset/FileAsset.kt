package org.orca.kotlass.data.common.fileasset

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Compass identifier for a given file.
 */
@Serializable(with = FileAsset.FileAssetSerializer::class)
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
    @SerialName("filename")
    val fileName: String?

    /**
     * Readable name of this file.
     */
    val name: String?

    /**
     * Unique ID for this asset if it is a resource in Compass Resources.
     */
    @SerialName("wikiNodeId")
    val resourceId: Int?

    /**
     * File type of this asset.
     */
    @SerialName("wikiNodeType")
    val fileType: FileType?

    @Serializable
    data class InternalAsset(
        override val author: String? = null,

        override val description: String? = null,

        @SerialName("filename")
        override val fileName: String? = null,

        override val name: String?,

        @SerialName("wikiNodeId")
        override val resourceId: Int? = null,

        @SerialName("wikiNodeType")
        override val fileType: FileType? = null,

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

        @SerialName("filename")
        override val fileName: String? = null,

        override val name: String?,

        @SerialName("wikiNodeId")
        override val resourceId: Int? = null,

        @SerialName("wikiNodeType")
        override val fileType: FileType? = null,

        /**
         * URI to the file location.
         */
        val uri: String
    ) : FileAsset

    /**
     * Pointer to a resource in Compass Resources.
     */
    @Serializable
    data class ResourcesAsset(
        override val author: String? = null,

        override val description: String? = null,

        @SerialName("filename")
        override val fileName: String? = null,

        override val name: String? = null,

        @SerialName("wikiNodeId")
        override val resourceId: Int,

        @SerialName("wikiNodeType")
        override val fileType: FileType,
    ) : FileAsset

    /**
     * This exists as Compass sometimes just returns an empty asset instead
     * of just returning a proper `null`.
     */
    @Serializable
    data class NoAsset(
        override val author: String? = null,

        override val description: String? = null,

        @SerialName("filename")
        override val fileName: String? = null,

        override val name: String? = null,

        @SerialName("wikiNodeId")
        override val resourceId: Int? = null,

        @SerialName("wikiNodeType")
        override val fileType: FileType? = null,
    ) : FileAsset

    /**
     * Compass file assets differentiate between internal IDs and external URIs.
     */
    object FileAssetSerializer : JsonContentPolymorphicSerializer<FileAsset>(FileAsset::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out FileAsset> =
            element.jsonObject.let {
                if (it["fileAssetId"]?.jsonPrimitive?.contentOrNull != null) {
                    return InternalAsset.serializer()
                }

                if (it["uri"]?.jsonPrimitive?.contentOrNull != null) {
                    return ExternalAsset.serializer()
                }

                if (it["wikiNodeId"]?.jsonPrimitive?.contentOrNull != null) {
                    return ResourcesAsset.serializer()
                }

                return NoAsset.serializer()
            }
    }
}