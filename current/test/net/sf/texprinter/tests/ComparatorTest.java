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
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Neither the name of the project's author nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
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
 * <b>TeXPrinter.java</b>: Comparator tests.
 */

// package definition
package net.sf.texprinter.tests;

// imports
import java.util.ArrayList;
import java.util.Collections;
import net.sf.texprinter.model.Post;
import java.util.PriorityQueue;
import net.sf.texprinter.utils.PostComparator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comparator tests.
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.1
 */
public class ComparatorTest {
    
    public ComparatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void checkPostsWithNoAcceptancePriorityQueue() {
        
        PriorityQueue<Post> queue = new PriorityQueue<Post>(1, new PostComparator());
        
        Post p1 = new Post();
        p1.setAccepted(false);
        p1.setVotes(7);
        
        Post p2 = new Post();
        p2.setAccepted(false);
        p2.setVotes(10);
        
        Post p3 = new Post();
        p3.setAccepted(false);
        p3.setVotes(5);
        
        queue.add(p1);
        queue.add(p2);
        queue.add(p3);
        
        assertTrue(queue.remove().getVotes() == 10);
        assertTrue(queue.remove().getVotes() == 7);
        assertTrue(queue.remove().getVotes() == 5);
    }
    
    @Test
    public void checkPostsWithAcceptancePriorityQueue() {
        
        PriorityQueue<Post> queue = new PriorityQueue<Post>(1, new PostComparator());
        
        Post p1 = new Post();
        p1.setAccepted(true);
        p1.setVotes(7);
        
        Post p2 = new Post();
        p2.setAccepted(false);
        p2.setVotes(10);
        
        Post p3 = new Post();
        p3.setAccepted(false);
        p3.setVotes(5);
        
        queue.add(p1);
        queue.add(p2);
        queue.add(p3);
        
        assertTrue(queue.remove().getVotes() == 7);
        assertTrue(queue.remove().getVotes() == 10);
        assertTrue(queue.remove().getVotes() == 5);
    }
    
    @Test
    public void checkPostsWithAllAcceptedPriorityQueue() {
        
        PriorityQueue<Post> queue = new PriorityQueue<Post>(1, new PostComparator());
        
        Post p1 = new Post();
        p1.setAccepted(true);
        p1.setVotes(7);
        
        Post p2 = new Post();
        p2.setAccepted(true);
        p2.setVotes(10);
        
        Post p3 = new Post();
        p3.setAccepted(true);
        p3.setVotes(5);
        
        queue.add(p1);
        queue.add(p2);
        queue.add(p3);
        
        assertTrue(queue.remove().getVotes() == 10);
        assertTrue(queue.remove().getVotes() == 7);
        assertTrue(queue.remove().getVotes() == 5);
    }
    
    @Test
    public void checkPostsWithNoAcceptanceArrayList() {
        
        ArrayList<Post> list = new ArrayList<Post>();
        
        Post p1 = new Post();
        p1.setAccepted(false);
        p1.setVotes(7);
        
        Post p2 = new Post();
        p2.setAccepted(false);
        p2.setVotes(10);
        
        Post p3 = new Post();
        p3.setAccepted(false);
        p3.setVotes(5);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        
        Collections.sort(list, new PostComparator());
        
        assertTrue(list.get(0).getVotes() == 10);
        assertTrue(list.get(1).getVotes() == 7);
        assertTrue(list.get(2).getVotes() == 5);
    }
    
    @Test
    public void checkPostsWithAcceptanceArrayList() {
        
        ArrayList<Post> list = new ArrayList<Post>();
        
        Post p1 = new Post();
        p1.setAccepted(true);
        p1.setVotes(7);
        
        Post p2 = new Post();
        p2.setAccepted(false);
        p2.setVotes(10);
        
        Post p3 = new Post();
        p3.setAccepted(false);
        p3.setVotes(5);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        
        Collections.sort(list, new PostComparator());
        
        assertTrue(list.get(0).getVotes() == 7);
        assertTrue(list.get(1).getVotes() == 10);
        assertTrue(list.get(2).getVotes() == 5);
    }
    
    @Test
    public void checkPostsWithAllAcceptedArrayList() {
        
        ArrayList<Post> list = new ArrayList<Post>();
        
        Post p1 = new Post();
        p1.setAccepted(true);
        p1.setVotes(7);
        
        Post p2 = new Post();
        p2.setAccepted(true);
        p2.setVotes(10);
        
        Post p3 = new Post();
        p3.setAccepted(true);
        p3.setVotes(5);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        
        Collections.sort(list, new PostComparator());
        
        assertTrue(list.get(0).getVotes() == 10);
        assertTrue(list.get(1).getVotes() == 7);
        assertTrue(list.get(2).getVotes() == 5);
    }
    
}
