package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orca.kotlass.utils.InstantSerializer

/**
 * Data to send to get the list of classes
 */
@Serializable
data class StandardClassesOfUserRequest(
    override val start: Int = 0,
    override val limit: Int = 50,
    override val page: Int = 1,
    @Transient val sort: List<StandardClassListSortType> = listOf(
        StandardClassListSortType(), StandardClassListSortType(property = StandardClassListSortType.Types.name)
    ),
    val academicGroupId: Int = -1,
    val userId: Int,
) : Request {
    @SerialName("sort") private val _sort = json.encodeToString(sort)
}

/**
 * StandardClass list sorting type
 */
@Serializable
data class StandardClassListSortType(
    override val property: String = Types.yearLevelShortName,
    override val direction: String = SortType.Directions.ascending
) : SortType {
    object Types {
        const val yearLevelShortName = "yearLevelShortName"
        const val name = "name"
    }
}

/**
 * Description of a class assigned to the user
 */
@Serializable
data class StandardClass(
    @SerialName("__type") private val dataType: String = "",
    val campusId: Int? = null,
    val facultyName: String,
    @Serializable(InstantSerializer::class)
    val start: Instant,
    @Serializable(InstantSerializer::class)
    val finish: Instant,
    @SerialName("id") val activityId: Int,
    val importIdentifier: String,
    val locationId: Int? = null,
    val managerId: Int,
    @SerialName("managerImportIdentifier") val managerShortName: String,
    val name: String,
    val subjectId: Int,
    val subjectLongName: String,
    val yearLevelShortName: String,
    private val attendanceModeDefault: Int,
    private val checkInEnabledDefault: Int,
    private val customLocation: Unit? = null,
    private val description: String? = null,
    private val extendedStatusId: Int,
    private val haparaSyncEnabled: Boolean,
    private val importTeachers: Boolean,
    private val layerAllowsImport: Boolean,
    private val layerId: Int,
    private val rollTapThreshold: Int,
    private val subjectImportIdentifier: String,
    private val timetableStructureId: Int
)
