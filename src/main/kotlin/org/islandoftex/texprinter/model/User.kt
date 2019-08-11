// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.model

/**
 * Provides a simple POJO to handle users.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
data class User(
    // the user name
    var name: String = "",
    // the user reputation
    var reputation: String = "")
