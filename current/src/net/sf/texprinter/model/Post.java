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
 * Copyright (c) 2012, Paulo Roberto Massa Cereda
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
 * Post.java: This class is a simple POJO to handle posts, which can be
 * a question or an answer.
 * Last revision: paulo at temperantia 26 Feb 2012 06:00
 */
// package definition
package net.sf.texprinter.model;

// needed imports
import java.util.ArrayList;

/**
 * Provides a simple POJO to handle posts, which can be a question or
 * an answer.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 1.0
 */
public class Post {

    // the post title
    private String title;
    
    // the post text
    private String text;
    
    // the post date
    private String date;
    
    // the user who posted it
    private User user;
    
    // this is a special flag for posts that happen to be answers:
    // if an answer is accepted, this flag is set to true
    private boolean accepted = false;
    
    // the votes
    private int votes;
    
    // a list of comments to this post, if any
    private ArrayList<Comment> comments;

    /**
     * Checks if this post is accepted. It makes sense only if the post
     * is an answer.
     * @return A boolean on either the post is accepted or not.
     */
    public boolean isAccepted() {

        // return the flag
        return accepted;
    }

    /**
     * Setter for the accepted flag.
     * 
     * @param accepted The flag.
     */
    public void setAccepted(boolean accepted) {

        // return the flag
        this.accepted = accepted;
    }

    /**
     * Getter for the post date.
     * 
     * @return The post date.
     */
    public String getDate() {

        // return the date
        return date;
    }

    /**
     * Setter for the post date.
     * 
     * @param date The post date.
     */
    public void setDate(String date) {

        // set the date
        this.date = date;
    }

    /**
     * Getter for the post text.
     * 
     * @return The post text.
     */
    public String getText() {

        // return the post text
        return text;
    }

    /**
     * Setter for the post text.
     * 
     * @param text The post text.
     */
    public void setText(String text) {

        // set the post text
        this.text = text;
    }

    /**
     * Getter for the post title.
     * 
     * @return The post title.
     */
    public String getTitle() {

        // return the post title
        return title;
    }

    /**
     * Setter for the post title.
     * 
     * @param title The post title.
     */
    public void setTitle(String title) {

        // set the post title
        this.title = title;
    }

    /**
     * Getter for the post user.
     * 
     * @return The post user.
     */
    public User getUser() {

        // return the post user
        return user;
    }

    /**
     * Setter for the post user.
     * 
     * @param user The post user.
     */
    public void setUser(User user) {

        // set the post user.
        this.user = user;
    }

    /**
     * Getter for the post comments, if any.
     * 
     * @return An array with the post comments.
     */
    public ArrayList<Comment> getComments() {

        // return the array with the post comments
        return comments;
    }

    /**
     * Setter for the post comments, if any.
     * 
     * @param comments An array with the post comments.
     */
    public void setComments(ArrayList<Comment> comments) {

        // set the post comments
        this.comments = comments;
    }

    /**
     * Getter for the number of votes.
     * 
     * @return The number of votes.
     */
    public int getVotes() {

        // return the votes
        return votes;
    }

    /**
     * Setter for the number of votes.
     * 
     * @param votes The number of votes.
     */
    public void setVotes(int votes) {

        // set the number of votes
        this.votes = votes;
    }
}
