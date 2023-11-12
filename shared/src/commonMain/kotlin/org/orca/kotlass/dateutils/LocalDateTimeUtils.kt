package org.orca.kotlass.dateutils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

internal fun LocalDateTime.Companion.now() =
    Clock.System.now().toLocalDateTime()
internal fun LocalDate.Companion.now() =
    LocalDateTime.now().date

internal fun LocalTime.Companion.now() =
    LocalDateTime.now().time