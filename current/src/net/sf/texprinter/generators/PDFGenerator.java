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
 * PDFGenerator.java: This class is responsible for generating a PDF file
 * from a Question object.
 */

// package definition
package net.sf.texprinter.generators;

// needed imports
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.ChainedProperties;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.ImageProvider;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.texprinter.model.Comment;
import net.sf.texprinter.model.Post;
import net.sf.texprinter.model.Question;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;

/**
 * Provides the PDF generation from a Question object.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 1.0
 */
public class PDFGenerator {

    // the application logger
    private static final Logger log = Logger.getLogger(PDFGenerator.class.getCanonicalName());

    /**
     * Generates a PDF file from a Question object.
     * @param question The question.
     * @param filename The filename.
     */
    public static void generate(Question question, String filename) {

        // log message
        log.log(Level.INFO, "Starting PDF generation of {0}.", filename);

        // define a new PDF document
        Document document = null;

        // define a new PDF writer
        PdfWriter writer = null;

        // lets try
        try {

            // create a new PDF document
            document = new Document();

            // define a new PDF Writer
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

            // set the PDF version
            writer.setPdfVersion(PdfWriter.VERSION_1_6);

            // open the document
            document.open();

            // set the title font
            Font titleFont = new Font(FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);

            // set the chunk for the question title
            Chunk questionTitle = new Chunk(question.getQuestion().getTitle(), titleFont);

            // create a paragraph from that chunk
            Paragraph paragraphQuestionTitle = new Paragraph(questionTitle);

            // log message
            log.log(Level.INFO, "Adding the question title.");

            // add the question title to the document
            document.add(paragraphQuestionTitle);

            // set the asker font
            Font askerFont = new Font(FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.DARK_GRAY);

            // set the chunk for the asker
            Chunk questionAsker = new Chunk("Asked by " + question.getQuestion().getUser().getName() + " (" + question.getQuestion().getUser().getReputation() + ") on " + question.getQuestion().getDate() + " (" + String.valueOf(question.getQuestion().getVotes()) + (question.getQuestion().getVotes() == 1 ? " vote" : " votes") + ")", askerFont);

            // create a paragraph from that chunk
            Paragraph paragraphQuestionAsker = new Paragraph(questionAsker);

            // log message
            log.log(Level.INFO, "Adding both asker and reputation.");

            // add the asker to the document
            document.add(paragraphQuestionAsker);

            // create a line separator
            LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -5);

            // add the line to the document
            document.add(line);

            // add a new line
            document.add(Chunk.NEWLINE);

            // create a list of elements from the question objects
            List<Element> questionTextObjects = getPostText(question.getQuestion().getText());

            // log message
            log.log(Level.INFO, "Adding the question text.");

            // for each element
            for (Element questionTextObject : questionTextObjects) {

                // add it to the document
                document.add(questionTextObject);
            }

            // add a new line
            document.add(Chunk.NEWLINE);

            // create a new font for the comments title
            Font commentsTitleFont = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

            // create a new chunk based on that font
            Chunk commentsTitle = new Chunk("This question has " + question.getQuestion().getComments().size() + ((question.getQuestion().getComments().size() == 1) ? " comment:" : " comments:"), commentsTitleFont);

            // create a paragraph from that chunk
            Paragraph paragraphCommentsTitle = new Paragraph(commentsTitle);

            // if there are comments to this question
            if (!question.getQuestion().getComments().isEmpty()) {

                // log message
                log.log(Level.INFO, "Adding the question comments.");

                // add that paragraph to the document
                document.add(paragraphCommentsTitle);

                // add a new line
                document.add(Chunk.NEWLINE);

                // get all the comments
                List<Comment> questionComments = question.getQuestion().getComments();

                // for each comment
                for (Comment questionComment : questionComments) {

                    // get the elements of the comment text
                    List<Element> questionCommentObjects = getPostText(questionComment.getText());

                    // for each element
                    for (Element questionCommentObject : questionCommentObjects) {

                        // add it to the document
                        document.add(questionCommentObject);
                    }

                    // create a new paragraph about the comment author
                    Paragraph paragraphCommentAuthor = new Paragraph(questionComment.getAuthor() + " on " + questionComment.getDate() + " (" + String.valueOf(questionComment.getVotes()) + (questionComment.getVotes() == 1 ? " vote" : " votes") + ")", askerFont);

                    // set the alignment to the right
                    paragraphCommentAuthor.setAlignment(Element.ALIGN_RIGHT);

                    // add the paragraph to the document
                    document.add(paragraphCommentAuthor);

                    // add a new line
                    document.add(Chunk.NEWLINE);
                }
            }

            // add a line separator
            document.add(line);

            // add two new lines
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // get the list of answers
            List<Post> answersList = question.getAnswers();

            // if there are no answers
            if (answersList.isEmpty()) {

                // log message
                log.log(Level.INFO, "This question has no answers.");

                // create a new chunk
                Chunk noAnswersTitle = new Chunk("Sorry, this question has no answers yet.", titleFont);

                // create a paragraph from that chunk
                Paragraph paragraphNoAnswersTitle = new Paragraph(noAnswersTitle);

                // add the paragraph to the document
                document.add(paragraphNoAnswersTitle);

            } else {

                // log message
                log.log(Level.INFO, "Adding answers.");

                // there are answers, so create a counter for answers
                int answerCount = 1;

                // for each answer
                for (Post answer : answersList) {

                    // log message
                    log.log(Level.INFO, "Adding answer {0}.", answerCount);

                    // set the message text as empty
                    String answerAccepted = "";

                    // if the answer is accepted
                    if (answer.isAccepted()) {

                        // add that to the message
                        answerAccepted = " - Marked as accepted.";
                    }

                    // create a new chunk
                    Chunk answerTitle = new Chunk("Answer #" + answerCount, titleFont);

                    // create a paragraph from that chunk
                    Paragraph paragraphAnswerTitle = new Paragraph(answerTitle);

                    // add the paragraph to the document
                    document.add(paragraphAnswerTitle);

                    // increase the counter
                    answerCount++;

                    // create a new chunk
                    Chunk questionAnswerer = new Chunk("Answered by " + answer.getUser().getName() + " (" + answer.getUser().getReputation() + ") on " + answer.getDate() + answerAccepted + " (" + String.valueOf(answer.getVotes()) + (answer.getVotes() == 1 ? " vote" : " votes") + ")", askerFont);

                    // create a paragraph from that chunk
                    Paragraph paragraphQuestionAnswerer = new Paragraph(questionAnswerer);

                    // add that paragraph to the document
                    document.add(paragraphQuestionAnswerer);

                    // add a line separator
                    document.add(line);

                    // add a new line
                    document.add(Chunk.NEWLINE);

                    // create a list of elements from the answer text
                    List<Element> answerTextObjects = getPostText(answer.getText());

                    // for each element
                    for (Element answerTextObject : answerTextObjects) {

                        // add it to the document
                        document.add(answerTextObject);
                    }

                    // add a new line
                    document.add(Chunk.NEWLINE);

                    // create a new font style
                    Font answerCommentsTitleFont = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

                    // create a new chunk
                    Chunk answerCommentsTitle = new Chunk("This answer has " + answer.getComments().size() + ((answer.getComments().size() == 1) ? " comment:" : " comments:"), answerCommentsTitleFont);

                    // create a paragraph from that chunk
                    Paragraph paragraphAnswerCommentsTitle = new Paragraph(answerCommentsTitle);

                    // if there are comments for that answer
                    if (!answer.getComments().isEmpty()) {

                        // log message
                        log.log(Level.INFO, "Adding comments for answer {0}.", (answerCount - 1));

                        // add that paragraph to the document
                        document.add(paragraphAnswerCommentsTitle);

                        // add a new line
                        document.add(Chunk.NEWLINE);

                        // get all the comments
                        List<Comment> answerComments = answer.getComments();

                        // for each comment
                        for (Comment answerComment : answerComments) {

                            // create a list of elements from the comment text
                            List<Element> answerCommentObjects = getPostText(answerComment.getText());

                            // for each element
                            for (Element answerCommentObject : answerCommentObjects) {

                                // add it to the document
                                document.add(answerCommentObject);
                            }

                            // create a new paragraph for the comment author
                            Paragraph paragraphAnswerCommentAuthor = new Paragraph(answerComment.getAuthor() + " on " + answerComment.getDate() + " (" + String.valueOf(answerComment.getVotes()) + (answerComment.getVotes() == 1 ? " vote" : " votes") + ")", askerFont);

                            // set the aligment to the right
                            paragraphAnswerCommentAuthor.setAlignment(Element.ALIGN_RIGHT);

                            // add the paragraph to the document
                            document.add(paragraphAnswerCommentAuthor);

                            // add a new line
                            document.add(Chunk.NEWLINE);
                        }
                    }

                    // add a line separator
                    document.add(line);

                    // add two new lines
                    document.add(Chunk.NEWLINE);
                    document.add(Chunk.NEWLINE);
                }
            }

            // log message
            log.log(Level.INFO, "PDF generation complete, closing {0}.", filename);

            // close the document
            document.close();

        } catch (IOException ioexception) {
            
            // log message
            log.log(Level.SEVERE, "An IO error occurred while trying to create the PDF file. MESSAGE: {0}", StringUtils.printStackTrace(ioexception));
            
            // critical error, exit
            Dialogs.exception();
            
        } catch (Exception exception) {

            // log message
            log.log(Level.SEVERE, "A generic error occurred while trying to create the PDF file. MESSAGE: {0}", StringUtils.printStackTrace(exception));

            // log message
            log.log(Level.INFO, "I will try to remove the remaining PDF file.");
            
            try {
                
                // log message
                log.log(Level.INFO, "Closing both document and writer.");

                // close the document
                document.close();
                
                // close the writer
                writer.close();
                
            } catch (Exception ex) {
                
                // log message
                log.log(Level.WARNING, "I could not close either document or writer. MESSAGE: {0}", StringUtils.printStackTrace(ex));
            }

            try {
                
                // reference problematic file
                File target = new File(filename);

                // log message
                log.log(Level.INFO, "Opening problematic file {0}.", filename);
                
                // check if file exists
                if (target.exists()) {
                    
                    // log message
                    log.log(Level.INFO, "File exists, trying to delete it.");
                    
                    // trying to remove it
                    if (target.delete()) {
                        
                        // log message
                        log.log(Level.INFO, "File {0} was successfully removed.", filename);
                        
                    } else {
                        
                        // log message
                        log.log(Level.SEVERE, "File {0} could not be removed.", filename);
                        
                    }
                }
            } catch (SecurityException se) {
                
                // log message
                log.log(Level.SEVERE, "A security exception was raised. MESSAGE: {0}", StringUtils.printStackTrace(se));
                
            }

            // critical error, exit
            Dialogs.exception();
            
        }
    }

    /**
     * Parses the HTML text to a list of elements.
     * @param text The text.
     * @return A list of elements.
     * @throws IOException Throws an IOException if the StringReader couldn't
     * get the string provided.
     */
    private static List<Element> getPostText(String text) throws IOException {

        // set the text to a snippet
        String snippet = text;

        // full code tag is not supported
        snippet = snippet.replaceAll("<pre><code>", "<pre>");
        snippet = snippet.replaceAll("<pre class=.*\"><code>", "<pre>");
        snippet = snippet.replaceAll("</code></pre>", "</pre>");

        // code tag is not supported
        snippet = snippet.replaceAll("<code>", "<font face=\"Courier\">");
        snippet = snippet.replaceAll("</code>", "</font>");

        // add new lines
        snippet = snippet.replaceAll("\n", "<br/>");

        // create a new stylesheet
        StyleSheet styles = new StyleSheet();

        // configure lists
        styles.loadTagStyle("ul", "indent", "10");
        styles.loadTagStyle("li", "leading", "14");

        // configure hyperlinks
        styles.loadTagStyle("a", "color", "blue");

        // create a map of providers
        HashMap providers = new HashMap();

        // set the image provider
        providers.put("img_provider", new TeXImageFactory());

        // parse the HTML to a list
        List<Element> objects = HTMLWorker.parseToList(new StringReader(snippet), styles, providers);

        // return the new list
        return objects;
    }

    /**
     * Image factory implementation.
     */
    public static class TeXImageFactory implements ImageProvider {

        /**
         * Gets the image.
         * @param string the image URL.
         * @param map A map.
         * @param cprops The properties.
         * @param doc The document listener.
         * @return An image.
         */
        @Override
        public Image getImage(String string, Map<String, String> map, ChainedProperties cprops, DocListener doc) {

            // log message
            log.log(Level.INFO, "Trying to get current image.");
            
            // define the image
            Image img = null;

            // lets try
            try {

                // create a new URL
                URL image = new URL(string);

                // get the image
                img = Image.getInstance(image);

                // if the image width is too big
                if (img.getWidth() > 500) {

                    // scale the image
                    img.scalePercent(50);
                }

            } catch (Exception except) {
                                
                // image had a problem
                
                // log message
                log.log(Level.WARNING, "An error occurred while getting the current image. Trying to set the replacement image instead. MESSAGE: {0}", StringUtils.printStackTrace(except));

                // lets try
                try {

                    // create a new image
                    img = Image.getInstance((new Base64()).decode("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"));
                    
                } catch (Exception exception) {
                    
                    // log message
                    log.log(Level.SEVERE, "An error occured while trying to create the image replacement. MESSAGE: {0}", StringUtils.printStackTrace(exception));
                }
            }

            // log message
            log.log(Level.INFO, "Image retrieved succesfully.");
            
            // return image
            return img;
        }
    }
}
