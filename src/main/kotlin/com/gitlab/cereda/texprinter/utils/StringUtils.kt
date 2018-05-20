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
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.regex.Pattern

/**
 * Provides String functions to the generator classes. Another helper class,
 * but this one is specific to string manipulation.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
object StringUtils {

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
        .replace("<b>".toRegex(), "\\\\textbf{")
        .replace("</b>".toRegex(), "}")
        // replace bold tags
        .replace("<strong>".toRegex(), "\\\\textbf{")
        .replace("</strong>".toRegex(), "}")
        // replace italic tags
        .replace("<i>".toRegex(), "\\\\textit{")
        .replace("</i>".toRegex(), "}")
        // replace emphasized tags
        .replace("<em>".toRegex(), "\\\\emph{")
        .replace("</em>".toRegex(), "}")
        // replace paragraphs tags
        .replace("<p>".toRegex(), "")
        .replace("</p>".toRegex(), "\n\n")
        // replace ordered lists tags
        .replace("<ol>".toRegex(), "\\\\begin{enumerate}\n")
        .replace("</ol>".toRegex(), "\\\\end{enumerate}\n")
        // replace unordered lists tags
        .replace("<ul>".toRegex(), "\\\\begin{itemize}\n")
        .replace("</ul>".toRegex(), "\\\\end{itemize}\n")
        // replace item tags
        .replace("<li>".toRegex(), "\\\\item ")
        .replace("</li>".toRegex(), "\n")
        // replace blockquote tags
        .replace("<blockquote>".toRegex(), "\\\\begin{quotation}\n")
        .replace("</blockquote>".toRegex(), "\\\\end{quotation}\n")
        // replace code tags
        .replace("<pre><code>".toRegex(), "\\\\begin{TeXPrinterListing}\n")
        .replace("<pre class=.*\"><code>".toRegex(), "\\\\begin{TeXPrinterListing}\n")
        .replace("</code></pre>".toRegex(), "\\\\end{TeXPrinterListing}\n\n")
        // replace inline code tags
        .replace("<code>".toRegex(), "\\\\lstinline|")
        .replace("</code>".toRegex(), "|")
        // replace links tags
        .replace("alt=\".*\"\\s*".toRegex(), "")
        .replace("rel=\".*\"\\s*".toRegex(), "")
        // replace spurious spaces
        .replace("\"\\s>".toRegex(), "\">")

    // parse the text
    val docLinks = Jsoup.parse(newText)
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
      images.add(ImageGroup(it.attr("abs:src")))
    }

    // for every image in the list of images
    images.forEach { img ->
      // lets try
      try {
        // finally, download the image to the current directory
        Downloader.download(img.url, img.name!!)
      } catch (exception: Exception) {
        // log message
        logger.warn { "An error occurred while getting the current image. Trying to set the replacement image instead. MESSAGE: ${StringUtils.printStackTrace(exception)}" }
        // image could not be downloaded for any reason
        try {
          // write a replacement image
          //Files.write(Paths.get(img.name!!), Base64.decode("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"))
          img.name = "example-image.pdf"
          //} catch (ioexception: IOException) {
          // log message
          //  log.log(Level.SEVERE, "An IO exception occured while trying to create the image replacement. MESSAGE: {0}", StringUtils.printStackTrace(ioexception))
        } catch (except: Exception) {
          // log message
          logger.error { "An error occured while trying to create the image replacement. MESSAGE: ${StringUtils.printStackTrace(except)}" }
        }
      }

      // TODO: insufficient, because of alt-text
      newText = newText.replace(("<img src=\"" + img.url + "\" />").toRegex(),
          "\\\\begin{figure}[h!]\n\\\\centering\n\\\\includegraphics[scale=0.5]{" + img.name + "}\n\\\\end{figure}")
    }

    // unescape all HTML entities
    newText = Parser.unescapeEntities(newText, true)

    // return new text
    return newText
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
