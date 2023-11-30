package org.orca.kotlass.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Compass wraps all returned data in
 * { [data]: [T] }, or { h: [String] } for errors,
 * which we take as failure anyway, handled by [CompassApiResult]
 */
@Serializable
data class ResponseWrapper<T>(
    @SerialName("d")
    val data: T
)
