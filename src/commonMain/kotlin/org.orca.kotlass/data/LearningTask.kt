package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Data to send to get the LearningTaskListContainer for a subject by its ID
 */
@Serializable
data class LearningTasksByActivityIdRequest(
    val start: Int = 0,
    val page: Int = 1,
    val limit: Int = 2000,
    val sort: String = Json.encodeToString(LearningTaskSortType()), // no idea why they expect this format
    val activityId: String,
    val showHiddenTasks: Boolean = false
)

/**
 * Data to send to get the LearningTaskListContainer for all subjects
 */
@Serializable
data class LearningTasksByUserIdRequest(
    val start: Int = 0,
    val page: Int = 1,
    val limit: Int = 2000,
    val sort: String = Json.encodeToString(LearningTaskSortType()), // no idea why they expect this format
    val academicGroupId: Int? = null, // null grabs the default year (this year), numbers grab a year defined by GetAllAcademicGroups
    val showHiddenTasks: Boolean = false,
    val userId: Int
)

/**
 * Learning task sorting type
 */
@Serializable
data class LearningTaskSortType(
    val property: String = type.dueDateTimestamp,
    val direction: String = directions.ascending
) {
    companion object {
        object type {
            const val dueDateTimestamp = "dueDateTimestamp"
            const val groupName = "groupName"
        }
        object directions {
            const val ascending = "ASC"
            const val descending = "DESC"
        }
    }
}

/**
 * Compass' "DataExtGridDataContainer" for containing LearningTasks
 */
@Serializable
data class LearningTaskList(
    @SerialName("__type") private val dataType: String,
    val data: Array<LearningTask>,
    private val total: Int
)

/**
 * Data type received from getAllLearningTasksByActivityId,
 * Contains LearningTaskList - array of LearningTasks
 */
@Serializable
data class LearningTaskListContainer(override val h: String? = null, override val d: LearningTaskList? = null) : CData

/**
 * Contains information about a specific class or "activity".
 */
@Serializable
data class LearningTask(
    @SerialName("__type") private val dataType: String,
    val activityId: Int,
    val activityName: String,
    val description: String,
    @Serializable(InstantSerializer::class)
    val dueDateTimestamp: Instant?,
    val categoryId: Int,
    val gradingItems: Array<LearningTaskGradingItem>,
    @Serializable(InstantSerializer::class)
    val createdTimestamp: Instant?,
    val hidden: Boolean,
    val name: String,
    val subjectName: String,
    @Serializable(InstantSerializer::class)
    private val activityStart: Instant?,
    private val assessmentPeriodId: Int? = null,
    private val attachments: Array<LearningTaskAttachment>? = null,
    private val canEditResults: Boolean,
    private val canManage: Boolean,
    private val canvasIntegrationId: String? = null,
    private val displayPrimaryGrading: Boolean,
    private val distributionType: Int,
    private val groupName: String,
    private val id: Int,
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
    private val rubricWikiNodeIds: Array<Int>? = null,
    private val securityOptions: Array<LearningTaskSecurityOption>,
    private val semesterReportCycles: Unit? = null,
    private val sendSmsOutstanding: Boolean,
    private val showAverageBoxPlot: Boolean? = null,
    private val showLegendBoxPlot: Boolean? = null,
    private val showNumericalBoxPlot: Boolean? = null,
    private val showTaskDueDates: Boolean,
    private val singleResultBreakdownCols: Int,
    val students: Array<LearningTaskStudent>,
    private val subjectId: Unit? = null,
    private val submissionItems: Array<LearningTaskSubmissionItem>? = null,
    private val taskReportDescription: String,
    private val taskTitleOnReport: String,
    private val verticalBreakdownHeadings: Boolean,
    private val wikiNodeId: Int
)

/**
 * An attachment on a learning task
 */
@Serializable
data class LearningTaskAttachment(
    private @SerialName("__type") val dataType: String,
    private val contentUrl: String? = null,
    val fileName: String,
    val id: String,
    val name: String,
    private val wikiNodeId: Int,
    private val wikiNodeType: Int
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
    private val gradingItemOrderBy: Int,
    private val includeInCrossSubjectSummaryReport: Boolean,
    private val includeInSemesterReport: Boolean,
    private val includedCalculationCycleMeasures: Array<Unit>,
    private val irishSubjectCode: Unit? = null,
    private val isAggregateItem: Boolean,
    private val isContributingItem: Boolean,
    private val isPrimaryGrade: Boolean,
    private val isSelfAssessment: Boolean,
    private val isSystemScheme: Boolean,
    private val masterGradingComponentId: Unit? = null,
    private val max: String? = null,
    private val min: String? = null,
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
    val id: Int,
    val name: String,
    val type: Int,
    private val masterGradingComponentId: Unit? = null,
    private val taskId: Int,
)

/**
 * Learning task student info
 */
@Serializable
data class LearningTaskStudent(
    @SerialName("__type") private val dataType: String,
    val comments: Array<LearningTaskStudentComment>? = null,
    val id: Int,
    val results: Array<LearningTaskStudentResult>,
    val rubricResults: Array<LearningTaskStudentRubricResult>? = null,
    val selfAssessmentEnabled: Boolean,
    val submissions: Array<LearningTaskStudentSubmission>? = null,
    val teacherResponses: Array<LearningTaskStudentTeacherResponse>? = null,
    @Serializable(InstantSerializer::class)
    private val dueDateTimestamp: Instant?,
    @Serializable(InstantSerializer::class)
    private val lastSubmittedTimestamp: Instant?,
    private val primaryResult: Unit? = null,
    @Serializable(InstantSerializer::class)
    private val smsOutstandingSentTimestamp: Instant?,
    private val submissionStatus: Int,
    @Serializable(InstantSerializer::class)
    val submittedTimestamp: Instant?,
    private val taskId: Int,
    private val userId: Int,
    private val userName: String
)

/**
 * Comment on learning task
 */
@Serializable
data class LearningTaskStudentComment(
    @SerialName("__type") private val dataType: String,
    val comment: String,
    @Serializable(InstantSerializer::class)
    val timestamp: Instant?,
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
    val timestamp: Instant?,
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
    @Serializable(InstantSerializer::class)
    val modifiedTimestamp: Instant?,
    val reportGradingSchemeOptionId: String? = null,
    val result: String,
    private val flaggedResultType: Int,
    private val isCalculatedResult: Boolean,
    private val isEstimatedResult: Boolean,
    private val lockedTimestamp: String? = null,
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
    val timestamp: String,
    private val contentUrl: String? = null,
    private val submissionFileType: Int,
    private val submitterBaseRole: Int,
    private val taskStudentId: Int,
    private val taskSubmissionItemId: Int,
    private val wikiNodeId: Int? = null
)