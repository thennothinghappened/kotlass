package org.orca.kotlass.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object InstantSerializer : KSerializer<Instant?> {
    override val descriptor = PrimitiveSerialDescriptor("org.orca.data.InstantSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant?) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant? {
        return try {
            decoder.decodeString().toInstant()
        } catch (error: IllegalArgumentException) {
            null
        }
    }
}