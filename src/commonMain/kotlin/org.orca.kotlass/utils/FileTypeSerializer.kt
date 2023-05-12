package org.orca.kotlass.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.orca.kotlass.data.FileType

// Order for the enum
private val _fileTypes = listOf(
    FileType.Unknown,
    FileType.Folder,
    FileType.HTMLDocument,
    FileType.Document,
    FileType.Link
)

object FileTypeSerializer : KSerializer<FileType> {
    override val descriptor = PrimitiveSerialDescriptor("org.orca.utils.FileTypeSerializer", PrimitiveKind.INT)
    override fun serialize(encoder: Encoder, value: FileType) = encoder.encodeInt(_fileTypes.indexOf(value))
    override fun deserialize(decoder: Decoder): FileType {
        val index = decoder.decodeInt()

        if (index >= _fileTypes.size) return FileType.Unknown

        return  _fileTypes[index]
    }
}