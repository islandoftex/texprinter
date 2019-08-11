// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.model

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

/**
 * Provides a simple POJO to handle posts, which can be a question or
 * an answer.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
class Post(
    // the post title
    var title: String = "",
    // the post date
    var date: String = "",
    // the user who posted it
    var user: User = User(),
    // the string representation of the user
    var userString: String = user.name,
    // this is a special flag for posts that happen to be answers:
    // if an answer is accepted, this flag is set to true
    var isAccepted: Boolean = false,
    // the votes
    var votes: Int = 0,
    // a list of comments to this post, if any
    var comments: ArrayList<Comment> = arrayListOf(),
    // do you want xhtml?
    var xhtml: Boolean = false) {

  // the post text
  var text: String = ""
    get() {
      if (this.xhtml) {
        var snippet = field
        val doc = Jsoup.parse(snippet)
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml)
        snippet = doc.body().html()
        // full code tag is not supported
        snippet = snippet.replace("<pre><code>".toRegex(), "<pre>")
            .replace("<pre class=.*\"><code>".toRegex(), "<pre>")
            .replace("</code></pre>".toRegex(), "</pre>")
            // code tag is not supported
            .replace("<code>".toRegex(), "<font face=\"Courier\">")
            .replace("</code>".toRegex(), "</font>")
            // add new lines
            .replace("\n\n".toRegex(), "<br/>")
        return snippet
      } else {
        return field
      }
    }
}
