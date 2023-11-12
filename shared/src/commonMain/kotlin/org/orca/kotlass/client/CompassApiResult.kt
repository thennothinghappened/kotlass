package org.orca.kotlass.client

import io.ktor.http.parsing.*
import io.ktor.serialization.*

/**
 * Represents a returned API result.
 */
sealed interface CompassApiResult<T> {
    class Success<T>(val data: T) : CompassApiResult<T>
    class Failure<T>(val error: CompassApiError) : CompassApiResult<T>
}

/**
 * Different error types when talking to Compass.
 */
sealed interface CompassApiError {

    sealed interface HasCause : CompassApiError {
        val error: Throwable
    }

    /**
     * JSON parsing error.
     */
    data class ParseError(override val error: JsonConvertException) : HasCause

    /**
     * Network/connection error.
     */
    data class ConnectionError(override val error: Throwable) : HasCause

    /**
     * Error returned from Compass, which unfortunately tells us nothing
     * about if we are at fault or if Compass is experiencing issues, or
     * if this is a credential issue, or parsing issue, etc.
     */
    data object CompassError : CompassApiError

    /**
     * Client logic error, aka, bug in Kotlass!
     */
    data class ClientError(override val error: Throwable) : HasCause
}