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
 * TeXPrinter.java: The main class.
 */

// package definition
package net.sf.texprinter;

// needed imports
import com.ezware.dialog.task.CommandLink;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import net.sf.texprinter.generators.PDFGenerator;
import net.sf.texprinter.generators.TeXGenerator;
import net.sf.texprinter.model.Question;
import net.sf.texprinter.utils.CommandLineHelper;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.OutputController;
import net.sf.texprinter.utils.StringHandler;
import net.sf.texprinter.utils.StringUtils;

/**
 * The main class.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 1.0
 */
public class TeXPrinter {

    // the debug flag, only for testing purposes
    private static final int DEBUG = 0;

    /**
     * The main method.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        // get the log list
        Logger logList = LogManager.getLogManager().getLogger("");

        // set the level for every handler
        for (Handler handler : logList.getHandlers()) {

            // set it accordingly
            handler.setLevel((DEBUG == 1 ? Level.ALL : Level.OFF));
        }

        // if TeXPrinter is in debug mode
        if (DEBUG == 1) {

            // lets try
            try {

                // add a file handler
                logList.addHandler(new FileHandler("texprinter.xml"));

            } catch (IOException e) {
                // do nothing
            }
        }

        // add the string handler to the log list
        logList.addHandler(new StringHandler());

        // get the output controller
        OutputController outControl = OutputController.getInstance();

        // set the native look and feel
        Dialogs.setNativeLookAndFeel();

        // the question id
        String questionId;

        // command line parser
        CommandLineHelper parser = new CommandLineHelper();

        // if there are arguments
        if (args.length != 0) {

            // parse them
            parser.parse(args);

            // get the question id
            questionId = parser.getQuestionId();

            // and set the flag
            //isCommandLine = true;
            outControl.setCommandLineMode(true);

        } else {

            // ask for the question id
            questionId = askForQuestionId();

        }

        // fetch the question
        Question q = new Question("http://tex.stackexchange.com/questions/" + questionId.trim());

        // create a result option
        int result;

        // if it's not a command line use
        if (!outControl.isCommandLineMode()) {

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
            result = Dialogs.choices(null, "Choose the output", "I'm ready to print the question you provided me. Please choose your option.", 0, listCommands);

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
                TeXGenerator.generate(q, filename);

                // and stop now
                break;

            default:

                // print a message error, this is only possible
                // in a non command line use
                Dialogs.info(null, "Oh, the humanity!", "Let me get straight: why on earth didn't\nyou choose an option?\n\nA kitten dies every time you do this.");

                // exit the application
                System.exit(0);
        }

        // if we are in command line
        if (outControl.isCommandLineMode()) {

            // print message
            System.out.println("Done! The new file '" + filename + "' was generated successfully! Have fun!");

        } else {

            // print messagebox
            Dialogs.info(null, "Question printed successfully!", "Done! The new file <b>" + filename + "</b> was generated successfully! Have fun!");

            // exit application
            System.exit(0);
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
            id = Dialogs.inputQuestion();

            // if the user didn't type anything
            if (id == null || id.length() == 0) {

                // exit the application
                System.exit(0);

            } else {

                // if the input is equal to the question mark
                if (id.trim().equals("?")) {

                    // show the about message
                    Dialogs.showAbout(null);

                    // and exit application
                    System.exit(0);

                } else {

                    // check if the string contains only numbers
                    if (StringUtils.onlyNumbers(id)) {

                        // if so, stop loop
                        keep = false;

                    } else {

                        // if the user does not want to try again
                        if (Dialogs.ask(null, "Oops!", "The ID you typed seems to be invalid. Do you want to try again?") == false) {

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
