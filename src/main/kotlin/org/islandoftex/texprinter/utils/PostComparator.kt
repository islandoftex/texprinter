/******************************************************************************
 * Copyright 2012-2018 Paulo Roberto Massa Cereda                             *
 *                                                                            *
 * Redistribution and use in source and binary forms, with or                 *
 *  without modification, are permitted provided that the following           *
 *  conditions are met:                                                       *
 *                                                                            *
 * 1. Redistributions of source code must retain the above copyright          *
 * notice, this list of conditions and the following disclaimer.              *
 *                                                                            *
 * 2. Redistributions in binary form must reproduce the above copyright       *
 * notice, this list of conditions and the following disclaimer in the        *
 * documentation and/or other materials provided with the distribution.       *
 *                                                                            *
 * 3. Neither the name of the copyright holder nor the names of it            *
 *  contributors may be used to endorse or promote products derived           *
 *  from this software without specific prior written permission.             *
 *                                                                            *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS       *
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT         *
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS         *
 *  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE            *
 *  COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,      *
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,      *
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS     *
 *  OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND    *
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR     *
 *  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE    *
 *  USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *
 ******************************************************************************/
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
