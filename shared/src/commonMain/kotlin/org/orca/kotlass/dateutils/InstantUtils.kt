package org.orca.kotlass.dateutils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal fun Instant.toLocalDateTime(): LocalDateTime =
    this.toLocalDateTime(TimeZone.currentSystemDefault())

/**
 * Lenient serializer for [Instant], as Compass sometimes uses
 * empty strings instead of `null` for no value.
 */
internal object InstantLenientSerializer : KSerializer<Instant?> {
    override val descriptor = PrimitiveSerialDescriptor("InstantLenientSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant?) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant? =
        try {
            decoder.decodeString().toInstant()
        } catch (error: IllegalArgumentException) {
            null
        }
}