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
 * Dialogs.java: This class provides message features to the other classes.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import com.ezware.dialog.task.CommandLink;
import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialog.StandardCommand;
import com.ezware.dialog.task.TaskDialogs;
import java.awt.Window;
import java.util.List;
import javax.swing.UIManager;
import net.sf.texprinter.config.Configuration;
import net.sf.texprinter.ui.AboutPanel;
import net.sf.texprinter.ui.ChangelogPanel;
import net.sf.texprinter.ui.ExceptionPanel;
import net.sf.texprinter.ui.InputPanel;

/**
 * Provides message features to the other classes.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 1.0
 */
public class Dialogs {

    // define the output controller
    private static OutputController outControl = OutputController.getInstance();
    
    /**
     * Displays an info messagebox.
     * @param window The window.
     * @param title The title of the messagebox.
     * @param text The text of the messagebox.
     */
    public static void info(Window window, String title, String text) {

        // if it is GUI mode
        if (!outControl.isCommandLineMode()) {
            
            // display the dialog
            TaskDialogs.inform(window, title, text);
        }
        else {
            
            // display message
            System.out.println(text);
        }
        
        
    }

    /**
     * Displays an error messagebox.
     * @param window The window.
     * @param title The title of the messagebox.
     * @param text The text of the messagebox.
     */
    public static void error(Window window, String title, String text) {

        // if it is GUI mode
        if (!outControl.isCommandLineMode()) {
            
            // error window
            TaskDialogs.error(window, title, text);
            
        } else {
            
            // display error
            System.out.println(text);
        }
    }

    /**
     * Displays a question messagebox.
     * @param window The window.
     * @param title The title of the messagebox.
     * @param text The text of the messagebox.
     * @return A boolean with the chosen option.
     */
    public static boolean ask(Window window, String title, String text) {

        // return the result of the dialog
        return TaskDialogs.ask(window, title, text);
    }

    /**
     * Displays an input messagebox.
     * @param window The window.
     * @param title The title of the messagebox.
     * @param text The text of the messagebox.
     * @param defaultValue The default value.
     * @return The typed value.
     */
    public static String input(Window window, String title, String text, String defaultValue) {

        // return the typed value of the dialog
        return TaskDialogs.input(window, title, text, defaultValue);
    }

    /**
     * Displays a list of options.
     * @param window The window.
     * @param title The title of the messagebox.
     * @param text The text of the messagebox.
     * @param choice The default option.
     * @param choices A list of options.
     * @return The index of the chosen option.
     */
    public static int choices(Window window, String title, String text, int choice, List<CommandLink> choices) {

        // return the result of the dialog
        return TaskDialogs.choice(window, title, text, choice, choices);
    }

    /**
     * Displays the exception.
     */
    public static void exception() {

        // if it is GUI mode
        if (!outControl.isCommandLineMode()) {
        
            // create a new window
            TaskDialog exceptionBox = new TaskDialog(null, "Exception");

            // create a new panel
            ExceptionPanel exceptionPanel = new ExceptionPanel();

            // set icon
            exceptionBox.setIcon(TaskDialog.StandardIcon.ERROR);

            // set instruction
            exceptionBox.setInstruction("Houston, we have a problem.");

            // set text
            exceptionBox.setText("Unfortunately, TeXPrinter raised an exception. It might be a bug, or\nsimply a temporary technical dificulty.\n\nTeXPrinter keeps track of every internal behaviour in order to ease\nthe debugging process. The generated error report does not include\nsensible data of any form neither operating system variables. Only\nthe walkthrough execution plan is sent.");

            // set fixed component
            exceptionBox.setFixedComponent(exceptionPanel);

            // show window
            exceptionBox.show();

            // lets try
            try {

                // stop the error transmission
                exceptionPanel.stop();

            } catch (Exception ex) {

                // don't do anything
            }

            // exit application
            System.exit(0);
            
        }
        else {
            
            // Display the exception
            System.out.println("Unfortunately, TeXPrinter raised an exception. It might be a bug, or\nsimply a temporary technical dificulty.");
            
            // exit the application
            System.exit(0);
        }

    }


    /*
     * Set the native look and feel according to the operating system.
     */
    public static void setNativeLookAndFeel() {

        // let's try
        try {

            // set the default look and feel as the system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            // something happened, but we won't do nothing
        }
    }

    /**
     * Show the about messagebox.
     * @param window The window.
     */
    public static void showAbout(Window window) {

        // define the copyright symbol
        final String COPYRIGHT = "\u00a9";

        // create a new configuration
        Configuration config = new Configuration();

        // create a new window
        TaskDialog aboutBox = new TaskDialog(window, "About");
        
        // create a new panel
        AboutPanel aboutPanel = new AboutPanel();
        
        // set the icon
        aboutBox.setIcon(TaskDialog.StandardIcon.INFO);
        
        // set the instruction
        aboutBox.setInstruction("TeXPrinter");
        
        // set the text
        aboutBox.setText("<i>Version " + config.getAppVersionNumber() + " - " + config.getAppVersionName() + "</i>\n\nCopyright " + COPYRIGHT + " 2011, Paulo Roberto Massa Cereda\nAll rights reserved.\n\nThis application is licensed under the <u>New BSD License</u>. I want to call your attention\nto the fact that the <i>New BSD License</i> has been verified as a <i>GPL-compatible free\nsoftware license</i> by the Free Software Foundation, and has been vetted as an <i>open\nsource license</i> by the Open Source Initiative.");
        
        // set the fixed component
        aboutBox.setFixedComponent(aboutPanel);
        
        // show
        aboutBox.show();
        
        // lets try to finish the version checker
        try {
            
            // stop the version checker
            aboutPanel.stop();
        } catch (Exception e) {
            
            // something happened
        }

    }

    /**
     * Show the changelog window.
     * @param window The window.
     */
    public static void showChangelog(Window window) {

        // create a new configuration
        Configuration config = new Configuration();

        // create a new window
        TaskDialog changelogBox = new TaskDialog(window, "Changelog");
        
        // create a new panel
        ChangelogPanel changelogPanel = new ChangelogPanel();
        
        // set icon
        changelogBox.setIcon(TaskDialog.StandardIcon.INFO);
        
        // set instruction
        changelogBox.setInstruction("TeXPrinter " + config.getAppVersionNumber());
        
        // set text
        changelogBox.setText("Changes in this version:");
        
        // set fixed component
        changelogBox.setFixedComponent(changelogPanel);
        
        // show window
        changelogBox.show();

    }

    /**
     * Show the input dialog.
     * @return The textfield value.
     */
    public static String inputQuestion() {
        
        // create new window
        TaskDialog mainWindow = new TaskDialog(null, "TeXPrinter");
        
        // set icon
        mainWindow.setIcon(TaskDialog.StandardIcon.INFO);
                
        // set instruction
        mainWindow.setInstruction("Welcome to TeXPrinter!");
        
        // set text
        mainWindow.setText("Please type the question ID you want me to print.\nIf you want me to display the application version, type <b>?</b>.");
        
        // create new panel
        InputPanel inputPanel = new InputPanel();
        
        // set fixed component
        mainWindow.setFixedComponent(inputPanel);
        
        // set window buttons
        mainWindow.setCommands(StandardCommand.OK, StandardCommand.CANCEL);
        
        // return the proper value
        return (mainWindow.show().equals(StandardCommand.OK) ? inputPanel.getTextValue() : null);
    }
}
