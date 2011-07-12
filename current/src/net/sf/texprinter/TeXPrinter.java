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
 * <b>TeXPrinter.java</b>: The main class.
 */

// package definition
package net.sf.texprinter;

// needed imports
import com.ezware.dialog.task.CommandLink;
import java.util.ArrayList;
import java.util.List;
import net.sf.texprinter.model.Question;
import net.sf.texprinter.generators.PDFGenerator;
import net.sf.texprinter.generators.TeXGenerator;
import net.sf.texprinter.utils.CommandLineHelper;
import net.sf.texprinter.utils.MessagesHelper;
import net.sf.texprinter.utils.StringHelper;

/**
 * The main class.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0.2
 * @since 1.0
 */
public class TeXPrinter {

    /**
     * The main method.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        // set the native look and feel
        MessagesHelper.setNativeLookAndFeel();

        // the question id
        String questionId;

        // flag for command line use
        boolean isCommandLine = false;

        // command line parser
        CommandLineHelper parser = new CommandLineHelper();

        // if there are arguments
        if (args.length != 0) {

            // parse them
            parser.parse(args);

            // get the question id
            questionId = parser.getQuestionId();

            // and set the flag
            isCommandLine = true;

        } else {

            // ask for the question id
            questionId = askForQuestionId();

        }

        // fetch the question
        Question q = new Question("http://tex.stackexchange.com/questions/" + questionId.trim(), isCommandLine);

        // if the question isn't poisoned, i.e., invalid
        if (!q.isPoisoned()) {

            // create a result option
            int result;

            // if it's not a command line use
            if (!isCommandLine) {

                // create a new list of options
                List<CommandLink> listCommands = new ArrayList<CommandLink>();

                // PDF option
                CommandLink clPDF = new CommandLink("PDF Output", "Generate a PDF output from the provided question ID. All resources (e.g. images) will be\n embedded in the resulting PDF file. The document will be ready for viewing and printing.");

                // add to the list
                listCommands.add(clPDF);

                // TeX option
                CommandLink clTeX = new CommandLink("TeX Output", "Generate a source TeX output from the provided question ID. All resources (e.g. images)\nwill be downloaded to the current directory. You'll need to compile the TeX document.");

                // add to the list
                listCommands.add(clTeX);

                // get the result
                result = MessagesHelper.choices(null, "Choose the output", "I'm ready to print the question you provided me. Please choose your option.", 0, listCommands);

            } else {

                // it's a command line use

                // set the result according to the isPDF flag
                result = (parser.isPDF() ? 0 : 1);
            }

            // set the filename
            String filename = questionId.trim();

            // check the result
            switch (result) {
                case 0:

                    // set the filename to PDF
                    filename = filename + ".pdf";

                    // and generate it
                    PDFGenerator.generate(q, filename);

                    // stop now
                    break;

                case 1:

                    // set the filename to TeX
                    filename = filename + ".tex";

                    // and generate it
                    TeXGenerator.generate(q, filename, isCommandLine);

                    // and stop now
                    break;

                default:

                    // print a message error, this is only possible
                    // in a non command line use
                    MessagesHelper.error(null, "Why?", "Why didn't you choose an option?\nA kitten dies every time you do this.");

                    // exit the application
                    System.exit(0);
            }

            // if we are in command line
            if (isCommandLine) {

                // print message
                System.out.println("Done! The new file '" + filename + "' was generated successfully! Have fun!");

            } else {

                // print messagebox
                MessagesHelper.info(null, "Question printed successfully!", "Done! The new file <b>" + filename + "</b> was generated successfully! Have fun!");

                // exit application
                System.exit(0);
            }
        } else {

            // if we are in command line
            if (isCommandLine) {

                // display error message
                System.out.println("Sorry, I couldn't create a PDF file from the question ID you provided.\nMaybe the ID leads to a 404 page or my internal algorithm failed at some\npoint.");

            } else {

                // display a fancy error messagebox
                MessagesHelper.error(null, "Houston, we have a problem.", "Sorry, I couldn't create a PDF file from the question ID you provided.\nMaybe the ID leads to a 404 page or my internal algorithm failed at some point.");

                // exit application
                System.exit(0);
            }
        }
    }

    /**
     * Asks for the question id.
     * @return The question id.
     */
    private static String askForQuestionId() {

        // a flag for the main loop
        boolean keep = true;

        // the id
        String id = "";

        // while there's no nice input
        while (keep) {

            // ask for id
            id = MessagesHelper.input(null, "Welcome to TeXPrinter!", "Please type the question ID you want me to print.\nIf you want me to display the application version, type <b>?</b>.", "");

            // if the user didn't type anything
            if (id == null || id.length() == 0) {

                // exit the application
                System.exit(0);
                
            } else {
                
                // if the input is equal to the question mark
                if (id.trim().equals("?")) {
                    
                    // show the about message
                    MessagesHelper.showAbout(null);
                    
                    // and exit application
                    System.exit(0);
                    
                } else {
                    
                    // check if the string contains only numbers
                    if (StringHelper.onlyNumbers(id)) {
                        
                        // if so, stop loop
                        keep = false;
                        
                    } else {
                        
                        // if the user does not want to try again
                        if (MessagesHelper.ask(null, "Oops!", "The ID you typed seems to be invalid. Do you want to try again?") == false) {
                            
                            // exit application
                            System.exit(0);
                        }
                    }
                }
            }
        }

        // return the id
        return id;
    }
}
