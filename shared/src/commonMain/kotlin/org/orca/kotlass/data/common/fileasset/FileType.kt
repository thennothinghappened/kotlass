package org.orca.kotlass.data.common.fileasset

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


/**
 * File type of a Compass asset.
 */
@Serializable(with = FileTypeSerializer::class)
enum class FileType(val index: Int) {
    Unknown(0),
    Folder(1),
    HTMLDocument(2),
    Document(3),
    Link(4)
}

internal object FileTypeSerializer : KSerializer<FileType> {
    override val descriptor = PrimitiveSerialDescriptor("FileTypeSerializer", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): FileType =
        decoder.decodeInt().let { index ->
            FileType.entries.find {
                it.index == index
            } ?: FileType.Unknown
        }

    override fun serialize(encoder: Encoder, value: FileType) {
        encoder.encodeInt(value.index)
    }

}