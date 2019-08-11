// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.model

/**
 * Provides a simple POJO to handle comments.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
data class Comment(
  // the comment text
  var text: String = "",
  // the author name
  var author: String = "",
  // the date
  var date: String = "",
  // the votes
  var votes: Int = 0)
