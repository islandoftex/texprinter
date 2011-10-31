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
 * ChangelogPanel.java: This class provides the panel for changelog.
 */

// package definition
package net.sf.texprinter.ui;

// needed imports
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.texprinter.utils.StringUtils;
import net.sf.texprinter.utils.UIUtils;

/**
 * Provides the panel for changelog.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 2.0
 */
public class ChangelogPanel extends javax.swing.JPanel {

    // the application logger
    private static final Logger log = Logger.getLogger(ChangelogPanel.class.getCanonicalName());
    
    /**
     * Constructor method.
     */
    public ChangelogPanel() {

        // create the components
        initComponents();

        // set editor to false
        changelogEditor.setEditable(false);

        // get the HTML page
        URL helpURL = ChangelogPanel.class.getResource("/net/sf/texprinter/config/changelog.html");

        // if valid
        if (helpURL != null) {

            // lets try
            try {

                // set the page to the editor
                changelogEditor.setPage(helpURL);
                
            } catch (IOException e) {
                
                // log message
                log.log(Level.SEVERE, "I could not load the HTML changelog. MESSAGE: {0}", StringUtils.printStackTrace(e));
            }
        } else {
            
            // log message
            log.log(Level.SEVERE, "HTML changelog was not found.");
            
        }
        
        // set the default font to the editor.
        UIUtils.setDefaultFontToEditorPane(changelogEditor);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        changelogScroller = new javax.swing.JScrollPane();
        changelogEditor = new javax.swing.JEditorPane();

        setOpaque(false);

        changelogEditor.setContentType("html/plain");
        changelogScroller.setViewportView(changelogEditor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(changelogScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(changelogScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane changelogEditor;
    private javax.swing.JScrollPane changelogScroller;
    // End of variables declaration//GEN-END:variables
}
