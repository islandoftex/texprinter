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
 * CommandLineHelper.java: This class provides command line features
 * for the main application.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import net.sf.texprinter.config.Configuration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Provides command line features for the main application.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 1.0
 */
public class CommandLineHelper {

    // flag to determine if the output will be PDF
    private boolean isPDF = true;
    
    // the question id
    private String questionId;
    
    // the command line options
    private Options commandLineOptions;

    /**
     * Constructor method.
     */
    public CommandLineHelper() {

        // argument for question id
        Option questionIdOption = OptionBuilder.withLongOpt("question-id").withDescription("the TeX.SX question ID").hasArg().withArgName("ID").create();

        // argument for output format
        Option outputFormatOption = OptionBuilder.withLongOpt("output").withDescription("the output format ('pdf' or 'tex')").hasArg().withArgName("EXT").create();

        // argument for version
        Option versionOption = OptionBuilder.withLongOpt("version").withDescription("print the application version").create();

        // argument for help
        Option helpOption = OptionBuilder.withLongOpt("help").withDescription("print the help message").create();

        // create a new list of options
        commandLineOptions = new Options();

        // add the question id
        commandLineOptions.addOption(questionIdOption);

        // add the output format
        commandLineOptions.addOption(outputFormatOption);

        // add the version
        commandLineOptions.addOption(versionOption);

        // add the help
        commandLineOptions.addOption(helpOption);

    }

    /**
     * Getter for the question id.
     * @return The question id.
     */
    public String getQuestionId() {

        // return the question id
        return questionId;
    }

    /**
     * Checks if the output format is set to PDF.
     * @return A boolean to determine if the output will be PDF.
     */
    public boolean isPDF() {

        // return the flag
        return isPDF;
    }

    /**
     * Parses the command line arguments.
     * @param args The command line arguments.
     */
    public void parse(String args[]) {

        // create a new parser
        CommandLineParser parser = new GnuParser();

        // lets try
        try {

            // print application header
            printHeader();

            // parse the command line arguments
            CommandLine line = parser.parse(commandLineOptions, args);

            // if we are dealing with version
            if (line.hasOption("version")) {

                // set the configuration retriever
                Configuration config = new Configuration();

                // print the application version
                System.out.println("TeXPrinter " + config.getAppVersionNumber() + " - " + config.getAppVersionName());

                // and exit
                System.exit(0);

            } else {

                // the help argument
                if (line.hasOption("help")) {

                    // print usage
                    printUsage();

                    // and exit
                    System.exit(0);

                } else {

                    // check for both question id and output format
                    if (line.hasOption("question-id") && line.hasOption("output")) {

                        // set the question id
                        questionId = line.getOptionValue("question-id");

                        // if the output is not set to PDF or TeX
                        if (!line.getOptionValue("output").equalsIgnoreCase("pdf") && !line.getOptionValue("output").equalsIgnoreCase("tex")) {

                            // print error message and exit
                            printErrorMessage("You need to provide either PDF or TeX output.");

                        } else {

                            // set the format flag
                            this.isPDF = line.getOptionValue("output").equalsIgnoreCase("pdf");
                        }

                    } else {

                        // none of the above, print error message and exit
                        printErrorMessage("Missing or wrong parameters.");
                    }
                }
            }

        } catch (ParseException exp) {

            // oops something went wrong, print error message and exit
            printErrorMessage(exp.getMessage());

        }
    }

    /**
     * Prints an error message and exits the application.
     * @param message The message.
     */
    private void printErrorMessage(String message) {

        // print message
        System.out.println("Parsing failed. Reason: " + message + "\n");

        // print usage
        printUsage();

        // and exit
        System.exit(0);
    }

    /*
     * Prints the usage.
     */
    private void printUsage() {

        // create a new help formatter
        HelpFormatter formatter = new HelpFormatter();

        // print the options for arguments
        formatter.printHelp("texprinter [ --question-id ID --output EXT | --version | --help ]", commandLineOptions);
    }

    /**
     * Prints the application header.
     */
    private void printHeader() {

        // print header
        System.out.println("TeXPrinter - A TeX.SX question printer");
        System.out.println("Copyright (c) 2011, Paulo Roberto Massa Cereda");
        System.out.println("All rights reserved.\n");
    }
}
