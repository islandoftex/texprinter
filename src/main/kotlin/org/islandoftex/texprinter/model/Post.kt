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
