package org.orca.kotlass.data.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NewsFeedPagedRequest(

    @SerialName("activityId")
    val activityId: Int?,

    @SerialName("limit")
    val limit: String,

    @SerialName("start")
    val offset: String

)
