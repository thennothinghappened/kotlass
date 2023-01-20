package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlertList(override val h: String? = null, override val d: Array<Alert>? = null) : CData

@Serializable
data class Alert(
    @SerialName("__type") private val dataType: String,
    @SerialName("AlertItemId") val alertItemId: Int,
    @SerialName("Body") val body: String,
    @SerialName("Content") val content: String,
    @SerialName("Dismissible") val dismissible: Boolean,
    @SerialName("LinkText") val linkText: String,
    @SerialName("LinkUrl") val linkUrl: String,
    @SerialName("Title") val title: String,
    @SerialName("Type") val type: Int, //todo: what are the types?
    @SerialName("ImageSourceUrl") private val imageSourceUrl: String, //todo: alerts with images?
    @SerialName("IsWarning") private val isWarning: Boolean //todo: what triggers this?
)