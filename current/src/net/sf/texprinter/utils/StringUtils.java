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
 * StringUtils.java: This is a helper class that provides String functions
 * to the generator classes.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import com.itextpdf.text.pdf.codec.Base64;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Provides String functions to the generator classes.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 1.0
 */
public class StringUtils {

    // the application logger
    private static final Logger log = Logger.getLogger(StringUtils.class.getCanonicalName());
    
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
        newText = newText.replaceAll("<pre><code>", "\\\\begin{TeXPrinterListing}\n");
        newText = newText.replaceAll("<pre class=.*\"><code>", "\\\\begin{TeXPrinterListing}\n");
        newText = newText.replaceAll("</code></pre>", "\\\\end{TeXPrinterListing}\n\n");

        // replace inline code tags
        newText = newText.replaceAll("<code>", "\\\\lstinline|");
        newText = newText.replaceAll("</code>", "|");

        // replace links tags
        newText = newText.replaceAll("alt=\".*\" ", "");

        // parse the text
        Document docLinks = Jsoup.parse(newText);

        // get all the links
        Elements links = docLinks.getElementsByTag("a");
        
        // if there are links
        if (links.size() > 0) {
            
            // for every link
            for (Element link : links) {
            
                // get the outer HTML
                String temp  = link.outerHtml();
                
                // replace it
                newText = newText.replaceFirst(Pattern.quote(temp), "\\\\href{" + link.attr("href") + "}{" + link.text() + "}");
            
            }
        }

        // create a list of images
        ArrayList<ImageGroup> images = new ArrayList<ImageGroup>();

        // parse the current text
        Document doc = Jsoup.parse(text);

        // fetch all the media found
        Elements media = doc.select("[src]");

        // for all media found
        for (Element m : media) {

            // if it's an image tag
            if (m.tagName().equals("img")) {

                // create a new image group with the image link
                ImageGroup image = new ImageGroup(m.attr("abs:src"));

                // add to the list of images
                images.add(image);

                // set the current image to null
                image = null;
            }
        }

        // create a new loop saver
        LoopSaver lps = null;
        
        // for every image in the list of images
        for (ImageGroup img : images) {

            // create a new object
            lps = new LoopSaver();
            
            // while there are references for that image in the text
            while (newText.indexOf(img.getURL()) != -1) {

                // tick loop
                lps.tick();
                
                // replace the occurrence of that image
                newText = newText.replaceFirst("<img src=\"" + img.getURL() + "\" />", "\\\\begin{figure}[h!]\n\\\\centering\n\\\\includegraphics[scale=0.5]{" + img.getName() + "}\n\\\\end{figure}");
            }

            // lets try
            try {

                // finally, download the image to the current directory
                Downloader.download(img.getURL(), img.getName());

            } catch (Exception exception) {

                // log message
                log.log(Level.WARNING, "An error occurred while getting the current image. Trying to set the replacement image instead. MESSAGE: {0}", StringUtils.printStackTrace(exception));
                
                // image could not be downloaded for any reason
                try {

                    // open a file stream
                    FileOutputStream f = new FileOutputStream(img.getName());

                    // write a replacement image
                    f.write(Base64.decode("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"));

                    // close the file
                    f.close();

                } catch (IOException ioexception) {
                    
                    // log message
                   log.log(Level.SEVERE, "An IO exception occured while trying to create the image replacement. MESSAGE: {0}", StringUtils.printStackTrace(ioexception));  
                   
                } catch (Exception except) {
                    
                    // log message
                    log.log(Level.SEVERE, "An error occured while trying to create the image replacement. MESSAGE: {0}", StringUtils.printStackTrace(except));
                    
                }

                // display message
                Dialogs.info(null, "Don't panic!", "For some reason, I couldn't download the following image:\n\n<b>" + img.getURL() + "</b>\n\nPlease, try to download this image. Don't panic, this is just a friendly warning.");

            }

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

    /**
     * Prints the stack trace to a string.
     * @param exception The exception.
     * @return 
     */
    public static String printStackTrace(Exception exception) {
        
        // lets try
        try {
            
            // create a string writer
            StringWriter stringWriter = new StringWriter();
            
            // create a print writer
            PrintWriter printWriter = new PrintWriter(stringWriter);
            
            // set the stack trace to the writer
            exception.printStackTrace(printWriter);
            
            // return the writer
            return "M: " + exception.getMessage() + " S: " + stringWriter.toString();
            
        } catch (Exception except) {
            
            // error message
            return "Error in printStackTrace: " + except.getMessage();
        }
    }
}
