// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.utils

import org.islandoftex.texprinter.model.Post
import java.util.*

/**
 * Implements a comparator for lists based on votes and acceptance marks. This
 * class is implemented to make sure better voted answers appear first.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.1
 */
class PostComparator : Comparator<Post> {

  /**
   * Compares two objects and return the priority. One answer has a higher
   * priority than the other if it has more votes or if it's accepted.
   *
   * @param o1 Object one.
   * @param o2 Object two.
   * @return The priority.
   */
  override fun compare(o1: Post, o2: Post): Int {
    // if both are accepted
    return if (o1.isAccepted && o2.isAccepted) {
      // the highest score comes first
      (if (o1.votes > o2.votes) +1 else if (o1.votes < o2.votes) -1 else 0) * -1
    } else {
      // only the first one is accepted
      if (o1.isAccepted) {
        // it comes first
        -1
      } else {
        // only the second one is accepted
        if (o2.isAccepted) {
          // it comes first
          +1
        } else {
          // the highest score comes first
          (if (o1.votes > o2.votes) +1 else if (o1.votes < o2.votes) -1 else 0) * -1
        }
      }
    }
  }
}
