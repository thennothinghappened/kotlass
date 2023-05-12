package org.orca.kotlass.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// adding & removing 0xFF000000 is needed because this will most likely be used with compose and it makes that work properly
// may remove later and transfer that responsibility to those apps
object ColourSerializer : KSerializer<Long> {
    override val descriptor = PrimitiveSerialDescriptor("org.orca.utils.ColourSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Long) = encoder.encodeString("#${(value - 0xFF000000)
        .toString(16)
        .padStart(6, '0')}")
    override fun deserialize(decoder: Decoder): Long = decoder.decodeString().removePrefix("#").toLong(16) or 0xFF000000
}