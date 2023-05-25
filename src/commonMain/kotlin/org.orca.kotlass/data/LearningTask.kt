package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.serialization.*
import org.orca.kotlass.utils.FileTypeSerializer
import org.orca.kotlass.utils.InstantNullableSerializer
import org.orca.kotlass.utils.InstantSerializer

/**
 * Data to send to get the LearningTaskListContainer for a subject by its ID
 */
@Serializable
data class LearningTasksByActivityIdRequest(
    override val start: Int = 0,
    override val page: Int = 1,
    override val limit: Int = 2000,
    @Transient val sort: LearningTaskSortType = LearningTaskSortType(), // no idea why they expect this format
    val activityId: String,
    val showHiddenTasks: Boolean = false
) : Request {
    @SerialName("sort") private val _sort = json.encodeToString(sort)
}

/**
 * Data to send to get the LearningTask Array for all subjects
 */
@Serializable
data class LearningTasksByUserIdRequest(
    override val start: Int = 0,
    override val page: Int = 1,
    override val limit: Int = 2000,
    @Transient val sort: LearningTaskSortType = LearningTaskSortType(), // no idea why they expect this format
    val academicGroupId: Int? = null, // null grabs the default year (this year), numbers grab a year defined by GetAllAcademicGroups
    val showHiddenTasks: Boolean = false,
    val userId: Int
) : Request {
    @SerialName("sort") private val _sort = json.encodeToString(sort)
}

/**
 * Learning task sorting type
 */
@Serializable
data class LearningTaskSortType(
    override val property: String = Types.dueDateTimestamp,
    override val direction: String = SortType.Directions.ascending
) : SortType {
    object Types {
        const val dueDateTimestamp = "dueDateTimestamp"
        const val groupName = "groupName"
    }
}

/**
 * A Learning Task is coursework set by a teacher usually with a due date.
 *
 * @see Activity
 * @property activityId ID associated with the activity from which this task originated
 * @property activityName Readable name of the activity associated
 * @property attachments List of attachments specified by the teacher to download for this task
 * @property createdTimestamp Time at which the task was created
 * @property description Details of this task as HTML.
 * @property dueDateTimestamp When the task is due
 * @property categoryId Integer corresponding to the ID of a [TaskCategory] for which this task is associated
 * @property gradingItems TODO
 * @property hidden Whether this task should be displayed
 * @property id Integer ID corresponding to this task
 * @property name Name of the task
 * @property subjectName Readable name of the activity this task is associated with
 * @property submissionItems List of items submitted to the task
 */
@Serializable
data class LearningTask(
    @SerialName("__type") private val dataType: String,
    val activityId: Int,
    val activityName: String,
    val attachments: List<LearningTaskAttachment>? = null,
    @Serializable(InstantSerializer::class)
    val createdTimestamp: Instant,
    val description: String,
    @Serializable(InstantNullableSerializer::class)
    val dueDateTimestamp: Instant?,
    val categoryId: Int,
    val gradingItems: List<LearningTaskGradingItem>,
    val hidden: Boolean,
    val id: Int,
    val name: String,
    val subjectName: String,
    val submissionItems: List<LearningTaskSubmissionItem>? = null,
    @Serializable(InstantNullableSerializer::class)
    private val activityStart: Instant?,
    private val assessmentPeriodId: Int? = null,
    private val canEditResults: Boolean,
    private val canManage: Boolean,
    private val canvasIntegrationId: String? = null,
    private val displayPrimaryGrading: Boolean,
    private val distributionType: Int,
    private val externalIntegrationType: Int? = null,
    private val groupName: String,
    private val important: Boolean,
    private val includeBreakdownHeading: Boolean,
    private val includeInOverall: Boolean,
    private val includeInSemesterReports: Boolean,
    private val isAggregateTask: Boolean,
    private val isContributingTask: Boolean,
    private val masterTaskId: Int? = null,
    private val parentCode: String? = null,
    private val promptForCycleOnPush: Boolean,
    private val reportDisplayType: Int,
    private val reportOrdinal: Boolean? = null,
    private val resultDistributionDisplayType: Int,
    private val rubricItems: Unit? = null,
    private val rubricWikiNodeIds: List<Int>? = null,
    private val securityOptions: List<LearningTaskSecurityOption>,
    private val semesterReportCycles: List<LearningTaskSemesterReportsTaskCycle>? = null,
    private val sendSmsOutstanding: Boolean,
    private val showAverageBoxPlot: Boolean? = null,
    private val showLegendBoxPlot: Boolean? = null,
    private val showNumericalBoxPlot: Boolean? = null,
    private val showTaskDueDates: Boolean,
    private val singleResultBreakdownCols: Int,
    val students: List<LearningTaskStudent>,
    private val subjectId: Unit? = null,
    private val subjectHeader: Unit? = null,
    private val taskReportDescription: String? = null,
    private val taskTitleOnReport: String? = null,
    private val verticalBreakdownHeadings: Boolean,
    private val wikiNodeId: Int
)

