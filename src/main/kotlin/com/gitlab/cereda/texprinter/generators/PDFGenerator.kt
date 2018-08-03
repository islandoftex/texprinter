/******************************************************************************
 * Copyright 2012-2018 Paulo Roberto Massa Cereda and Ben Frank               *
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
package com.gitlab.cereda.texprinter.generators

import com.gitlab.cereda.texprinter.config.Configuration
import com.gitlab.cereda.texprinter.model.Post
import com.gitlab.cereda.texprinter.model.Question
import com.gitlab.cereda.texprinter.utils.AppUtils
import com.gitlab.cereda.texprinter.utils.Dialogs
import com.itextpdf.html2pdf.HtmlConverter
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.BlockElement
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.ListItem
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import mu.KotlinLogging
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Provides the PDF generation from a Question object.
 *
 * @author Paulo Roberto Massa Cereda and Ben Frank
 * @version 3.0
 * @since 1.0
 */
object PDFGenerator {
  // the application logger
  private val logger = KotlinLogging.logger {}

  /**
   * Generates a PDF file from a Question object.
   *
   * @param question The question.
   * @param filename The filename.
   */
  fun generate(question: Question, filename: String) {
    // log message
    logger.info { "Starting PDF generation of $filename." }
    question.question.xhtml = true

    // create a line separator
    val line = Paragraph().apply {
      add(LineSeparator(SolidLine()).apply {
        width = UnitValue.createPercentValue(100f)
        height = UnitValue.createPointValue(1f)
      })
    }

    /**
     * Adds the HTML text to a PDF document.
     *
     * @param text The text.
     * @param document The document.
     */
    fun addPostText(text: String, document: Document) {
      // TODO: check default image width (and maybe other styling)
      val html = Jsoup.parse(text).body()
      html.select("img").apply {
        if (attr("width").isNullOrBlank()) {
          attr("width", "100%")
        }
      }
      HtmlConverter.convertToElements(html.html()).forEach {
        // this should suffice (according to docs)
        try {
          document.add(it as BlockElement<*>)
        } catch (_: Exception) {
          document.add(it as ListItem)
        }
      }
    }

    /**
     * Adds a paragraph containing the comment's text
     *
     * @param html The comment's HTML.
     * @param doc The document to add to.
     */
    fun addCommentText(html: String, doc: Document) {
      val p = Paragraph().apply {
        setFontSize(10f)
        setTextAlignment(TextAlignment.RIGHT)
      }
      HtmlConverter.convertToElements(html).forEach {
        try {
          p.add(it as BlockElement<*>)
        } catch (_: Exception) {
          logger.error {
            "The post's comments are no comments. " +
            "Somebody has answered here!"
          }
        }
      }
      doc.add(p)
    }

    /**
     * Adds a post to a PDF document
     *
     * @param post The post.
     * @param doc The document.
     */
    fun addPostToDoc(post: Post, doc: Document) {
      // log message
      logger.info { "Adding the post's title: ${post.title}" }
      // add the question title to the document
      doc.add(Paragraph(post.title).apply {
        setFontSize(16f)
        setBold()
        setFontColor(ColorConstants.BLACK)
        setMarginBottom(-2f)
      })

      // log message
      logger.info { "Adding both author and reputation." }
      // add the asker to the document
      doc.add(Paragraph(post.userString).apply {
        setFontSize(10f)
        setItalic()
        setFontColor(ColorConstants.DARK_GRAY)
        setMarginBottom(-6f)
      })

      // add the line to the document
      doc.add(line)

      // log message
      logger.info { "Adding the post's text." }
      // create a list of elements from the question objects
      addPostText(post.text, doc)

      // if there are comments to this question
      if (!post.comments.isEmpty()) {
        // log message
        logger.info { "Adding the post's comments." }
        // create a paragraph for comments title
        doc.add(Paragraph("This post has " + post.comments.size +
                          if (post.comments.size == 1) " comment:" else " comments:").apply {
          setFontSize(12f)
          setBold()
          setFontColor(ColorConstants.BLACK)
        })
        // for each comment
        post.comments.forEach {
          // get the elements of the comment text
          addCommentText("<span>" + it.text + "</span>", doc)
          // create a new paragraph about the comment author
          doc.add(Paragraph(it.author + " on " +
                            it.date + " (" + it.votes.toString() +
                            (if (it.votes == 1) " vote" else " votes") + ")").apply {
            setFontSize(8f)
            setItalic()
            setFontColor(ColorConstants.DARK_GRAY)
            setTextAlignment(TextAlignment.RIGHT)
            setMarginTop(-2f)
            setMarginBottom(2f)
          })
        }
      }
    }

    FileOutputStream(filename).use {
      try {
        // define a new PDF writer
        PdfWriter(it, WriterProperties().apply {
          this.setPdfVersion(PdfVersion.PDF_1_7)
          this.setCompressionLevel(9)
        }).use {
          // define a new PDF document
          val pdfDocument = PdfDocument(it)
          pdfDocument.use {
            val config = Configuration()
            it.documentInfo.apply {
              this.author = "TeXPrinter v${config.appVersionNumber}"
              this.title = "Printed result of post " + filename.replace(".pdf", "")
              this.keywords = "TeX, LaTeX, ConTeXt, StackExchange"
              this.creator = "TeXPrinter v${config.appVersionNumber} (${config.appVersionName})"
            }
            Document(it).use { doc ->
              question.question.userString = "Asked by " + question.question.user.name +
                  " (" + question.question.user.reputation + ") on " +
                  question.question.date + " (" + question.question.votes.toString() +
                  (if (question.question.votes == 1) " vote" else " votes") + ")"
              addPostToDoc(question.question, doc)

              // add a line separator
              doc.add(line)
              // add two new lines
              doc.add(Paragraph("\n"))

              // get the list of answers
              val answersList = question.answers
              // if there are no answers
              if (answersList.isEmpty()) {
                // log message
                logger.info { "This question has no answers." }
                // add the paragraph to the document
                doc.add(Paragraph("Sorry, this question has no answers yet.").apply {
                  setFontSize(16f)
                  setBold()
                  setFontColor(ColorConstants.BLACK)
                })
              } else {
                // log message
                logger.info { "Adding answers." }
                // there are answers, so create a counter for answers
                var answerCount = 1
                // for each answer
                answersList.forEach { answer ->
                  // log message
                  logger.info { "Adding answer $answerCount." }
                  answer.xhtml = true
                  // set the message text as empty
                  var answerAccepted = ""
                  // if the answer is accepted
                  if (answer.isAccepted) {
                    // add that to the message
                    answerAccepted = " - Marked as accepted."
                  }
                  // create a new chunk
                  answer.title = "Answer #$answerCount"
                  // increase the counter
                  answerCount++

                  // user string
                  answer.userString = "Answered by " + answer.user.name + " (" +
                      answer.user.reputation + ") on " + answer.date + answerAccepted +
                      " (" + answer.votes.toString() + (if (answer.votes == 1) " vote" else " votes") + ")"

                  addPostToDoc(answer, doc)

                  // add a line separator
                  doc.add(line)
                  // add two new lines
                  doc.add(Paragraph("\n"))
                  doc.add(Paragraph("\n"))
                }
              }
              // log message
              logger.info { "PDF generation complete, closing $filename." }
            }
          }
        }
      } catch (ioexception: IOException) {
        // critical error, exit
        Dialogs.showExceptionWindow(ioexception)
      } catch (exception: Exception) {
        // log message
        logger.error {
          "A generic error occurred while trying to create the PDF file. " +
          "MESSAGE: ${AppUtils.printStackTrace(exception)}"
        }
        // log message
        logger.info { "I will try to remove the remaining PDF file." }

        try {
          // reference problematic file
          val target = File(filename)
          // log message
          logger.info { "Opening problematic file $filename." }
          // check if file exists
          if (target.exists()) {
            // log message
            logger.info { "File exists, trying to delete it." }
            // trying to remove it
            if (target.delete()) {
              // log message
              logger.info { "File $filename was successfully removed." }
            } else {
              // log message
              logger.error { "File $filename could not be removed." }
            }
          }
        } catch (ex: Exception) {
          // log message
          logger.error {
            "A exception was raised. " +
            "MESSAGE: ${AppUtils.printStackTrace(ex)}"
          }
        }

        // critical error, exit
        Dialogs.showExceptionWindow(exception)
      }
    }
  }
}
