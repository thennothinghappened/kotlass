package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.utils.InstantNullableSerializer

/**
 * Data to send to get the newsfeed
 */
@Serializable
data class NewsFeedRequest(
    val activityId: Int? = null,
    val limit: String = "50",
    val start: String = "0"
)

/**
 * A news item on the newsfeed.
 */
@Serializable
data class NewsItem(
    @SerialName("__type") private val dataType: String,
    @SerialName("Title") val title: String,
    @SerialName("Content1") val content1: String?,
    @SerialName("Content2") val content2: String?,
    @SerialName("Attachments") val attachments: List<NewsItemAttachment>,
    @SerialName("CommunicationType") val communicationType: Int, //todo: what are communication types
    @SerialName("Priority") val priority: Boolean,
    @SerialName("NotificationStatus") val notificationStatus: Int? = null, //todo: what are the statuses
    @SerialName("CreatedByAdmin") val createdByAdmin: Boolean,
    @SerialName("SenderId") val senderId: Int? = null,
    @SerialName("UserImageUrl") val userImageUrl: String,
    @SerialName("UserName") val userName: String,
    @Serializable(InstantNullableSerializer::class)
    @SerialName("PostDateTime") val postDateTime: Instant?, // todo: time post was made?
    @Serializable(InstantNullableSerializer::class)
    @SerialName("Start") val startDate: Instant?, // todo: point post will be shown from?
    @Serializable(InstantNullableSerializer::class)
    @SerialName("Finish") val finishDate: Instant?, // useful to know when post expires
    @SerialName("NewsItemGroupTargets") val newsItemGroupTargets: List<NewsItemGroupTarget>, // the group of people this will be sent to
    @SerialName("ShowImagesFullScreen") val showImagesFullscreen: Boolean,
    @SerialName("UserId") private val userId: Int, // duplicate of above
    @SerialName("EmailSentDate") private val emailSentDate: String,
    @SerialName("Locked") private val locked: Boolean,
    @SerialName("NewsItemCustomGroupTargets") private val newsItemCustomGroupTargets: NewsItemCustomGroupTarget, // this doesn't seem to be used
    @SerialName("NewsItemId") private val newsItemId: String, // todo: this probably isn't useful
    @SerialName("PublicWebsite") private val publicWebsite: Boolean,
    @SerialName("PublishToLinkedSchools") private val publishToLinkedSchools: Boolean,
    @SerialName("PublishToTalkingPoints") private val publishToTalkingPoints: Boolean,
    @SerialName("StartFinishString") private val startFinishString: String,
    @SerialName("TalkingPointsTags") private val talkingPointsTags: List<String>,
)

/**
 * An attachment for a newsfeed item.
 */
@Serializable
data class NewsItemAttachment(
    @SerialName("__type") private val dataType: String,
    @SerialName("AssetId") val assetId: Int,
    @SerialName("FileAssetType") val fileAssetType: Int,
    @SerialName("IsImage") val isImage: Boolean,
    @SerialName("Name") val name: String,
    @SerialName("OriginalFileName") val originalFileName: String,
    @SerialName("UiLink") val uiLink: String,
    @SerialName("SourceOrganisationId") private val sourceOrganisastionId: String?,
    @SerialName("Url") private val url: String?
)

/**
 * Custom target for newsfeed item
 */
@Serializable
data class NewsItemCustomGroupTarget(
    @SerialName("__type") private val dataType: String,
    @SerialName("CampusIds") val campusIds: List<Int>,
    @SerialName("CustomGroupIds") val customGroupIds: List<Int>
)

/**
 * Target for newsfeed item
 */
@Serializable
data class NewsItemGroupTarget(
    @SerialName("__type") private val dataType: String,
    @SerialName("ActivityIds") val activityIds: List<Int>,
    @SerialName("BaseRole") val baseRole: Int,
    @SerialName("CampusIds") val campusIds: List<Int>,
    @SerialName("FormGroups") val formGroups: List<Unit>,
    @SerialName("Future") val future: Boolean,
    @SerialName("Houses") val houses: List<String>, // todo: what format (is this a string?)
    @SerialName("UserIds") val userIds: List<Int>,
    @SerialName("YearLevels") val yearLevsl: List<Int>
)