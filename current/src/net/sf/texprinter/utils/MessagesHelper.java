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
 * <b>MessagesHelper.java</b>: This class provides message features to the
 * other classes.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import com.ezware.dialog.task.CommandLink;
import com.ezware.dialog.task.TaskDialogs;
import java.awt.Window;
import java.util.List;
import javax.swing.UIManager;

/**
 * Provides message features to the other classes.
 * @author Paulo Roberto Massa Cereda
 * @version 1.0.2
 * @since 1.0
 */
public class MessagesHelper {

    /**
     * Displays an info messagebox.
     * @param window The window.
     * @param title The title of the messagebox.
     * @param text The text of the messagebox.
     */
    public static void info(Window window, String title, String text) {
                
        // display the dialog
        TaskDialogs.inform(window, title, text);
    }

    /**
     * Displays an error messagebox.
     * @param window The window.
     * @param title The title of the messagebox.
     * @param text The text of the messagebox.
     */
    public static void error(Window window, String title, String text) {

        // display the dialog
        TaskDialogs.error(window, title, text);
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
     * @param e The exception.
     */
    public static void exception(Throwable e) {

        // display the exception
        TaskDialogs.showException(e);
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
     * @param window 
     */
    public static void showAbout(Window window) {

        // define the copyright symbol
        final String COPYRIGHT = "\u00a9";

        // call the proper method
        TaskDialogs.inform(window, "TeXPrinter", "<i>Version 1.0.2 - Tasty Waffles</i>\n\nCopyright " + COPYRIGHT + " 2011, Paulo Roberto Massa Cereda\nAll rights reserved.\n\nThis application is licensed under the <u>New BSD License</u>. I want to call your attention\nto the fact that the <i>New BSD License</i> has been verified as a <i>GPL-compatible free\nsoftware license</i> by the Free Software Foundation, and has been vetted as an <i>open\nsource license</i> by the Open Source Initiative.");
    }
}
