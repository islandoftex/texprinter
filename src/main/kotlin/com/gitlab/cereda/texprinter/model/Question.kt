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
package com.gitlab.cereda.texprinter.model

import com.gitlab.cereda.texprinter.TeXPrinter
import com.gitlab.cereda.texprinter.utils.PostComparator
import com.gitlab.cereda.texprinter.utils.StringUtils
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.util.*

/**
 * Provides a simple POJO to handle question. Well, not so simple, but it
 * aims at encapsulating the logic in it.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
class Question
/**
 * Default constructor. It fetches the online question and sets the
 * attributes defined above.
 *
 * @param questionLink The TeX.SX question link.
 */
(questionLink: String) {

  companion object {
    private val logger = KotlinLogging.logger { }
  }

  // the question
  var question: Post = Post()

  // the answers
  var answers = arrayListOf<Post>()
    get() {
      // sort the answers with the post comparator
      Collections.sort(field, PostComparator())
      // return a list of answers
      return field
    }

  init {
    // lets try to fetch data
    try {
      // log message
      logger.info { "Fetching the following question link: $questionLink" }
      // fetch the question
      val doc = Jsoup.connect(questionLink).get()

      // new post to act as the question
      val q = Post()

      // get the question title
      val questionTitle = doc.select("div#question-header").first().select("h1").first().select("a").first()
      // log message
      logger.info { "Setting the question title." }
      // set the title
      q.title = questionTitle.text()

      // trying to get the question date
      val questionDate = try {
        // handles this possibility
        doc.select(".post-signature.owner").first().select("span.relativetime").first()
      } catch (_: Exception) {
        // in case of failure, try this one instead
        doc.select(".post-signature").first().select("span.relativetime").first()
      }
      // log message
      logger.info { "Setting the question date." }
      // set the date
      q.date = questionDate.text()

      // get the question text
      val questionText = doc.select("div.post-text").first()
      // log message
      logger.info { "Setting the question text." }
      // set the question text
      q.text = questionText.html()

      // get the question votes
      val questionVote = doc.select("div#question").first().select("div.vote").first().select("span.vote-count-post").first()
      // log message
      logger.info { "Setting the question votes." }
      // set the votes
      q.votes = Integer.parseInt(questionVote.text())

      // get the question comments
      val questionCommentElements = doc.select("div.comments").first().select("li.comment")
      // create an array for comments
      val questionComments = ArrayList<Comment>()
      // if there are comments
      if (questionCommentElements.isNotEmpty()) {
        // log message
        logger.info { "This question has comments, getting them." }
        // iterate through comments
        questionCommentElements.forEach { questionCommentElement ->
          // create a new comment object
          val c = Comment()
          // get the text
          c.text = questionCommentElement.select("span.comment-copy").first().html()
          // get the author
          c.author = questionCommentElement.select(".comment-user").first().text()
          // get the date
          c.date = questionCommentElement.select("span.comment-date").first().text()

          // the comment votes
          val votes: Int = try {
            // parse the votes
            Integer.parseInt(questionCommentElement.select("span.cool").first().text())
          } catch (_: Exception) {
            // an error happened, set it to zero
            0
          }
          // set the votes
          c.votes = votes

          // add to the comments array
          questionComments.add(c)
        }
      } else {
        logger.info { "This question has not been commented on." }
      }
      // log message
      logger.info { "Comments retrieved, setting them to the question." }
      // set comments
      q.comments = questionComments

      // define the author name element
      var authorName: Element
      // define the author reputation element
      var authorReputation: Element? = null
      // log message
      logger.info { "Getting the question author name and reputation." }
      // lets try
      try {
        // get the name
        authorName = doc.select(".post-signature.owner").first().select("div.user-details").first()
        // get the reputation
        authorReputation = doc.select(".post-signature.owner").first().select("div.user-details").first().select("span.reputation-score").first()
      } catch (_: Exception) {
        // something wrong happened, trying to get the name again
        authorName = doc.select(".post-signature")[1].select("div.user-details")[1]
      }

      // set the temp author name
      var authorNameStr: String
      // check if this is a special question
      authorNameStr = if (authorName.getElementsByTag("a").isNotEmpty() &&
                          authorName.getElementsByTag("a").html().contains("/>")) {
        // get the author name
        authorName.getElementsByTag("a").html()
      } else {
        // lets try again
        try {
          // get the author name
          authorName.getElementsByTag("a").first().text()
        } catch (_: Exception) {
          // another error, lets try again
          authorName.text()
        }

      }
      // check if the author name needs to be retrieved from the string
      if ("/>" in authorNameStr) {
        // get the substring
        authorNameStr = authorNameStr.substring(authorNameStr.indexOf("/>") + 2)
      }

      // log message
      logger.info { "Creating a new user." }
      // a new user is created
      val u = User()

      // log message
      logger.info { "Setting the user name." }
      // set the user name
      u.name = authorNameStr

      // temp string for reputation
      val authorReputationStr: String
      // log message
      logger.info { "Checking user reputation." }
      // check if it is a normal question
      if (doc.select("div#question").select("span.community-wiki").isEmpty()) {
        // check if it is a migrated question
        authorReputationStr = if (authorName.getElementsByTag("a").isEmpty()) {
          // log message
          logger.info { "This is a migrated question." }
          // set the reputation
          "Migrated question"
        } else {
          // log message
          logger.info { "This is a normal question." }
          // normal question
          try {
            authorReputation!!.text()
          } catch (_: Exception) {
            ""
          }
        }
      } else {
        // log message
        logger.info { "This is a community wiki question." }
        // set the reputation
        authorReputationStr = "Community Wiki"
      }
      // log message
      logger.info { "Setting the user reputation." }
      // set the reputation
      u.reputation = authorReputationStr

      // log message
      logger.info { "Adding the user to the question." }
      // add the user to the question
      q.user = u

      // set the class variable
      this.question = q
      // create a new array
      this.answers = ArrayList()

      // log message
      logger.info { "Getting the answers." }
      // fetching the answers block
      val answersBlock = doc.select("div.answer")
      // check if there are answers
      if (answersBlock.isNotEmpty()) {
        // log message
        logger.info { "Answers found, retrieving them." }
        // get the authors block
        val answerAuthorsBlock = answersBlock.select("div.fw-wrap")//("table.fw")

        // counter for the loop
        var counter = 0
        // iterate now
        answerAuthorsBlock.forEach { currentAnswerAuthor ->
          // log message
          logger.info { "Getting answer ${counter + 1}." }
          // set new post
          val a = Post()
          // set new user
          val ua = User()

          // the temp author name
          var answerAuthorNameStr: String
          // check if it is a valid entry
          // TODO: community wiki posts like 1319 contain <br>
          answerAuthorNameStr = if (currentAnswerAuthor.select("div.user-details").last().getElementsByTag("a").isEmpty()) {
            // set the value
            currentAnswerAuthor.select("div.user-details").last().text()
          } else {
            // try another approach
            currentAnswerAuthor.select("div.user-details").last().getElementsByTag("a").first().html()
          }
          // check if user name has to be trimmed
          if ("/>" in answerAuthorNameStr) {
            // get the substring
            answerAuthorNameStr = answerAuthorNameStr.substring(answerAuthorNameStr.indexOf("/>") + 2)
          }
          // log message
          logger.info { "Setting the author name for answer ${counter + 1}." }
          // set the author
          ua.name = answerAuthorNameStr

          // check if it has reputation
          if (currentAnswerAuthor.select("div.user-details").last().select("span.reputation-score").isEmpty()) {
            // it is a community wiki
            if (currentAnswerAuthor.select("span.community-wiki").isNotEmpty()) {
              // log message
              logger.info { "Answer ${counter + 1} is community wiki." }
              // set the reputation
              ua.reputation = "Community Wiki"
            } else {
              // log message
              logger.info { "Answer ${counter + 1} is a migrated answer." }
              // it is a migrated question
              ua.reputation = "Migrated answer"
            }
          } else {
            // log message
            logger.info { "Answer ${counter + 1} is a normal answer." }
            // normal answer
            ua.reputation = currentAnswerAuthor.select("div.user-details").last().select("span.reputation-score").first().text()
          }

          // log message
          logger.info { "Adding user to answer ${counter + 1}." }
          // add user
          a.user = ua

          // log message
          logger.info { "Adding date for answer ${counter + 1}." }
          // add date
          a.date = currentAnswerAuthor.select("div.user-info").select("span.relativetime").first().text()

          // log message
          logger.info { "Adding text for answer ${counter + 1}." }
          // get text
          val answersTexts = answersBlock.select("div.post-text")
          // add text
          a.text = answersTexts[counter].html()

          // get the votes
          val theVotes = answersBlock.select("div.vote")
          // log message
          logger.info { "Adding votes for answer ${counter + 1}." }
          // set the votes
          a.votes = Integer.parseInt(theVotes[counter].select("span.vote-count-post").first().text())

          // check if it is accepted
          if (!theVotes[counter].getElementsByClass("vote-accepted-on").isEmpty()) {
            // log message
            logger.info { "Answer ${counter + 1} is accepted." }
            // set accepted
            a.isAccepted = true
          }

          // create the comments array
          val currentAnswerComments = ArrayList<Comment>()
          // answers comments
          val currentAnswerCommentsElements = answersBlock[counter].select("div.comments").first().select("li.comment")
          // log message
          logger.info { "Checking comments for answer ${counter + 1}." }
          // if the answer has comments
          if (currentAnswerCommentsElements.isNotEmpty()) {
            // log message
            logger.info { "Adding comments for answer ${counter + 1}." }

            // iterate
            currentAnswerCommentsElements.forEach { currentAnswerCommentElement ->
              // create a new comment
              val ca = Comment()

              // set the text
              ca.text = currentAnswerCommentElement.select("span.comment-copy").first().html()
              ca.author = try {
                // try to set the author
                currentAnswerCommentElement.select("a.comment-user").first().text()
              } catch (_: Exception) {
                // fix it
                currentAnswerCommentElement.select("span.comment-user").first().text()
              }

              // set date
              ca.date = currentAnswerCommentElement.select("span.comment-date").first().text()

              // the comment votes
              val votes: Int = try {
                // try to parse it
                Integer.parseInt(currentAnswerCommentElement.select("span.cool").first().text())
              } catch (e: Exception) {
                // set default to zero
                0
              }
              // lets try
              // set votes
              ca.votes = votes
              // set the current comment to the list of comments
              currentAnswerComments.add(ca)
            }
          } else {
            // log message
            logger.info { "No comments for answer ${counter + 1}." }
          }
          // set the comments
          a.comments = currentAnswerComments

          // set to the class variable
          this.answers.add(a)
          // increments counter
          counter++
        }
        // log message
        logger.info { "All answers added." }
      } else {
        // log message
        logger.info { "There are no answers for this question." }
      }
    } catch (ex: Exception) {
      // log message
      if (ex is IOException) {
        logger.error { "An IO error occurred while trying to fetch and set the question data. Possibly a 404 page. MESSAGE: ${StringUtils.printStackTrace(ex)}" }
      } else {
        logger.error { "A generic error occurred while trying to fetch and set the question data. MESSAGE: ${StringUtils.printStackTrace(ex)}" }
      }

      // show dialog
      if (!TeXPrinter.isConsoleApplication) {
        Alert(Alert.AlertType.ERROR, "I'm sorry to tell you this, but the question ID you provided seems to lead to a 404 page. " +
                                     "Another possible cause is a very unstable internet connection, so the request timed out.\n\n" +
                                     "Please, correct the question ID and try again.", ButtonType.OK).showAndWait()
      }
    }

  }
}
