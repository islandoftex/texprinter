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
 * <b>Question.java</b>: This class is a simple POJO to handle question.
 * Well, not so simple, but it aims at encapsulating the logic in it.
 */

// package definition
package net.sf.texprinter.model;

// needed packages
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import net.sf.texprinter.utils.MessagesHelper;

/**
 * Provides a simple POJO to handle question. Well, not so simple, but it
 * aims at encapsulating the logic in it.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0.2
 * @since 1.0
 */
public class Question {

    // the question
    private Post question;
    
    // the answers
    private ArrayList<Post> answers;
    
    // this flag controls any exception and thus may invalidate this
    // question
    private boolean poisoned = false;

    /**
     * Constructor method. It fetches the online question and sets the
     * attributes defined above.
     * @param questionLink The TeX.SX question link.
     * @param isCommandLine A flag to determine if this program is being
     * called from the command line.
     */
    public Question(String questionLink, boolean isCommandLine) {
        
        // lets try to fetch data
        try {
            
            // connect and fetch data
            Document doc = Jsoup.connect(questionLink).get();

            // get the question title
            Element questionTitle = doc.select("div#question-header").first().select("h1").first().select("a").first();
            
            // get the question date
            Element questionDate = doc.select(".post-signature").first().select("span.relativetime").first();
            
            // get question text
            Element questionText = doc.select("div.post-text").first();
            
            // get the question comment elements
            Elements questionCommentElements = doc.select("div.comments").first().select("tr.comment");

            // create a new list of question comments
            ArrayList<Comment> questionComments = new ArrayList<Comment>();

            // if there are comments on this question
            if (!questionCommentElements.isEmpty()) {

                // for each question comment found
                for (Element questionCommentElement : questionCommentElements) {
                    
                    // create a new comment
                    Comment c = new Comment();
                    
                    // set the comment text
                    c.setText(questionCommentElement.select("span.comment-copy").first().html());
                    
                    // set the comment author
                    c.setAuthor(questionCommentElement.select("a.comment-user").first().text());
                    
                    // set the comment date
                    c.setDate(questionCommentElement.select("span.comment-date").first().text());
                    
                    // add this new comment to the list of question comments
                    questionComments.add(c);
                    
                    // set the old comment to null, just to play it safe
                    c = null;
                }
            }

            // get the question author
            Element authorName = doc.select(".post-signature.owner").first().select("div.user-details").first().select("a").first();
            
            // get the author reputation
            Element authorReputation = doc.select(".post-signature.owner").first().select("div.user-details").first().select("span.reputation-score").first();

            // create a new post
            Post q = new Post();
            
            // set the title
            q.setTitle(questionTitle.text());
            
            // set the date
            q.setDate(questionDate.text());
            
            // set the text
            q.setText(questionText.html());

            // set the comments
            q.setComments(questionComments);

            // then we create a new user
            User u = new User();
            
            // set the name
            u.setName(authorName.text());
            
            // set the reputation
            u.setReputation(authorReputation.text());

            // set this new user to the question
            q.setUser(u);

            // and set this new object as an instance of the
            // attribute of this very same classe
            this.question = q;

            // now we get the block destinated to answers
            Elements answersBlock = doc.select("div.answer");

            // set a new list of answers, empty at first
            this.answers = new ArrayList<Post>();

            // if the block of answers has any answer
            if (!answersBlock.isEmpty()) {

                // create a list to hold the authors names
                Elements authorsNames = new Elements();
                
                // create a list to hold the authors reputations
                Elements authorsReputations = new Elements();
                
                // and we create a list to hold the authors dates
                Elements authorsDates = new Elements();

                // we need to fix the authors, since the TeX.SX template does
                // not make a distinction between users that post answers than
                // users that edit questions, e.g., if we have 4 answers and
                // 2 of them were edited by moderators, we will have 6 users
                // found in the template for answers. The following code aims
                // at fixing it.
                
                // get the user info block
                Elements fixAuthors = answersBlock.select("div.user-info");
                
                // for each user found
                for (Element fixAuthor : fixAuthors) {
                    
                    // if the author answered and not edited the question
                    if (fixAuthor.select("div.user-action-time").text().startsWith("answered")) {
                        
                        // add the author name
                        authorsNames.add(fixAuthor.select("div.user-details").select("a").first());
                        
                        // add the author reputation
                        authorsReputations.add(fixAuthor.select("div.user-details").select("span.reputation-score").first());
                        
                        // add the author date
                        authorsDates.add(fixAuthor.select("div.user-info").select("span.relativetime").first());
                    }
                }

                // set the answer text
                Elements answersTexts = answersBlock.select("div.post-text");
                
                // get the answer vote block
                Elements theVotes = answersBlock.select("div.vote");

                // iterate through the answers
                for (int i = 0; i < authorsNames.size(); i++) {

                    // get the current answer comments block
                    Elements currentAnswerCommentsElements = answersBlock.get(i).select("div.comments").first().select("tr.comment");

                    // create a new list of comments for the current answer
                    ArrayList<Comment> currentAnswerComments = new ArrayList<Comment>();

                    // if there are comments for this answer
                    if (!currentAnswerCommentsElements.isEmpty()) {

                        // for each comment found
                        for (Element currentAnswerCommentElement : currentAnswerCommentsElements) {
                            
                            // create a new comment
                            Comment c = new Comment();
                            
                            // set the comment text
                            c.setText(currentAnswerCommentElement.select("span.comment-copy").first().html());
                            
                            // set the comment author
                            c.setAuthor(currentAnswerCommentElement.select("a.comment-user").first().text());
                            
                            // set the comment date
                            c.setDate(currentAnswerCommentElement.select("span.comment-date").first().text());
                            
                            // add this comment to the list
                            currentAnswerComments.add(c);
                            
                            // lets play it safe, set the old comment to null
                            c = null;
                        }
                    }

                    // lets create a new post, which is an answer
                    Post ps = new Post();
                    
                    // an answer has no title, so we set to an empty string
                    ps.setTitle("");
                    
                    // set the date
                    ps.setDate(authorsDates.get(i).text());
                    
                    // set the text
                    ps.setText(answersTexts.get(i).html());

                    // set the comment
                    ps.setComments(currentAnswerComments);
                    
                    // if the votes for this answer indicate that it's accepted
                    if (!theVotes.get(i).getElementsByClass("vote-accepted-on").isEmpty()) {
                        
                        // mark it as accepted
                        ps.setAccepted(true);
                    }

                    // create a new user
                    User us = new User();
                    
                    // set the name
                    us.setName(authorsNames.get(i).text());
                    
                    // set the reputation
                    us.setReputation(authorsReputations.get(i).text());

                    // add the user to the answer
                    ps.setUser(us);

                    // add the answer to the list of answers
                    this.answers.add(ps);
                    
                    // set the current post to null
                    ps = null;
                    
                    // set the current user to null
                    us = null;
                }
            }
        } catch (Exception e) {
            
            // something bad happened, lets catch the exception
            
            // if this application is executed from the command line
            if (isCommandLine) {
                
                // print the message
                System.out.println("\nException: " + e.getMessage() + "\n");
                
            } else {
                
                // display a fancy message on screen
                MessagesHelper.exception(e);
            }
            
            // an error occurred, so this question is poisoned
            poisoned = true;
        }

    }

    /**
     * Check if this question is poisoned.
     * @return A boolean to indicate the state of the question.
     */
    public boolean isPoisoned() {
        
        // return the flag
        return this.poisoned;
    }

    /**
     * Getter for the answers.
     * @return A list of answers.
     */
    public ArrayList<Post> getAnswers() {
        
        // return a list of answers
        return answers;
    }

    /**
     * Setter for the answers.
     * @param answers A list of answers.
     */
    public void setAnswers(ArrayList<Post> answers) {
        
        // set the list of answers
        this.answers = answers;
    }

    /**
     * Getter for the question, which happens to be a <code>Post</code> object.
     * @return The question itself.
     */
    public Post getQuestion() {
        
        // return the question
        return question;
    }

    /**
     * Setter for the question.
     * @param question The question.
     */
    public void setQuestion(Post question) {
        
        // set the question
        this.question = question;
    }
}
