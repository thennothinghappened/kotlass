package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.utils.InstantSerializer

/**
 * Data to send to get the ActivitySummary by instanceId
 */
@Serializable
data class ActivitySummaryByInstanceIdRequest(
    val instanceId: String
)

/**
 * Data to send to get the ActivitySummary by activityId
 */
@Serializable
data class ActivitySummaryByActivityIdRequest(
    val activityId: String
)

/**
 * Contains information about a specific class or "activity".
 */
@Serializable
data class ActivitySummary(
    @SerialName("__type") private val dataType: String,
    @SerialName("AcademicYearLevel") val academicYearLevel: String,
    @SerialName("ActivityDefaultAttendanceMode") private val activityDefaultAttendanceMode: Int,
    @SerialName("ActivityDefaultCampusId") private val activityDefaultCampusId: Int? = null,
    @SerialName("ActivityDefaultCustomLocation") private val activityDefaultCustomLocation: Int? = null,
    @SerialName("ActivityDefaultLocationId") private val activityDefaultLocationId: Int? = null,
    @SerialName("ActivityDisplayName") val activityDisplayName: String,
    @SerialName("ActivityFinish") private val activityFinish: String? = null,
    @SerialName("ActivityId") val activityId: Int,
    @SerialName("ActivityImportIdentifier") private val activityImportIdentifier: String,
    @SerialName("ActivityManagerId") val activityManagerId: String,
    @SerialName("ActivityPermissions") private val activityPermissions: ActivityPermissions,
    @SerialName("ActivitySingular") val activitySingular: String,
    @SerialName("ActivityStart") private val activityStart: String? = null,
    @SerialName("ExtendedStatusId") private val extendedStatusId: Int,
    @SerialName("FacultyId") private val facultyId: Int,
    @SerialName("FacultyManagerIds") private val facultyManagerIds: Array<Int>,
    @SerialName("Instances") val instances: Array<Activity>,
    @SerialName("IsBusRoute") val isBusRoute: Boolean,
    @SerialName("IsExam") val isExam: Boolean,
    @SerialName("IsLearningTasksAdmin") val isLearningTasksAdmin: Boolean,
    @SerialName("IsMeeting") val isMeeting: Boolean,
    @SerialName("IsSchoolApproval") val isSchoolApproval: Boolean,
    @SerialName("IsStandardClass") val isStandardClass: Boolean,
    @SerialName("IsYardDuty") val isYardDuty: Boolean,
    @SerialName("RollTapThreshold") private val rollTapThreshold: Int,
    @SerialName("SubjectCoordinatorId") private val subjectCoordinatorId: String? = null,
    @SerialName("SubjectId") private val subjectId: Int,
    @SerialName("SubjectName") val subjectName: String,
    @SerialName("SubjectShortName") val subjectShortName: String
)

/**
 * Contains information about if you have permission to edit the activity
 */
@Serializable
data class ActivityPermissions(
    @SerialName("__type") private val dataType: String,
    val canDelete: Boolean,
    val canEdit: Boolean
)

/**
 * An instance of an Activity (class session)
 */
