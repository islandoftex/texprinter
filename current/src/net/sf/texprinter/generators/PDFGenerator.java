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
 * <b>PDFGenerator.java</b>: This class is responsible for generating a PDF
 * file from a Question object.
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.texprinter.model.Comment;
import net.sf.texprinter.model.Post;
import net.sf.texprinter.model.Question;

/**
 * Provides the PDF generation from a Question object.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class PDFGenerator {

    /**
     * Generates a PDF file from a Question object.
     * @param question The question.
     * @param filename The filename.
     */
    public static void generate(Question question, String filename) {
        
        // lets try
        try {
            
            // create a new PDF document
            Document document = new Document();
            
            // define a new PDF Writer
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            
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
            
            // add the question title to the document
            document.add(paragraphQuestionTitle);
            
            // set the asker font
            Font askerFont = new Font(FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.DARK_GRAY);
            
            // set the chunk for the asker
            Chunk questionAsker = new Chunk("Asked by " + question.getQuestion().getUser().getName() + " (" + question.getQuestion().getUser().getReputation() + ") on " + question.getQuestion().getDate(), askerFont);
            
            // create a paragraph from that chunk
            Paragraph paragraphQuestionAsker = new Paragraph(questionAsker);
            
            // add the asker to the document
            document.add(paragraphQuestionAsker);
            
            // create a line separator
            LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -5);
            
            // add the line to the document
            document.add(line);
            
            // add a new line
            document.add(Chunk.NEWLINE);

            // create a list of elements from the
            List<Element> questionTextObjects = getPostText(question.getQuestion().getText());

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
                    Paragraph paragraphCommentAuthor = new Paragraph(questionComment.getAuthor() + " on " + questionComment.getDate(), askerFont);
                    
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
                
                // create a new chunk
                Chunk noAnswersTitle = new Chunk("Sorry, this question has no answers yet.", titleFont);
                
                // create a paragraph from that chunk
                Paragraph paragraphNoAnswersTitle = new Paragraph(noAnswersTitle);
                
                // add the paragraph to the document
                document.add(paragraphNoAnswersTitle);
                
            } else {
                
                // there are answers, so create a counter for answers
                int answerCount = 1;

                // for each answer
                for (Post answer : answersList) {

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
                    Chunk questionAnswerer = new Chunk("Answered by " + answer.getUser().getName() + " (" + answer.getUser().getReputation() + ") on " + answer.getDate() + answerAccepted, askerFont);
                    
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
                            Paragraph paragraphAnswerCommentAuthor = new Paragraph(answerComment.getAuthor() + " on " + answerComment.getDate(), askerFont);
                            
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

            // close the document
            document.close();
            
        } catch (Exception e) {
            
            // something happened, but we won't do anything
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
            
            // lets try
            try {
                
                // create a new URL
                URL image = new URL(string);
                
                // get the image
                Image img = Image.getInstance(image);
                
                // if the image width is too big
                if (img.getWidth() > 500) {
                    
                    // scale the image
                    img.scalePercent(50);
                }
                
                // return image
                return img;

            } catch (Exception e) {
                // something bad happened, but we don't care
            }
            return null;
        }
    }
}
