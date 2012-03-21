/**
 * \cond LICENSE
 * ********************************************************************
 * This is a conditional block for preventing the DoxyGen documentation
 * tool to include this license header within the description of each
 * source code file. If you want to include this block, please define
 * the LICENSE parameter into the provided DoxyFile.
 * ********************************************************************
 *
 * TeXPrinter - A TeX.SX question printer Copyright (c) 2012, Paulo Roberto
 * Massa Cereda All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the project's author nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * ********************************************************************
 * End of the LICENSE conditional block
 * ********************************************************************
 * \endcond
 *
 * UIUtils.java: This class provides UI helper methods.
 * Last revision: paulo at temperantia 26 Feb 2012 05:12
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.net.URL;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import org.apache.commons.lang.SystemUtils;

/**
 * Provides UI helper methods. I decided to pack all UI helper methods to
 * this class, so in fact we don't need to create instances of it, but
 * rather call the static methods.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.0
 */
public class UIUtils {

    /**
     * Formats a JButton as a JLabel. In some cases, I just want the a
     * JButton component to act as a JLabel, so this method does this
     * for me.
     *
     * @param button The button. Some makeup is made to the button, so
     * it will look as a good old JLabel component.
     */
    public static void formatButtonAsLabel(JButton button) {

        // disable focus paint
        button.setFocusPainted(false);

        // redefine margins
        button.setMargin(new Insets(0, 0, 0, 0));

        // disable content area fill
        button.setContentAreaFilled(false);

        // disable border paint
        button.setBorderPainted(false);

        // disable property
        button.setOpaque(false);
    }

    /**
     * Set the label font to the editor. When the content type
     * is set to 'text/html', the plain visualization is very ugly, so this
     * method will set the default JLabel font to a JEditorPane.
     *
     * @param editor The editor. The CSS stylesheet will be added to it.
     * @param justify A flag representing full justification. If true, the
     * text will be fully justified.
     */
    public static void setDefaultFontToEditorPane(JEditorPane editor, boolean justify) {

        // get the system font
        Font font = UIManager.getFont("Label.font");

        // set the body rule
        String bodyRule = "body { font-family: " + font.getFamily() + "; "
                + "font-size: " + font.getSize() + "pt; margin-left: 0px; margin-top: 0px; " + (justify ? "text-align: justify;" : "") + " }";

        // set the list rule
        String listRule = "ol { margin-left: 20px; list-style-type: square; }";

        // add the body rule
        ((HTMLDocument) editor.getDocument()).getStyleSheet().addRule(bodyRule);

        // add the list rule
        ((HTMLDocument) editor.getDocument()).getStyleSheet().addRule(listRule);
    }

    /**
     * Loads the image icon from a package. Another simple method, this one
     * will load an image from inside a package and then add it to an ImageIcon
     * object.
     *
     * @param theClass The class. It's used to get the resource.
     * @param path The path. The path is used to make the search more reliable.
     * @param description The description. Telling what the image actually is
     * might somehow help.
     * @return The image icon in a ImageIcon object.
     */
    public static ImageIcon createImageIcon(Class theClass, String path, String description) {

        // get the resource
        URL imgURL = theClass.getClass().getResource(path);

        // check if not null
        if (imgURL != null) {

            // return new icon
            return new ImageIcon(imgURL, description);

        } else {

            // return null
            return null;
        }
    }

    /**
     * Formats a button to a label with wider margins. In short, this
     * method is similar to the button/label one, but with wider margins.
     *
     * @param button The button. Nothing new here.
     */
    public static void formatButtonAsList(JButton button) {

        // disable focus paint
        button.setFocusPainted(false);

        // fix the margins
        button.setMargin(new Insets(0, 0, 0, 5));

        // disable content area fill
        button.setContentAreaFilled(false);

        // disable border paint
        button.setBorderPainted(false);

        // disable property
        button.setOpaque(false);
    }

    
    /**
     * Formats a label as a title. The idea behind this method is to make
     * a JLabel component to look fancy when acting as a title to a window.
     * 
     * @param label The label to be formatted as a title.
     */
    public static void formatLabelAsTitle(JLabel label) {

        // if it's not Linux
        if (!SystemUtils.IS_OS_LINUX) {
            
            // simply increase the font size
            label.setFont(label.getFont().deriveFont(14f));
            
        } else {
                        
            // it's Linux, so add a bold style too.
            label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
            
        }
        
        // paint it as blue
        label.setForeground(new Color(35, 107, 178));
    }

    /**
     * Format an editor to act as a label. I decided to replace all the
     * occurrences of multiline JLabel's to JEditorPane's instead, so I
     * want them to look exactly like ordinary JLabels.
     * 
     * @param editor The editor to be formatted as a label.
     */
    public static void formatEditorPaneAsLabel(JEditorPane editor) {
        
        // disable it
        editor.setEnabled(false);
        
        // make it read only
        editor.setEditable(false);
        
        // transparent
        editor.setOpaque(false);
                
        // set the disabled color as black
        editor.setDisabledTextColor(Color.BLACK);
    }
    
        /**
     * Set the native look and feel according to the operating system.
     */
    public static void setAluminiumLookAndFeel() {

        // let's try
        try {

            // set the aluminium look and feel as the system look and feel
            UIManager.setLookAndFeel(new AluminiumLookAndFeel());

        } catch (Exception e) {
            // something happened, but we won't do nothing
        }
    }
}
