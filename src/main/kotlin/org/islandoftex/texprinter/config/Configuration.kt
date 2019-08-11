// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.config

import kotlinx.serialization.Serializable

/**
 * Retrieves the application properties by JSON serialization.
 *
 * @author Ben Frank
 * @version 3.0
 * @since 3.0
 */
@Serializable
data class Configuration(
    val appAuthor: String,
    val appVersionNumber: String,
    val appVersionName: String,
    val appVersionURL: String
)