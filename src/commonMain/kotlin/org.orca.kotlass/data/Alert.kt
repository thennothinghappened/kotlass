package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlertList(override val h: String? = null, override val d: Array<Alert>? = null) : CData

@Serializable
data class Alert(
    @SerialName("__type") val dataType: String,
    @SerialName("AlertItemId") val alertItemId: Int,
    @SerialName("Body") val body: String,
    @SerialName("Content") val content: String,
    @SerialName("Dismissible") val dismissable: Boolean,
    @SerialName("ImageSourceUrl") val imageSourceUrl: String,
    @SerialName("IsWarning") val isWarning: Boolean,
    @SerialName("LinkText") val linkText: String,
    @SerialName("LinkUrl") val linkUrl: String,
    @SerialName("Title") val title: String,
    @SerialName("Type") val type: Int
)