/**
 *
 */
@Serializable
data class LearningTaskSemesterReportsTaskCycle(
    @SerialName("__type") private val dataType: String,
    val includeInSemesterReports: Boolean,
    val reportCycleId: Int,
    val reportOrdinal: Boolean? = null,
    val taskId: Int
)

/**
 * An attachment for a [LearningTask] added by a teacher.
 *
 * @property fileName The name of the file when it was uploaded
 * @property id ID for the associated file if stored on Compass for using with [org.orca.kotlass.IKotlassClient] `downloadFile()`
 * @property name Display name
 * @property fileType [FileType] of the file
 */
@Serializable
data class LearningTaskAttachment(
    @SerialName("__type") private val dataType: String,
    private val contentUrl: String? = null,
    val fileName: String,
    val id: String,
    val name: String,
    private val wikiNodeId: Int,
    @Serializable(FileTypeSerializer::class)
    @SerialName("wikiNodeType") val fileType: FileType
)

/**
 * Learning task grade
 */
@Serializable
data class LearningTaskGradingItem(
    @SerialName("__type") private val dataType: String,
    val id: Int,
    val measureUniqueId: String,
    val name: String,
    private val calculationSettings: Unit? = null,
    private val calculationType: Unit? = null,
    private val canvasIntegrationId: String? = null,
    private val category: String? = null,
    private val commentLength: Int? = null,
    private val commentType: Int? = null,
    private val decimalPlaces: Unit? = null,
    private val dimension: String? = null,
    private val externalIntegrationType: Int? = null,
    private val gradingItemOrderBy: Int,
    private val includeInCrossSubjectSummaryReport: Boolean,
    private val includeInSemesterReport: Boolean,
    private val includedCalculationCycleMeasures: List<Unit>,
    private val irishSubjectCode: Unit? = null,
    private val isAggregateItem: Boolean,
    private val isContributingItem: Boolean,
    private val isPrimaryGrade: Boolean,
    private val isSelfAssessment: Boolean,
    private val isSystemScheme: Boolean,
    private val masterGradingComponentId: Unit? = null,
    private val max: String? = null,
    private val min: String? = null,
    private val nzAssessmentStandard: Unit? = null,
    private val shortName: String,
    private val ordinal: Int,
    private val parentStudentAccess: Int,
    private val progressGraphItemType: Int,
    private val reportIncludeDistribution: Boolean,
    private val reportOrdinal: Int,
    private val reportRenderType: Int,
    private val staffAccess: Int,
    private val strandCode: String? = null,
    private val taskId: Int,
    private val tempId: Unit? = null,
    private val weighting: Int
)

/**
 * Learning task "Security option"
 */
@Serializable
data class LearningTaskSecurityOption(
    @SerialName("__type") private val dataType: String,
    val allowSubmission: Boolean,
    val enableCommentChain: Boolean,
    val gradingVisible: Boolean,
    val taskVisible: Boolean,
    val userBaseRole: Int
)

@Serializable
data class LearningTaskSubmissionItem(
    @SerialName("__type") private val dataType: String,
    @Serializable(FileTypeSerializer::class)
    @SerialName("type") val fileType: FileType,
    val id: Int,
    val name: String,
    private val masterGradingComponentId: Unit? = null,
    private val taskId: Int,
)

/**
 * Learning task student info
 */
