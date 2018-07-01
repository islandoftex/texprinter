/******************************************************************************
 * Copyright 2011-2018 Paulo Roberto Massa Cereda and Ben Frank               *
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

package com.gitlab.cereda.texprinter.tests

import com.gitlab.cereda.texprinter.model.Post
import com.gitlab.cereda.texprinter.utils.PostComparator
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import java.util.*

/**
 * Tests for PostComparator
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.1
 */
class PostComparatorTest : ShouldSpec({
  "PriorityQueue" {
    should("check posts with no acceptance PriorityQueue") {
      val queue = PriorityQueue<Post>(1, PostComparator())

      queue.addAll(listOf(
          Post().apply {
            isAccepted = false
            votes = 7
          },
          Post().apply {
            isAccepted = false
            votes = 10
          },
          Post().apply {
            isAccepted = false
            votes = 5
          }
      ))

      queue.remove().votes shouldBe 10
      queue.remove().votes shouldBe 7
      queue.remove().votes shouldBe 5
    }
    should("check posts with acceptance PriorityQueue") {
      val queue = PriorityQueue<Post>(1, PostComparator())

      queue.addAll(listOf(
          Post().apply {
            isAccepted = true
            votes = 7
          },
          Post().apply {
            isAccepted = false
            votes = 10
          },
          Post().apply {
            isAccepted = false
            votes = 5
          }
      ))

      queue.remove().votes shouldBe 7
      queue.remove().votes shouldBe 10
      queue.remove().votes shouldBe 5
    }
    should("check posts with all accepted PriorityQueue") {
      val queue = PriorityQueue<Post>(1, PostComparator())

      queue.addAll(listOf(
          Post().apply {
            isAccepted = true
            votes = 7
          },
          Post().apply {
            isAccepted = true
            votes = 10
          },
          Post().apply {
            isAccepted = true
            votes = 5
          }
      ))

      queue.remove().votes shouldBe 10
      queue.remove().votes shouldBe 7
      queue.remove().votes shouldBe 5
    }
  }

  "ArrayList" {
    should("check posts with no acceptance ArrayList") {
      val list = arrayListOf(
          Post().apply {
            isAccepted = false
            votes = 7
          },
          Post().apply {
            isAccepted = false
            votes = 10
          },
          Post().apply {
            isAccepted = false
            votes = 5
          }
      )

      Collections.sort(list, PostComparator())

      list[0].votes shouldBe 10
      list[1].votes shouldBe 7
      list[2].votes shouldBe 5
    }

    should("check posts with acceptance ArrayList") {
      val list = arrayListOf(
          Post().apply {
            isAccepted = true
            votes = 7
          },
          Post().apply {
            isAccepted = false
            votes = 10
          },
          Post().apply {
            isAccepted = false
            votes = 5
          }
      )

      Collections.sort(list, PostComparator())

      list[0].votes shouldBe 7
      list[1].votes shouldBe 10
      list[2].votes shouldBe 5
    }

    should("check posts with all accepted ArrayList") {
      val list = arrayListOf(
          Post().apply {
            isAccepted = true
            votes = 7
          },
          Post().apply {
            isAccepted = true
            votes = 10
          },
          Post().apply {
            isAccepted = true
            votes = 5
          }
      )

      Collections.sort(list, PostComparator())

      list[0].votes shouldBe 10
      list[1].votes shouldBe 7
      list[2].votes shouldBe 5
    }
  }
})