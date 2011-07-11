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
 * <b>StringHelper.java</b>: This is a helper class that provides String
 * functions to the generator classes.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.util.ArrayList;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Provides String functions to the generator classes.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class StringHelper {

    /**
     * Escapes HTML entities and tags to a TeX format.
     * @param text The input text.
     * @return A new text formatted from HTML to TeX.
     */
    public static String escapeHTMLtoTeX(String text) {

        // replace bold tags
        String newText = text.replaceAll("<b>", "\\\\textbf{");
        newText = newText.replaceAll("</b>", "}");

        // replace bold tags
        newText = newText.replaceAll("<strong>", "\\\\textbf{");
        newText = newText.replaceAll("</strong>", "}");

        // replace italic tags
        newText = newText.replaceAll("<i>", "\\\\textit{");
        newText = newText.replaceAll("</i>", "}");

        // replace emphasized tags
        newText = newText.replaceAll("<em>", "\\\\emph{");
        newText = newText.replaceAll("</em>", "}");

        // replace paragraphs tags
        newText = newText.replaceAll("<p>", "");
        newText = newText.replaceAll("</p>", "\n\n");

        // replace ordered lists tags
        newText = newText.replaceAll("<ol>", "\\\\begin{enumerate}\n");
        newText = newText.replaceAll("</ol>", "\\\\end{enumerate}\n");

        // replace unordered lists tags
        newText = newText.replaceAll("<ul>", "\\\\begin{itemize}\n");
        newText = newText.replaceAll("</ul>", "\\\\end{itemize}\n");

        // replace item tags
        newText = newText.replaceAll("<li>", "\\\\item ");
        newText = newText.replaceAll("</li>", "\n");

        // replace blockquote tags
        newText = newText.replaceAll("<blockquote>", "\\\\begin{quotation}\n");
        newText = newText.replaceAll("</blockquote>", "\\\\end{quotation}\n");

        // replace code tags
        newText = newText.replaceAll("<pre><code>", "\\\\begin{lstlisting}\n");
        newText = newText.replaceAll("<pre class=.*\"><code>", "\\\\begin{lstlisting}\n");
        newText = newText.replaceAll("</code></pre>", "\\\\end{lstlisting}\n\n");

        // replace inline code tags
        newText = newText.replaceAll("<code>", "\\\\lstinline|");
        newText = newText.replaceAll("</code>", "|");

        // replace links tags
        newText = newText.replaceAll("<a href=\"", "\\\\href{");
        newText = newText.replaceAll("\\\" rel=\\\"nofollow\">", "}{");
        newText = newText.replaceAll("</a>", "}");
        newText = newText.replaceAll("alt=\".*\" ", "");

        // create a list of images
        ArrayList<ImageGroupHelper> images = new ArrayList<ImageGroupHelper>();

        // parse the current text
        Document doc = Jsoup.parse(text);

        // fetch all the media found
        Elements media = doc.select("[src]");

        // for all media found
        for (Element m : media) {

            // if it's an image tag
            if (m.tagName().equals("img")) {

                // create a new image group with the image link
                ImageGroupHelper image = new ImageGroupHelper(m.attr("abs:src"));

                // add to the list of images
                images.add(image);

                // set the current image to null
                image = null;
            }
        }

        // for every image in the list of images
        for (ImageGroupHelper img : images) {

            // while there are references for that image in the text
            while (newText.indexOf(img.getURL()) != -1) {

                // replace the occurrence of that image
                newText = newText.replaceFirst("<img src=\"" + img.getURL() + "\" />", "\\\\begin{figure}[h!]\n\\\\centering\n\\\\includegraphics[scale=0.5]{" + img.getName() + "}\n\\\\end{figure}");
            }

            // finally, download the image to the current directory
            DownloadHelper.download(img.getURL(), img.getName());
        }

        // unescape all HTML entities
        newText = StringEscapeUtils.unescapeHtml(newText);

        // return new text
        return newText;
    }

    /**
     * Checks if the provided string only contains numbers
     * @param text The string.
     * @return A boolean to determine if the string only contains numbers.
     */
    public static boolean onlyNumbers(String text) {

        // if there's no string to compare
        if (text == null || text.length() == 0) {
            
            // return false
            return false;
        }

        // for every char in the string
        for (int i = 0; i < text.length(); i++) {

            // if there's a char which is not a digit
            if (!Character.isDigit(text.charAt(i))) {
                
                // return false
                return false;
            }
        }

        // everything is fine, return true
        return true;
    }
}