@Serializable
data class LearningTaskStudent(
    @SerialName("__type") private val dataType: String,
    val comments: List<LearningTaskStudentComment>? = null,
    val id: Int,
    val results: List<LearningTaskStudentResult>,
    val rubricResults: List<LearningTaskStudentRubricResult>? = null,
    val selfAssessmentEnabled: Boolean,
    val submissions: List<LearningTaskStudentSubmission>? = null,
    @SerialName("submissionStatus") private val _submissionStatus: Int,
    @Serializable(InstantNullableSerializer::class)
    val submittedTimestamp: Instant?,
    val teacherResponses: List<LearningTaskStudentTeacherResponse>? = null,
    @Serializable(InstantNullableSerializer::class)
    private val dueDateTimestamp: Instant?,
    @Serializable(InstantNullableSerializer::class)
    private val lastSubmittedTimestamp: Instant?,
    private val primaryResult: Unit? = null,
    @Serializable(InstantNullableSerializer::class)
    private val smsOutstandingSentTimestamp: Instant?,
    private val taskId: Int,
    private val userId: Int,
    private val userName: String
) {
    @Transient val submissionStatus = when(_submissionStatus) {
        1 -> LearningTaskSubmissionStatus.PENDING
        2 -> LearningTaskSubmissionStatus.OVERDUE
        3 -> LearningTaskSubmissionStatus.SUBMITTED_ON_TIME
        4 -> LearningTaskSubmissionStatus.SUBMITTED_LATE
        else -> { throw Throwable("Unknown submission status $_submissionStatus") }
    }
}

/**
 * @see LearningTask
 * @property PENDING Not yet been received, and the deadline hasn't hit.
 * @property OVERDUE Deadline has hit and not received
 * @property SUBMITTED_ON_TIME Submitted before deadline
 * @property SUBMITTED_LATE Submitted after deadline
 */
enum class LearningTaskSubmissionStatus {
    PENDING,
    OVERDUE,
    SUBMITTED_ON_TIME,
    SUBMITTED_LATE
}

/**
 * Comment on learning task
 */
@Serializable
data class LearningTaskStudentComment(
    @SerialName("__type") private val dataType: String,
    val comment: String,
    @Serializable(InstantSerializer::class)
    val timestamp: Instant,
    val userIdPoster: Int,
    val userNamePoster: String,
    private val cycleMeasureId: Unit? = null,
    private val gradingItemId: Unit? = null,
    private val id: Int,
    private val status: Int,
    private val taskStudentId: Int,
)

/**
 * Teacher reply attachments on learning task
 */
@Serializable
data class LearningTaskStudentTeacherResponse(
    @SerialName("__type") private val dataType: String,
    val fileId: String,
    val fileName: String,
    val id: Int,
    val teacherResponseType: Int,
    @Serializable(InstantSerializer::class)
    val timestamp: Instant,
    private val contentUri: String? = null,
    private val taskStudentId: Int,
    private val wikiNodeId: Int? = null
)

/**
 * Rubric result on learning task
 */
@Serializable
data class LearningTaskStudentRubricResult(
    @SerialName("__type") private val dataType: String,
    private val cycleEnrolmentResultId: Int,
    val id: Int,
    val rubricCriterionId: Int,
    val rubricGradingScaleId: Int,
    val rubricId: Int,
    private val overallResult: Unit? = null,
    private val result: Unit? = null,
    private val taskStudentId: Int
)

/**
 * Result on learning task
 */
@Serializable
data class LearningTaskStudentResult(
    @SerialName("__type") private val dataType: String,
    val id: Int,
    val modifiedByUserId: Int,
    @Serializable(InstantNullableSerializer::class)
    val modifiedTimestamp: Instant?,
    val reportGradingSchemeOptionId: String? = null,
    val result: String,
    private val flaggedResultType: Int,
    private val isCalculatedResult: Boolean,
    private val isEstimatedResult: Boolean,
    @Serializable(InstantNullableSerializer::class)
    private val lockedTimestamp: Instant? = null,
    private val taskGradingItemId: Int,
    private val taskStudentId: Int,
    private val userIdLocked: Unit? = null
)

/**
 * Submission on a learning task
 */
@Serializable
data class LearningTaskStudentSubmission(
    @SerialName("__type") private val dataType: String,
    val fileId: String,
    val fileName: String,
    val id: Int,
    @Serializable(FileTypeSerializer::class)
    @SerialName("submissionFileType") val fileType: FileType,
    @Serializable(InstantSerializer::class)
    val timestamp: Instant,
    val taskSubmissionItemId: Int,
    private val contentUrl: String? = null,

    private val submitterBaseRole: Int,
    private val taskStudentId: Int,
    private val wikiNodeId: Int? = null
)