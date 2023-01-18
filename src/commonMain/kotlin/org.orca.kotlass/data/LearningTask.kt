package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
    val property: String = type.dueDate,
    val direction: String = directions.ascending
) {
    companion object {
        object type {
            const val dueDate = "dueDateTimestamp"
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
    @SerialName("__type") val dataType: String,
    val data: Array<LearningTask>,
    val total: Int
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
    @SerialName("__type") val dataType: String,
    val activityId: Int,
    val activityName: String,
    val activityStart: String,
    val assessmentPeriodId: Int? = null,
    val attachments: Array<LearningTaskAttachment>? = null,
    val canEditResults: Boolean,
    val canManage: Boolean,
    val canvasIntegrationId: String? = null,
    val categoryId: Int,
    val createdTimestamp: String,
    val description: String,
    val displayPrimaryGrading: Boolean,
    val distributionType: Int,
    val dueDateTimestamp: String? = null,
    val gradingItems: Array<LearningTaskGradingItem>,
    val groupName: String,
    val hidden: Boolean,
    val id: Int,
    val important: Boolean,
    val includeBreakdownHeading: Boolean,
    val includeInOverall: Boolean,
    val includeInSemesterReports: Boolean,
    val isAggregateTask: Boolean,
    val isContributingTask: Boolean,
    val masterTaskId: Int? = null,
    val name: String,
    val parentCode: String? = null,
    val promptForCycleOnPush: Boolean,
    val reportDisplayType: Int,
    val reportOrdinal: Boolean? = null,
    val resultDistributionDisplayType: Int,
    val rubricItems: Unit? = null,
    val rubricWikiNodeIds: Array<Int>? = null,
    val securityOptions: Array<LearningTaskSecurityOption>,
    val semesterReportCycles: Unit? = null,
    val sendSmsOutstanding: Boolean,
    val showAverageBoxPlot: Boolean? = null,
    val showLegendBoxPlot: Boolean? = null,
    val showNumericalBoxPlot: Boolean? = null,
    val showTaskDueDates: Boolean,
    val singleResultBreakdownCols: Int,
    val students: Array<LearningTaskStudent>,
    val subjectId: Unit? = null,
    val subjectName: String,
    val submissionItems: Array<LearningTaskSubmissionItems>? = null,
    val taskReportDescription: String,
    val taskTitleOnReport: String,
    val verticalBreakdownHeadings: Boolean,
    val wikiNodeId: Int
)

/**
 * An attachment on a learning task
 */
@Serializable
data class LearningTaskAttachment(
    @SerialName("__type") val dataType: String,
    val contentUrl: String? = null,
    val fileName: String,
    val id: String,
    val name: String,
    val wikiNodeId: Int,
    val wikiNodeType: Int
)

/**
 * Learning task grade
 */
@Serializable
data class LearningTaskGradingItem(
    @SerialName("__type") val dataType: String,
    val calculationSettings: Unit? = null,
    val calculationType: Unit? = null,
    val canvasIntegrationId: String? = null,
    val category: String? = null,
    val commentLength: Int? = null,
    val commentType: Int? = null,
    val decimalPlaces: Unit? = null,
    val dimension: String? = null,
    val gradingItemOrderBy: Int,
    val id: Int,
    val includeInCrossSubjectSummaryReport: Boolean,
    val includeInSemesterReport: Boolean,
    val includedCalculationCycleMeasures: Array<Unit>,
    val irishSubjectCode: Unit? = null,
    val isAggregateItem: Boolean,
    val isContributingItem: Boolean,
    val isPrimaryGrade: Boolean,
    val isSelfAssessment: Boolean,
    val isSystemScheme: Boolean,
    val masterGradingComponentId: Unit? = null,
    val max: String? = null,
    val min: String? = null,
    val measureUniqueId: String,
    val name: String,
    val shortName: String,
    val ordinal: Int,
    val parentStudentAccess: Int,
    val progressGraphItemType: Int,
    val reportIncludeDistribution: Boolean,
    val reportOrdinal: Int,
    val reportRenderType: Int,
    val staffAccess: Int,
    val strandCode: String? = null,
    val taskId: Int,
    val tempId: Unit? = null,
    val weighting: Int
)

/**
 * Learning task "Security option"
 */
@Serializable
data class LearningTaskSecurityOption(
    @SerialName("__type") val dataType: String,
    val allowSubmission: Boolean,
    val enableCommentChain: Boolean,
    val gradingVisible: Boolean,
    val taskVisible: Boolean,
    val userBaseRole: Int
)

@Serializable
data class LearningTaskSubmissionItems(
    @SerialName("__type") val dataType: String,
    val id: Int,
    val masterGradingComponentId: Unit? = null,
    val name: String,
    val taskId: Int,
    val type: Int
)

/**
 * Learning task student info
 */
@Serializable
data class LearningTaskStudent(
    @SerialName("__type") val dataType: String,
    val comments: Array<LearningTaskStudentComment>? = null,
    val dueDateTimestamp: String,
    val id: Int,
    val lastSubmittedTimestamp: String,
    val primaryResult: Unit? = null,
    val results: Array<LearningTaskStudentResult>,
    val rubricResults: Array<LearningTaskStudentRubricResult>? = null,
    val selfAssessmentEnabled: Boolean,
    val smsOutstandingSentTimestamp: String,
    val submissionStatus: Int,
    val submissions: Array<LearningTaskStudentSubmission>? = null,
    val submittedTimestamp: String,
    val taskId: Int,
    val teacherResponses: Array<LearningTaskStudentTeacherResponse>? = null,
    val userId: Int,
    val userName: String
)

/**
 * Comment on learning task
 */
@Serializable
data class LearningTaskStudentComment(
    @SerialName("__type") val dataType: String,
    val comment: String,
    val cycleMeasureId: Unit? = null,
    val gradingItemId: Unit? = null,
    val id: Int,
    val status: Int,
    val taskStudentId: Int,
    val timestamp: String,
    val userIdPoster: Int,
    val userNamePoster: String
)

/**
 * Teacher reply attachments on learning task
 */
@Serializable
data class LearningTaskStudentTeacherResponse(
    @SerialName("__type") val dataType: String,
    val contentUri: String? = null,
    val fileId: String,
    val fileName: String,
    val id: Int,
    val taskStudentId: Int,
    val teacherResponseType: Int,
    val timestamp: String,
    val wikiNodeId: Int? = null
)

/**
 * Rubric result on learning task
 */
@Serializable
data class LearningTaskStudentRubricResult(
    @SerialName("__type") val dataType: String,
    val cycleEnrolmentResultId: Int,
    val id: Int,
    val overallResult: Unit? = null,
    val result: Unit? = null,
    val rubricCriterionId: Int,
    val rubricGradingScaleId: Int,
    val rubricId: Int,
    val taskStudentId: Int
)

/**
 * Result on learning task
 */
@Serializable
data class LearningTaskStudentResult(
    @SerialName("__type") val dataType: String,
    val flaggedResultType: Int,
    val id: Int,
    val isCalculatedResult: Boolean,
    val isEstimatedResult: Boolean,
    val lockedTimestamp: String? = null,
    val modifiedByUserId: Int,
    val modifiedTimestamp: String,
    val reportGradingSchemeOptionId: String? = null,
    val result: String,
    val taskGradingItemId: Int,
    val taskStudentId: Int,
    val userIdLocked: Unit? = null
)

/**
 * Submission on a learning task
 */
@Serializable
data class LearningTaskStudentSubmission(
    @SerialName("__type") val dataType: String,
    val contentUrl: String? = null,
    val fileId: String,
    val fileName: String,
    val id: Int,
    val submissionFileType: Int,
    val submitterBaseRole: Int,
    val taskStudentId: Int,
    val taskSubmissionItemId: Int,
    val timestamp: String,
    val wikiNodeId: Int? = null
)