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
 * UIUtils.java: This class provides UI helper methods.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.awt.Font;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;

/**
 * Provides UI helper methods.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 2.0
 */
public class UIUtils {
    
    /**
     * Converts a JButton to a JLabel.
     * @param button The button.
     */
    public static void convertToLabel(JButton button) {
        
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
     * Set the operating system font to the editor.
     * @param editor The editor.
     */
    public static void setDefaultFontToEditorPane(JEditorPane editor) {
        
        // get the system font
        Font font = UIManager.getFont("Label.font");
        
        // set the body rule
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
                "font-size: " + font.getSize() + "pt; margin-left: 0px; margin-top: 0px; }";
        
        // set the list rule
        String listRule = "ol { margin-left: 20px; list-style-type: square; }";
        
        // add the body rule
        ((HTMLDocument)editor.getDocument()).getStyleSheet().addRule(bodyRule);
        
        // add the list rule
        ((HTMLDocument)editor.getDocument()).getStyleSheet().addRule(listRule);
    }
    
    /**
     * Loads the icon from a package.
     * @param theClass The class.
     * @param path The path.
     * @param description The description.
     * @return The icon.
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
    
}
