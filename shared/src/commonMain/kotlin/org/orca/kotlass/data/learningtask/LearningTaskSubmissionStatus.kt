package org.orca.kotlass.data.learningtask

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Status of a given [LearningTaskTargetStudent]'s submissions
 * to a [LearningTask].
 */
@Serializable(with = LearningTaskSubmissionStatusSerializer::class)
enum class LearningTaskSubmissionStatus(val index: Int) {
    Pending(1),
    Overdue(2),
    SubmittedOnTime(3),
    SubmittedLate(4)
}

internal object LearningTaskSubmissionStatusSerializer : KSerializer<LearningTaskSubmissionStatus> {
    override val descriptor = PrimitiveSerialDescriptor("FileTypeSerializer", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): LearningTaskSubmissionStatus =
        decoder.decodeInt().let { index ->
            LearningTaskSubmissionStatus.entries.find {
                it.index == index
            } ?: throw IllegalArgumentException("Unknown submission status number $index")
        }

    override fun serialize(encoder: Encoder, value: LearningTaskSubmissionStatus) {
        encoder.encodeInt(value.index)
    }

}