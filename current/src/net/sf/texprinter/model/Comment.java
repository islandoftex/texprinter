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
 * <b>Comment.java</b>: This class is a simple POJO to handle comments.
 */

// package definition
package net.sf.texprinter.model;

/**
 * Provides a simple POJO to handle comments.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0.2
 * @since 1.0
 */
public class Comment {

    // the comment text
    private String text;
    // the author name
    private String author;
    // the date
    private String date;

    /**
     * Getter for the author name. I had to remove the diamond symbol when
     * found. A user which happens to be a moderator, by a design feature,
     * has a diamond symbol attached to his name.
     * @return The author name.
     */
    public String getAuthor() {

        // replace the diamond
        author = author.replaceAll("♦", "");

        // return the author name
        return author;
    }

    /**
     * Setter for the author name.
     * @param author The author name.
     */
    public void setAuthor(String author) {

        // set the author name
        this.author = author;
    }

    /**
     * Getter for the date.
     * @return The comment date.
     */
    public String getDate() {

        // return the date
        return date;
    }

    /** 
     * Setter for the date.  Since StackOverflow displays dates in a pretty
     * nice format, I decide to leave the date String as it is.
     * @param date The comment date.
     */
    public void setDate(String date) {

        // set the date
        this.date = date;
    }

    /**
     * Getter for the comment text.
     * @return The comment text.
     */
    public String getText() {

        // return the text
        return text;
    }

    /**
     * Setter for the comment text.  The text is hold in the HTML format.
     * @param text The comment text.
     */
    public void setText(String text) {

        // set the comment text
        this.text = text;
    }
}
