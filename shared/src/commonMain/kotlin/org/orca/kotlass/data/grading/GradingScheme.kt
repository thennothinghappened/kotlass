package org.orca.kotlass.data.grading

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Compass uses indices into this list to describe the way
 * an assessment grade should work, e.g. in learning tasks.
 */
@Serializable
data class GradingScheme(
    /**
     * Name of this grading scheme.
     */
    val name: String,

    /**
     * Description of this grading scheme.
     */
    val description: String,

    /**
     * Unique ID for this [GradingScheme].
     */
    @SerialName("measureUniqueId")
    val id: String,

    /**
     * List of options for this scheme.
     */
    val options: List<GradingSchemeOption>
)

/**
 * An option for a [GradingScheme]'s results.
 */
@Serializable
data class GradingSchemeOption(

    /**
     * Display name of this option.
     */
    @SerialName("displayValue")
    val name: String,

    /**
     * Value assigned to the option internally.
     */
    val value: String?
)