@Serializable
data class Activity(
    @SerialName("__type") private val dataType: String,
    @SerialName("ActivityDisplayName") val activityDisplayName: String,
    @SerialName("ActivityId") val activityId: String,
    @SerialName("ActivityManagerId") val activityManagerId: Int,
    @SerialName("ActivitySingular") val activitySingular: String,
    @SerialName("AttendeeCount") val attendeeCount: Int,
    @SerialName("AttendeeUserIdList") val attendeeUserIdList: Array<Int>,
    @SerialName("CampusId") val campusId: Int,
    @SerialName("CurrentInstance") val currentInstance: Boolean,
    @SerialName("FutureInstance") val futureInstance: Boolean,
    @SerialName("InstancePlural") val instancePlural: String,
    @SerialName("InstanceSingular") val instanceSingular: String,
    @SerialName("IsBusRoute") val isBusRoute: Boolean,
    @SerialName("IsExam") val isExam: Boolean,
    @SerialName("IsMeeting") val isMeeting: Boolean,
    @SerialName("IsSchoolApproval") val isSchoolApproval: Boolean,
    @SerialName("IsStandardClass") val isStandardClass: Boolean,
    @SerialName("IsYardDuty") val isYardDuty: Boolean,
    @SerialName("l") val locationName: String,
    @SerialName("ManagerPhotoPath") val managerPhotoPath: String,
    @SerialName("ManagerTextReadable") val managerTextReadable: String,
    @SerialName("PastInstance") val pastInstance: Boolean,
    @SerialName("RunningStatus") val runningStatus: Boolean,
    @SerialName("SubjectName") val subjectName: String,
    @SerialName("SubjectShortname") val subjectShortName: String,
    @SerialName("UpcomingInstance") val upcomingInstance: Boolean, //todo: duplicate of FutureInstance?
    @Serializable(InstantSerializer::class)
    @SerialName("st") val start: Instant?,
    @Serializable(InstantSerializer::class)
    @SerialName("fn") val finish: Instant?,
    @SerialName("id") val id: String,
    @SerialName("CoveringIid") val coveringIid: String? = null,
    @SerialName("CoveringPhotoPath") val coveringPhotoId: String? = null,
    @SerialName("CoveringUid") val coveringUid: Int,
    @SerialName("ActivityImportIdentifier") private val activityImportIdentifier: String,
    @SerialName("AttendanceMode") private val attendanceMode: Int,
    @SerialName("AttendeeLimit") private val attendeeLimit: Int? = null,
    @SerialName("ExtendedStatusId") private val extendedStatusId: Int,
    @SerialName("LocationDetails") private val locationDetails: Location,
    @SerialName("LocationId") private val locationId: Int,
    @SerialName("locations") private val locations: Array<LocationDetailsContainer>, // very redundant... big waste of memory
    @SerialName("ReadableAttendeeCount") private val readableAttendeeCount: String,
    @SerialName("SubjectId") private val subjectId: String,
    @SerialName("UserCanCancelOrDelete") private val userCanCancelOrDelete: Boolean,
    @SerialName("UserCanEdit") private val userCanEdit: Boolean,
    @SerialName("bs") private val bs: Array<Unit>,
    @SerialName("dt") private val dateTime: String,
    @SerialName("irm") private val irm: Boolean,
    @SerialName("lp") private val lessonPlan: ActivityLessonPlan,
    @SerialName("m") val managerShortName: String,
    @SerialName("managers") val managers: Array<Manager>,
    @SerialName("mi") private val mi: Int,
    @SerialName("rollTapThreshold") private val rollTapThreshold: Int,
    @SerialName("wsv") private val wsv: String
)

/**
 * Container for LocationDetails
 */
@Serializable
data class LocationDetailsContainer(
    @SerialName("__type") private val dataType: String,
    @SerialName("LocationDetails") val locationDetails: Location,
    @SerialName("LocationId") val locationId: Int,
    @SerialName("CoveringLocationDetails") val coveringLocationDetails: String? = null,
    @SerialName("CoveringLocationId") val coveringLocationId: Int? = null,
    private val campusId: Int? = null,
    private val customLocation: Unit? = null
)

/**
 * "lp" - what does this do
 */
@Serializable
data class ActivityLessonPlan(
    @SerialName("__type") private val dataType: String,
    val fileAssetId: String? = null,
    val name: String? = null,
    private val mp: String,
    private val sp: String? = null,
    private val wnid: Int? = null,
)

/**
 * A manager (teacher)
 */
@Serializable
data class Manager(
    @SerialName("__type") private val dataType: String,
    @SerialName("CoveringImportIdentifier") val coveringImportIdentifier: String? = null,
    @SerialName("CoveringName") val coveringName: String? = null,
    @SerialName("CoveringPhotoPath") val coveringPhotoPath: String? = null,
    @SerialName("CoveringUserId") val coveringUserId: Int? = null,
    @SerialName("ManagerImportIdentifier") val managerImportIdentifier: String,
    @SerialName("ManagerName") val managerName: String,
    @SerialName("ManagerPhotoPath") val managerPhotoPath: String,
    @SerialName("ManagerUserId") val managerUserId: Int
)