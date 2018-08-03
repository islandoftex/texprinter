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
package com.gitlab.cereda.texprinter.utils

import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URL
import java.util.*
import java.util.regex.Pattern

/**
 * Provides "static" functions to the application and especially the generator
 * classes. It integrates string functions as well as a download function which
 * is only relevant to the TeX generator.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
object AppUtils {
  // the application logger
  private val logger = KotlinLogging.logger { }

  /**
   * Escapes HTML entities and tags to a TeX format. This method tries to
   * replace HTML code by the TeX equivalent macros.
   *
   * @param text The input text.
   * @return A new text formatted from HTML to TeX.
   */
  fun escapeHTMLtoTeX(text: String): String {
    var newText = text
        // replace bold tags
        .replace("<b>", "\\textbf{")
        .replace("</b>", "}")
        // replace bold tags
        .replace("<strong>", "\\textbf{")
        .replace("</strong>", "}")
        // replace italic tags
        .replace("<i>", "\\textit{")
        .replace("</i>", "}")
        // replace emphasized tags
        .replace("<em>", "\\emph{")
        .replace("</em>", "}")
        // replace paragraphs tags
        .replace("<p>", "")
        .replace("</p>", "\n\n")
        // replace ordered lists tags
        .replace("<ol>", "\\begin{enumerate}\n")
        .replace("</ol>", "\\end{enumerate}\n")
        // replace unordered lists tags
        .replace("<ul>", "\\begin{itemize}\n")
        .replace("</ul>", "\\end{itemize}\n")
        // replace item tags
        .replace("<li>", "\\item ")
        .replace("</li>", "\n")
        // replace blockquote tags
        .replace("<blockquote>", "\\begin{quotation}\n")
        .replace("</blockquote>", "\\end{quotation}\n")
        // replace code tags
        .replace("<pre><code>", "\\begin{TeXPrinterListing}\n")
        .replace("<pre class=.*\"><code>", "\\begin{TeXPrinterListing}\n")
        .replace("</code></pre>", "\\end{TeXPrinterListing}\n\n")
        .replace("<pre>", "\\begin{TeXPrinterListing}\n")
        .replace("</pre>", "\\end{TeXPrinterListing}\n")
        // replace inline code tags
        .replace("<code>", "\\lstinline|")
        .replace("</code>", "|")
        .replace("<font face=\"Courier\">", "\\lstinline|")
        .replace("</font>", "|")
        // replace links tags
        .replace("rel=\".*\"\\s*".toRegex(), "")
        // replace spurious spaces
        .replace("\"\\s>".toRegex(), "\">")
        .replace("\"/>", "\" />")
        // replace line breaks
        .replace("<br/>", "")

    // parse the text
    val docLinks = Jsoup.parse(newText)
    //docLinks.outputSettings().syntax(Document.OutputSettings.Syntax.xml)
    // get all the links
    docLinks.getElementsByTag("a").forEach {
      // replace the outer html
      newText = newText.replace(Pattern.quote(it.outerHtml()).toRegex(),
          "\\\\href{" + it.attr("href") + "}{" +
          it.text().replace("\\", "\\\\") + "}")
    }

    // create a list of images
    val images = ArrayList<ImageGroup>()
    // parse the current text
    val doc = Jsoup.parse(text)
    // fetch all the media found
    val media = doc.select("[src]")
    // for all media found (img tag)
    media.filter { it.tagName() == "img" }.forEach {
      images.add(ImageGroup(it.attr("abs:src"),
          it.attr("alt") ?: ""))
    }

    // for every image in the list of images
    images.forEach { img ->
      // lets try
      try {
        // finally, download the image to the current directory
        download(img.url, img.name)
      } catch (exception: Exception) {
        // log message
        logger.warn { "An error occurred while getting the current image. Trying to set the replacement image instead. MESSAGE: ${AppUtils.printStackTrace(exception)}" }
        // image could not be downloaded for any reason
        // use example-image as replacement image
        img.name = "example-image.pdf"
      }

      // TODO: insufficient, because of alt-text
      val caption = if (img.altText.isNotBlank() &&
                        !img.altText.startsWith("enter image") &&
                        img.altText != "alt text") "\n\\\\caption{${img.altText}}"
      else ""
      newText = newText
          .replace((if (img.altText.isBlank()) "<img src=\"${img.url}\" />"
          else "<img src=\"${img.url}\" alt=\"${img.altText}\" />")
              .toRegex(), """\\begin{figure}[htbp]
\\centering
\\includegraphics[scale=0.5]{${img.name}}$caption
\\end{figure}
""")
    }

    // unescape all HTML entities
    newText = Parser.unescapeEntities(newText, true)

    // return new text
    return newText
  }

  /**
   * Download the file from the URL.
   *
   * @param resourceURL The resource URL.
   * @param fileName The file name.
   */
  fun download(resourceURL: String, fileName: String) {
    // log message
    logger.info { "Trying to download the file $fileName" }

    // lets try
    try {
      // open and close the connection
      URL(resourceURL).openConnection().getInputStream().use {
        FileOutputStream(fileName).use { outStream ->
          it.copyTo(outStream)
        }
      }
      // log message
      logger.info { "File $fileName downloaded successfully." }
    } catch (e: Exception) {
      // log message
      logger.error {
        "A generic error happened during the file download. " +
        "MESSAGE: ${AppUtils.printStackTrace(e)}"
      }
    }
  }

  /**
   * Prints the stack trace to a string. This method gets the exception
   * and prints the stack trace to a string instead of the system default
   * output.
   *
   * @param exception The exception.
   * @return The string containg the whole stack trace.
   */
  fun printStackTrace(exception: Exception): String {
    // lets try
    return try {
      // create a string writer
      val stringWriter = StringWriter()
      // create a print writer
      val printWriter = PrintWriter(stringWriter)
      // set the stack trace to the writer
      exception.printStackTrace(printWriter)
      // return the writer
      "M: " + exception.message + " S: " + stringWriter.toString()
    } catch (except: Exception) {
      // error message
      "Error in printStackTrace: " + except.message
    }
  }
}
