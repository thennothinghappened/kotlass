package org.orca.kotlass.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor = PrimitiveSerialDescriptor("org.orca.utils.LocalDateSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): LocalDate = decoder.decodeString().toLocalDate()
}

object LocalDateNullableSerializer : KSerializer<LocalDate?> {
    override val descriptor = PrimitiveSerialDescriptor("org.orca.utils.LocalDateNullableSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate?) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): LocalDate? {
        return try {
            decoder.decodeString().toLocalDate()
        } catch (error: IllegalArgumentException) {
            null
        }
    }
}

object InstantSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("org.orca.utils.InstantSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant {
        return decoder.decodeString().toInstant()
    }
}

object InstantNullableSerializer : KSerializer<Instant?> {
    override val descriptor = PrimitiveSerialDescriptor("org.orca.utils.InstantNullableSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant?) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant? {
        return try {
            decoder.decodeString().toInstant()
        } catch (error: IllegalArgumentException) {
            null
        }
    }
}