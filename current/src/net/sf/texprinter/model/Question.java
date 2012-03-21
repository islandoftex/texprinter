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
 * Question.java: This class is a simple POJO to handle question.
 * Well, not so simple, but it aims at encapsulating the logic in it.
 * Last revision: paulo at iustitia 20 Mar 2012 06:02
 */

// package definition
package net.sf.texprinter.model;

// needed packages
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.PostComparator;
import net.sf.texprinter.utils.ProgressMessage;
import net.sf.texprinter.utils.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Provides a simple POJO to handle question. Well, not so simple, but it
 * aims at encapsulating the logic in it.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 1.0
 */
public class Question {

    // the application logger
    private static final Logger log = Logger.getLogger(Question.class.getCanonicalName());
    
    // the question
    private Post question;
    
    // the answers
    private ArrayList<Post> answers;

     /**
     * Default constructor. It fetches the online question and sets the
     * attributes defined above.
     * 
     * @param questionLink The TeX.SX question link.
     */
    public Question(String questionLink) {
        
        // create a new progress message
        ProgressMessage pm = new ProgressMessage("I'm fetching the question.");
                
        // lets try to fetch data
        try {

            // log message
            log.log(Level.INFO, "Fetching the following question link: {0}", questionLink);
            
            // fetch the question
            Document doc = Jsoup.connect(questionLink).get();

            // new post to act as the question
            Post q = new Post();

            // get the question title
            Element questionTitle = doc.select("div#question-header").first().select("h1").first().select("a").first();

            // log message
            log.log(Level.INFO, "Setting the question title.");
            
            // set the title            
            q.setTitle(questionTitle.text());

            // trying to get the question date
            Element questionDate = null;
            
            // try
            try {
                
                // handles this possibility
                questionDate = doc.select(".post-signature.owner").first().select("span.relativetime").first();
                
            } catch (Exception e) {
                
                // in case of failure, try this one instead
                questionDate = doc.select(".post-signature").first().select("span.relativetime").first();
            }

            // log message
            log.log(Level.INFO, "Setting the question date.");
            
            // set the date
            q.setDate(questionDate.text());

            // get the question text
            Element questionText = doc.select("div.post-text").first();
            
            // log message
            log.log(Level.INFO, "Setting the question text.");
            
            // set the question text
            q.setText(questionText.html());

            // get the question votes
            Element questionVote = doc.select("div#question").first().select("div.vote").first().select("span.vote-count-post").first();
            
            // log message
            log.log(Level.INFO, "Setting the question votes.");
            
            // set the votes
            q.setVotes(Integer.parseInt(questionVote.text()));
            
            // get the question comments
            Elements questionCommentElements = doc.select("div.comments").first().select("tr.comment");


            // create an array for comments
            ArrayList<Comment> questionComments = new ArrayList<Comment>();

            // if there are comments
            if (!questionCommentElements.isEmpty()) {

                // log message
                log.log(Level.INFO, "This question has comments, getting them.");

                // iterate through comments
                for (Element questionCommentElement : questionCommentElements) {

                    // create a new comment object
                    Comment c = new Comment();

                    // get the text
                    c.setText(questionCommentElement.select("span.comment-copy").first().html());
                    
                    // get the author
                    c.setAuthor(questionCommentElement.select(".comment-user").first().text());
                    
                    // get the date
                    c.setDate(questionCommentElement.select("span.comment-date").first().text());

                    // the comment votes
                    int votes;
                    
                    // lets try to parse
                    try {
                        
                        // parse the votes
                        votes = Integer.parseInt(questionCommentElement.select("span.cool").first().text());
                        
                    } catch (Exception e) {
                        
                        // an error happened, set it to zero
                        votes = 0;
                    }
                    
                    // set the votes
                    c.setVotes(votes);
                    
                    // add to the comments array
                    questionComments.add(c);
                    
                    // set the object to null
                    c = null;
                }
            }

            // log message
            log.log(Level.INFO, "Comments retrieved, setting them to the question.");
            
            // set comments
            q.setComments(questionComments);

            // define the author name element
            Element authorName = null;
            
            // define the author reputation element
            Element authorReputation = null;
            
            // log message
            log.log(Level.INFO, "Getting the question author name and reputation.");
            
            // lets try
            try {
                
                // get the name
                authorName = doc.select(".post-signature.owner").first().select("div.user-details").first();
                
                // get the reputation
                authorReputation = doc.select(".post-signature.owner").first().select("div.user-details").first().select("span.reputation-score").first();
                
            } catch (Exception e) {
                
                // something wrong happened, trying to get the name again
                authorName = doc.select(".post-signature").get(1).select("div.user-details").get(1);
            }

            // set the temp author name
            String authorNameStr = "";

            // check if this is a special question
            if (!authorName.getElementsByTag("a").isEmpty() && authorName.getElementsByTag("a").html().contains("/>")) {
                
                // get the author name
                authorNameStr = authorName.getElementsByTag("a").html();
                
            } else {
                
                // lets try again
                try {
                    
                    // get the author name
                    authorNameStr = authorName.getElementsByTag("a").first().text();
                    
                } catch (Exception e) {
                    
                    // another error, lets try again
                    authorNameStr = authorName.text();
                }
            }

            // check if the author name needs to be retrieved from the string
            if (authorNameStr.indexOf("/>") != -1) {
                
                // get the substring
                authorNameStr = authorNameStr.substring(authorNameStr.indexOf("/>") + 2);
            }

            // log message
            log.log(Level.INFO, "Creating a new user.");
            
            // a new user is created
            User u = new User();

            // log message
            log.log(Level.INFO, "Setting the user name.");
            
            // set the user name
            u.setName(authorNameStr);
            
            // temp string for reputation
            String authorReputationStr = "";

            // log message
            log.log(Level.INFO, "Checking user reputation.");
            
            // check if it is a normal question
            if (doc.select("div#question").select("span.community-wiki").isEmpty()) {
                
                // check if it is a migrated question
                if (authorName.getElementsByTag("a").isEmpty()) {
                    
                    // log message
                    log.log(Level.INFO, "This is a migrated question.");
                    
                    // set the reputation
                    authorReputationStr = "Migrated question";
                    
                } else {
                    
                    // log message
                    log.log(Level.INFO, "This is a normal question.");
                    
                    // normal question
                    authorReputationStr = authorReputation.text();
                }
            } else {
                // log message
                log.log(Level.INFO, "This is a community wiki question.");
                
                // set the reputation
                authorReputationStr = "Community Wiki";
            }

            // log message
            log.log(Level.INFO, "Setting the user reputation.");
            
            // set the reputation
            u.setReputation(authorReputationStr);

            // log message
            log.log(Level.INFO, "Adding the user to the question.");
            
            // add the user to the question
            q.setUser(u);

            // set the class variable
            this.question = q;

            // create a new array
            this.answers = new ArrayList<Post>();

            // log message
            log.log(Level.INFO, "Getting the answers.");
            
            // fetching the answers block
            Elements answersBlock = doc.select("div.answer");
            
            // check if there are answers
            if (!answersBlock.isEmpty()) {

                // log message
                log.log(Level.INFO, "Answers found, retrieving them.");

                // get the authors block
                Elements answerAuthorsBlock = answersBlock.select("table.fw");
                
                // counter for the loop
                int counter = 0;

                // iterate now
                for (Element currentAnswerAuthor : answerAuthorsBlock) {

                    // log message
                    log.log(Level.INFO, "Getting answer {0}.", (counter + 1));

                    // set new post
                    Post a = new Post();
                    
                    // set new user
                    User ua = new User();

                    // the temp author name
                    String answerAuthorNameStr = "";
                    
                    // check if it is a valid entry
                    if (currentAnswerAuthor.select("div.user-details").last().getElementsByTag("a").isEmpty()) {
                        
                        // set the value
                        answerAuthorNameStr = currentAnswerAuthor.select("div.user-details").last().text();
                        
                    } else {
                        
                        // try another approach
                        answerAuthorNameStr = currentAnswerAuthor.select("div.user-details").last().getElementsByTag("a").first().html();
                    }

                    // check if user name has to be trimmed
                    if (answerAuthorNameStr.indexOf("/>") != -1) {
                        
                        // get the substring
                        answerAuthorNameStr = answerAuthorNameStr.substring(answerAuthorNameStr.indexOf("/>") + 2);
                    }

                    // log message
                    log.log(Level.INFO, "Setting the author name for answer {0}.", (counter + 1));
                    
                    // set the author
                    ua.setName(answerAuthorNameStr);

                    // check if it has reputation
                    if (currentAnswerAuthor.select("div.user-details").last().select("span.reputation-score").isEmpty()) {
                        
                        // it is a community wiki
                        if (!currentAnswerAuthor.select("span.community-wiki").isEmpty()) {
                            
                            // log message
                            log.log(Level.INFO, "Answer {0} is community wiki.", (counter + 1));
                            
                            // set the reputation
                            ua.setReputation("Community Wiki");
                            
                        } else {
                            
                            // log message
                            log.log(Level.INFO, "Answer {0} is a migrated answer.", (counter + 1));
                            
                            // it is a migrated question
                            ua.setReputation("Migrated answer");
                        }
                    } else {
                        
                        // log message
                        log.log(Level.INFO, "Answer {0} is a normal answer.", (counter + 1));
                        
                        // normal answer
                        ua.setReputation(currentAnswerAuthor.select("div.user-details").last().select("span.reputation-score").first().text());
                    }

                    // log message
                    log.log(Level.INFO, "Adding user to answer {0}.", (counter + 1));
                    
                    // add user
                    a.setUser(ua);

                    // log message
                    log.log(Level.INFO, "Adding date for answer {0}.", (counter + 1));
                    
                    // add date
                    a.setDate(currentAnswerAuthor.select("div.user-info").select("span.relativetime").first().text());

                    // log message
                    log.log(Level.INFO, "Adding text for answer {0}.", (counter + 1));
                    
                    // get text
                    Elements answersTexts = answersBlock.select("div.post-text");
                    
                    // add text
                    a.setText(answersTexts.get(counter).html());

                    // get the votes
                    Elements theVotes = answersBlock.select("div.vote");
                    
                    // log message
                    log.log(Level.INFO, "Adding votes for answer {0}.", (counter + 1));
                    
                    // set the votes
                    a.setVotes(Integer.parseInt(theVotes.get(counter).select("span.vote-count-post").first().text()));

                    // check if it is accepted
                    if (!theVotes.get(counter).getElementsByClass("vote-accepted-on").isEmpty()) {
                        
                        // log message
                    log.log(Level.INFO, "Answer {0} is accepted.", (counter + 1));
                        
                        // set accepted
                        a.setAccepted(true);
                    }

                    // create the comments array
                    ArrayList<Comment> currentAnswerComments = new ArrayList<Comment>();

                    // answers comments
                    Elements currentAnswerCommentsElements = answersBlock.get(counter).select("div.comments").first().select("tr.comment");

                    // log message
                    log.log(Level.INFO, "Checking comments for answer {0}.", (counter + 1));
                    
                    // if the answer has comments
                    if (!currentAnswerCommentsElements.isEmpty()) {

                        // log message
                        log.log(Level.INFO, "Adding comments for answer {0}.", (counter + 1));

                        // iterate
                        for (Element currentAnswerCommentElement : currentAnswerCommentsElements) {

                            // create a new comment
                            Comment ca = new Comment();

                            // set the text
                            ca.setText(currentAnswerCommentElement.select("span.comment-copy").first().html());

                            try {
                                
                                // try to set the author
                                ca.setAuthor(currentAnswerCommentElement.select("a.comment-user").first().text());
                                
                            } catch (Exception e) {
                                
                                // fix it
                                ca.setAuthor(currentAnswerCommentElement.select("span.comment-user").first().text());
                            }

                            // set date
                            ca.setDate(currentAnswerCommentElement.select("span.comment-date").first().text());

                            // the comment votes
                            int votes;
                            
                            // lets try
                            try {
                                
                                // try to parse it
                                votes = Integer.parseInt(currentAnswerCommentElement.select("span.cool").first().text());
                                
                            } catch (Exception e) {
                                
                                // set default to zero
                                votes = 0;
                                
                            }
                            
                            // set votes
                            ca.setVotes(votes);

                            // set the current comment to the list of comments
                            currentAnswerComments.add(ca);
                            
                            // set the current comment to null
                            ca = null;

                        }

                    } else {
                        
                        // log message
                        log.log(Level.INFO, "No comments for answer {0}.", (counter + 1));
                        
                    }

                    // set the comments
                    a.setComments(currentAnswerComments);

                    // set to the class variable
                    this.answers.add(a);

                    // increments counter
                    counter++;

                }
                
                // log message
                log.log(Level.INFO, "All answers added.");

            } else {
                
                // log message
                log.log(Level.INFO, "There are no answers for this question.");
                
            }
            
            // stop the wait window
            pm.interrupt();

        } catch (IOException ioex) {

            // log message
            log.log(Level.SEVERE, "An IO error occurred while trying to fetch and set the question data. Possibly a 404 page. MESSAGE: {0}", StringUtils.printStackTrace(ioex));
            
            // stop the wait window
            pm.interrupt();
            
            // show dialog
            Dialogs.showNotFoundWindow();
            
        } catch (Exception excep) {
            
            // log message
            log.log(Level.SEVERE, "A generic error occurred while trying to fetch and set the question data. MESSAGE: {0}", StringUtils.printStackTrace(excep));
            
            // stop the wait window
            pm.interrupt();
            
            Dialogs.showNotFoundWindow();
            
        }
    }

    /**
     * Getter for the answers.
     * 
     * @return A list of answers.
     */
    public ArrayList<Post> getAnswers() {

        // sort the answers with the post comparator
        Collections.sort(answers, new PostComparator());

        // return a list of answers
        return answers;
    }

    /**
     * Setter for the answers.
     * 
     * @param answers A list of answers.
     */
    public void setAnswers(ArrayList<Post> answers) {

        // set the list of answers
        this.answers = answers;
    }

    /**
     * Getter for the question, which happens to be a <code>Post</code> object.
     * 
     * @return The question itself.
     */
    public Post getQuestion() {

        // return the question
        return question;
    }

    /**
     * Setter for the question.
     * 
     * @param question The question.
     */
    public void setQuestion(Post question) {

        // set the question
        this.question = question;
    }
}
