// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.tests

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import org.islandoftex.texprinter.model.Post
import org.islandoftex.texprinter.utils.PostComparator
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
      val queue = PriorityQueue(1, PostComparator())

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
      val queue = PriorityQueue(1, PostComparator())

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
      val queue = PriorityQueue(1, PostComparator())

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