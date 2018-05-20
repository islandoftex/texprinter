/**
 * \cond LICENSE
 * ********************************************************************
 * This is a conditional block for preventing the DoxyGen documentation
 * tool to include this license header within the description of each
 * source code file. If you want to include this block, please define
 * the LICENSE parameter into the provided DoxyFile.
 * ********************************************************************
 *
 * TeXPrinter - A TeX.SX question printer
 * Copyright (c) 2011, Paulo Roberto Massa Cereda
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. Neither the name of the project's author nor the names of its
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ********************************************************************
 * End of the LICENSE conditional block
 * ********************************************************************
 * \endcond
 *
 * **TeXPrinter.java**: Comparator tests.
 */

// package definition
package com.gitlab.cereda.texprinter.tests

// imports
import java.util.ArrayList
import java.util.Collections
import com.gitlab.cereda.texprinter.model.Post
import com.gitlab.cereda.texprinter.utils.PostComparator
import java.util.PriorityQueue
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.Assert.*

/**
 * Comparator tests.
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.1
 */
class ComparatorTest {

  @Before
  fun setUp() {
  }

  @After
  fun tearDown() {
  }

  @Test
  fun checkPostsWithNoAcceptancePriorityQueue() {
    val queue = PriorityQueue<Post>(1, PostComparator())

    val p1 = Post()
    p1.isAccepted = false
    p1.votes = 7

    val p2 = Post()
    p2.isAccepted = false
    p2.votes = 10

    val p3 = Post()
    p3.isAccepted = false
    p3.votes = 5

    queue.add(p1)
    queue.add(p2)
    queue.add(p3)

    assertTrue(queue.remove().votes == 10)
    assertTrue(queue.remove().votes == 7)
    assertTrue(queue.remove().votes == 5)
  }

  @Test
  fun checkPostsWithAcceptancePriorityQueue() {

    val queue = PriorityQueue<Post>(1, PostComparator())

    val p1 = Post()
    p1.isAccepted = true
    p1.votes = 7

    val p2 = Post()
    p2.isAccepted = false
    p2.votes = 10

    val p3 = Post()
    p3.isAccepted = false
    p3.votes = 5

    queue.add(p1)
    queue.add(p2)
    queue.add(p3)

    assertTrue(queue.remove().votes == 7)
    assertTrue(queue.remove().votes == 10)
    assertTrue(queue.remove().votes == 5)
  }

  @Test
  fun checkPostsWithAllAcceptedPriorityQueue() {

    val queue = PriorityQueue<Post>(1, PostComparator())

    val p1 = Post()
    p1.isAccepted = true
    p1.votes = 7

    val p2 = Post()
    p2.isAccepted = true
    p2.votes = 10

    val p3 = Post()
    p3.isAccepted = true
    p3.votes = 5

    queue.add(p1)
    queue.add(p2)
    queue.add(p3)

    assertTrue(queue.remove().votes == 10)
    assertTrue(queue.remove().votes == 7)
    assertTrue(queue.remove().votes == 5)
  }

  @Test
  fun checkPostsWithNoAcceptanceArrayList() {

    val list = ArrayList<Post>()

    val p1 = Post()
    p1.isAccepted = false
    p1.votes = 7

    val p2 = Post()
    p2.isAccepted = false
    p2.votes = 10

    val p3 = Post()
    p3.isAccepted = false
    p3.votes = 5

    list.add(p1)
    list.add(p2)
    list.add(p3)

    Collections.sort(list, PostComparator())

    assertTrue(list[0].votes == 10)
    assertTrue(list[1].votes == 7)
    assertTrue(list[2].votes == 5)
  }

  @Test
  fun checkPostsWithAcceptanceArrayList() {

    val list = ArrayList<Post>()

    val p1 = Post()
    p1.isAccepted = true
    p1.votes = 7

    val p2 = Post()
    p2.isAccepted = false
    p2.votes = 10

    val p3 = Post()
    p3.isAccepted = false
    p3.votes = 5

    list.add(p1)
    list.add(p2)
    list.add(p3)

    Collections.sort(list, PostComparator())

    assertTrue(list[0].votes == 7)
    assertTrue(list[1].votes == 10)
    assertTrue(list[2].votes == 5)
  }

  @Test
  fun checkPostsWithAllAcceptedArrayList() {
    val list = ArrayList<Post>()

    val p1 = Post()
    p1.isAccepted = true
    p1.votes = 7

    val p2 = Post()
    p2.isAccepted = true
    p2.votes = 10

    val p3 = Post()
    p3.isAccepted = true
    p3.votes = 5

    list.add(p1)
    list.add(p2)
    list.add(p3)

    Collections.sort(list, PostComparator())

    assertTrue(list[0].votes == 10)
    assertTrue(list[1].votes == 7)
    assertTrue(list[2].votes == 5)
  }

  companion object {

    @BeforeClass
    @Throws(Exception::class)
    fun setUpClass() {
    }

    @AfterClass
    @Throws(Exception::class)
    fun tearDownClass() {
    }
  }

}
