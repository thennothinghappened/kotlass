package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import org.orca.kotlass.utils.InstantNullableSerializer
import org.orca.kotlass.utils.InstantSerializer

enum class ActionCentreEventAttendanceStatus {
    PENDING,
    ATTENDING,
    AFTER_CONSENT_DATE
}

@Serializable
data class ActionCentreEvent(
    @SerialName("__type") private val dataType: String,
    val additionalContactDetails: String? = null,
    val additionalDetails: String,
    val administrationDetails: String,
    val allowConsentWithoutPayment: Boolean,
    val allowDecline: Boolean,
    val amountPaid: Float,
    val cost: Float,
    val attendeeLimit: Int? = null,
    @SerialName("attendeeStatus") private val _attendeeStatus: Int,
    val canProvideEventConsent: Boolean,
    val confirmedAttendeesCount: Int,
    @Serializable(InstantNullableSerializer::class)
    @SerialName("consentDt") val consentDateTime: Instant?,
    val consentFormId: Unit? = null,
    val consentName: String? = null,
    @Serializable(InstantNullableSerializer::class)
    val consentPaymentDue: Instant?,
    val consentReturnLocation: String? = null,
    val description: String? = null,
    val dressCode: String,
    val educativePurpose: String,
    val eligibleForCSEF: Boolean,
    val expenses: Unit? = null,
    val faculty: String,
    val fimsUnallocatedFunds: Float,
    @Serializable(InstantSerializer::class)
    val start: Instant,
    @Serializable(InstantSerializer::class)
    val finish: Instant,
    val id: Int,
    val isOptIn: Boolean,
    val isParentAttendee: Boolean,
    val isPartiallyPaid: Boolean,
    @SerialName("location") private val _location: JsonElement? = null,
    val medicalDetails: ActionCentreEventContactDetails,
    val name: String,
    val onlineProcessing: Boolean,
    val paidConsentMethod: Int? = null,
    val paidViaCsef: Boolean,
    val paidViaPaymentPlan: Boolean,
    val parentAttendeeId: Int,
    val parentAttendeeName: String? = null,
    @Serializable(InstantNullableSerializer::class)
    @SerialName("paymentDt") val paymentDateTime: Instant?,
    val paymentPlanInstalments: Unit? = null,
    val questions: List<Unit>,
    @Serializable(InstantNullableSerializer::class)
    @SerialName("refundCutoffDt") val refundCutoffDateTime: Instant? = null,
    val refundStatus: Int? = null,
    val requiresConsent: Boolean,
    val requiresPayment: Boolean,
    val sessions: List<ActionCentreEventSession>,
    val showActionPlans: Boolean,
    val studentConsentContent: String,
    val studentContactInfo: List<Unit>,
    val studentEmergencyContacts: List<Unit>,
    val studentId: Int,
    val studentMedicalInfo: List<Unit>,
    val studentName: String,
    val transport: String,
    val useCsef: Boolean,
    val usePaymentPlans: Boolean
) {
    // *really* messy way of dealing with that compass sends this to us as *either* a string or location.
    @Transient private val locationIsString = if (_location is JsonPrimitive) true else if (_location != null) false else null
    @Transient val locationString: String? = if (locationIsString == true) json.decodeFromJsonElement(_location!!) else null
    @Transient val location: Location? = if (locationIsString == false) {
        val l = (_location as JsonObject)
        json.decodeFromJsonElement(_location)
    } else null

    @Transient val attendanceStatus = when(_attendeeStatus) {
        0 -> ActionCentreEventAttendanceStatus.PENDING
        1 -> ActionCentreEventAttendanceStatus.ATTENDING
        2 -> ActionCentreEventAttendanceStatus.AFTER_CONSENT_DATE
        else -> { throw Throwable("Unknown attendance status $_attendeeStatus") }
    }
}

@Serializable
data class ActionCentreEventContactDetails(
    @SerialName("__type") private val dataType: String,
    val additionalMedicalDetails: String? = null
)

@Serializable
data class ActionCentreEventSession(
    @SerialName("__type") private val dataType: String,
    val campusName: String,
    @Serializable(InstantSerializer::class)
    val start: Instant,
    @Serializable(InstantSerializer::class)
    val finish: Instant,
    val instanceId: String,
    @SerialName("location") private val _location: JsonElement? = null,
    val locationComments: String,
) {
    // *really* messy way of dealing with that compass sends this to us as *either* a string or location.
    @Transient private val locationIsString = if (_location is JsonPrimitive) true else if (_location != null) false else null
    @Transient val locationString: String? = if (locationIsString == true) json.decodeFromJsonElement(_location!!) else null
    @Transient val location: Location? = if (locationIsString == false) {
        val l = (_location as JsonObject)
        json.decodeFromJsonElement(_location)
    } else null
}