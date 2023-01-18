package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data to send to get the ActivitySummary
 */
@Serializable
data class ActivitySummaryRequest(
    val instanceId: String
)

/**
 * Data type received from getLessonsByInstanceId,
 * Contains an ActivitySummary
 */
@Serializable
data class ActivitySummaryContainer(override val h: String? = null, override val d: ActivitySummary? = null) : CData

/**
 * Contains information about a specific class or "activity".
 */
@Serializable
data class ActivitySummary(
    @SerialName("__type") val dataType: String,
    @SerialName("AcademicYearLevel") val academicYearLevel: String,
    @SerialName("ActivityDefaultAttendanceMode") val actiivityDefaultAttendanceMode: Int,
    @SerialName("ActivityDefaultCampusId") val activityDefaultCampusId: Int? = null,
    @SerialName("ActivityDefaultCustomLocation") val activityDefaultCustomLocation: Int? = null,
    @SerialName("ActivityDefaultLocationId") val activityDefaultLocationId: Int? = null,
    @SerialName("ActivityDisplayName") val activityDisplayName: String,
    @SerialName("ActivityFinish") val activityFinish: String? = null,
    @SerialName("ActivityId") val activityId: Int,
    @SerialName("ActivityImportIdentifier") val activityImportIdentifier: String,
    @SerialName("ActivityManagerId") val activityManagerId: String,
    @SerialName("ActivityPermissions") val activityPermissions: ActivityPermissions,
    @SerialName("ActivitySingular") val activitySingular: String,
    @SerialName("ActivityStart") val activityStart: String? = null,
    @SerialName("ExtendedStatusId") val extendedStatusId: Int,
    @SerialName("FacultyId") val facultyId: Int,
    @SerialName("FacultyManagerIds") val facultyManagerIds: Array<Int>,
    @SerialName("Instances") val instances: Array<Activity>,
    @SerialName("IsBusRoute") val isBusRoute: Boolean,
    @SerialName("IsExam") val isExam: Boolean,
    @SerialName("IsLearningTasksAdmin") val isLearningTasksAdmin: Boolean,
    @SerialName("IsMeeting") val isMeeting: Boolean,
    @SerialName("IsSchoolApproval") val isSchoolApproval: Boolean,
    @SerialName("IsStandardClass") val isStandardClass: Boolean,
    @SerialName("IsYardDuty") val isYardDuty: Boolean,
    @SerialName("RollTapThreshold") val rollTapThreshold: Int,
    @SerialName("SubjectCoordinatorId") val subjectCoordinatorId: String? = null,
    @SerialName("SubjectId") val subjectId: Int,
    @SerialName("SubjectName") val subjectName: String,
    @SerialName("SubjectShortName") val subjectShortName: String
)

/**
 * Contains information about if you have permission to edit the activity
 */
@Serializable
data class ActivityPermissions(
    @SerialName("__type") val dataType: String,
    val canDelete: Boolean,
    val canEdit: Boolean
)

/**
 * An instance of an Activity (class session)
 */
@Serializable
data class Activity(
    @SerialName("__type") val dataType: String,
    @SerialName("ActivityDisplayName") val activityDisplayName: String,
    @SerialName("ActivityId") val activityId: String,
    @SerialName("ActivityImportIdentifier") val activityImportIdentifier: String,
    @SerialName("ActivityManagerId") val activityManagerId: Int,
    @SerialName("ActivitySingular") val activitySingular: String,
    @SerialName("AttendanceMode") val attendanceMode: Int,
    @SerialName("AttendeeCount") val attendeeCount: Int,
    @SerialName("AttendeeLimit") val attendeeLimit: Int? = null,
    @SerialName("AttendeeUserIdList") val attendeeUserIdList: Array<Int>,
    @SerialName("CampusId") val campusId: Int,
    @SerialName("CoveringIid") val coveringIid: Unit? = null,
    @SerialName("CoveringPhotoPath") val coveringPhotoId: String? = null,
    @SerialName("CoveringUid") val converingUid: Int,
    @SerialName("CurrentInstance") val currentInstance: Boolean,
    @SerialName("ExtendedStatusId") val extendedStatusId: Int,
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
    @SerialName("LocationDetails") val locationDetails: Location,
    @SerialName("LocationId") val locationId: Int,
    @SerialName("locations") val locations: Array<LocationDetailsContainer>, // very redundant... big waste of memory
    @SerialName("ManagerPhotoPath") val managerPhotoPath: String,
    @SerialName("ManagerTextReadable") val managerTextReadable: String,
    @SerialName("PastInstance") val pastInstance: Boolean,
    @SerialName("ReadableAttendeeCount") val readableAttendeeCount: String,
    @SerialName("RunningStatus") val runningStatus: Boolean,
    @SerialName("SubjectId") val subjectId: String,
    @SerialName("SubjectName") val subjectName: String,
    @SerialName("SubjectShortname") val subjectShortName: String,
    @SerialName("UpcomingInstance") val upcomingInstance: Boolean,
    @SerialName("UserCanCancelOrDelete") val userCanCancelOrDelete: Boolean,
    @SerialName("UserCanEdit") val userCanEdit: Boolean,
    @SerialName("bs") val bs: Array<Unit>,
    @SerialName("dt") val dateTime: String,
    @SerialName("fn") val finish: String,
    @SerialName("id") val id: String,
    @SerialName("irm") val irm: Boolean,
    @SerialName("lp") val lp: ActivityLP,
    @SerialName("m") val managerShortName: String,
    @SerialName("managers") val managers: Array<Manager>,
    @SerialName("mi") val mi: Int,
    @SerialName("rollTapThreshold") val rollTapThreshold: Int,
    @SerialName("st") val start: String,
    @SerialName("wsv") val wsv: String
)

/**
 * Container for LocationDetails
 */
@Serializable
data class LocationDetailsContainer(
    @SerialName("__type") val dataType: String,
    @SerialName("CoveringLocationDetails") val coveringLocationDetails: String? = null,
    @SerialName("CoveringLocationId") val coveringLocationId: Int? = null,
    @SerialName("LocationDetails") val locationDetails: Location,
    @SerialName("LocationId") val locationId: Int,
    val campusId: Int? = null,
    val customLocation: Unit? = null
)

/**
 * "lp" - what does this do
 */
@Serializable
data class ActivityLP(
    @SerialName("__type") val dataType: String,
    val fileAssetId: String? = null,
    val mp: String,
    val name: String? = null,
    val sp: String? = null,
    val wnid: String? = null,
)

/**
 * A manager (teacher)
 */
@Serializable
data class Manager(
    @SerialName("__type") val dataType: String,
    @SerialName("CoveringImportIdentifier") val coveringImportIdentifier: String? = null,
    @SerialName("CoveringName") val coveringName: String? = null,
    @SerialName("CoveringPhotoPath") val coveringPhotoPath: String? = null,
    @SerialName("CoveringUserId") val coveringUserId: Int? = null,
    @SerialName("ManagerImportIdentifier") val managerImportIdentifier: String,
    @SerialName("ManagerName") val managerName: String,
    @SerialName("ManagerPhotoPath") val managerPhotoPath: String,
    @SerialName("ManagerUserId") val managerUserId: Int
)