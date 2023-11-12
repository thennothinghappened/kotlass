package org.orca.kotlass.dateutils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun Instant.toLocalDateTime(): LocalDateTime =
    this.toLocalDateTime(TimeZone.currentSystemDefault())