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
 * ChangelogWindow.java: This class provides the dialog with the
 * application changelog.
 * Last revision: paulo at temperantia 26 Feb 2012 06:30
 */

// package definition
package net.sf.texprinter.ui;

// needed imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import net.sf.texprinter.config.Configuration;
import net.sf.texprinter.utils.StringUtils;
import net.sf.texprinter.utils.UIUtils;

/**
 * Provides the dialog with the application changelog.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class ChangelogWindow {
    
    // the dialog
    private JDialog theDialog;
    
    // the application logger
    private static final Logger log = Logger.getLogger(ChangelogWindow.class.getCanonicalName());

    /**
     * Default constructor.
     */
    public ChangelogWindow() {
        
        // the dialog
        theDialog = new JDialog();
        
        // set title
        theDialog.setTitle("TeXPrinter");
        
        // set minimum size
        theDialog.setMinimumSize(new Dimension(300, 150));
        
        // disable resize
        theDialog.setResizable(false);
        
        // set modal
        theDialog.setModal(true);
        
        // add a listener
        theDialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                
                // dispose it
                theDialog.dispose();
            }
        });
        
        // set a new layout
        theDialog.setLayout(new MigLayout());
        
        // create a main panel
        JPanel mainBlock = new JPanel(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][grow]", "[][]20[grow]10[]"));
        
        // set the background color to white
        mainBlock.setBackground(Color.WHITE);
        
        // create a new icon
        JLabel lblScreenIcon = new JLabel(new ImageIcon(ChangelogWindow.class.getResource("/net/sf/texprinter/ui/images/computericon.png")));
        
        // create a new configuration
        Configuration config = new Configuration();
        
        // create a new title
        JLabel lblTitle = new JLabel("<html>Changelog - TeXPrinter " + config.getAppVersionNumber() + "</html>");
        
        // format it as a title
        UIUtils.formatLabelAsTitle(lblTitle);
        
        // create a new text
        JEditorPane lblText = new JEditorPane("text/html", "<html>New and noteworthy in this version.</html>");
        
        // format it as a label
        UIUtils.formatEditorPaneAsLabel(lblText);
        
        // set the default font
        UIUtils.setDefaultFontToEditorPane(lblText, true);
       
        // add icon
        mainBlock.add(lblScreenIcon, "cell 0 0 0 4, aligny top");
        
        // add title
        mainBlock.add(lblTitle, "cell 1 0, growx, aligny top");
        
        // add text
        mainBlock.add(lblText, "cell 1 1, growx, aligny top");
        
        // create a new changelog
        JEditorPane changelogEditor = new JEditorPane();
        
        // create a scroll pane
        JScrollPane scrollerPanel = new JScrollPane(changelogEditor);
                
        // add the changelog
        mainBlock.add(scrollerPanel, "cell 1 2, growx, width 350!, height 150!, aligny top");
        
        // create a footer
        JPanel footerBlock = new JPanel();
        
        // set layout
        footerBlock.setLayout(new MigLayout("ins dialog", "[][][][]", "[]"));
        
        // add a separator
        footerBlock.add( new JSeparator(), "dock north");
        
        // set editor to false
        changelogEditor.setEditable(false);

        // get the HTML page
        URL helpURL = ChangelogWindow.class.getResource("/net/sf/texprinter/config/changelog.html");

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
        UIUtils.setDefaultFontToEditorPane(changelogEditor, false);
        
                
        // add a layout fix        
	footerBlock.add(new JLabel(), "dock center" );      
        
        // create a new button
        JButton closeButton = new JButton("Close");
        
        // add a new listener
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // make it invisible
                theDialog.setVisible(false);
                
                // dispose it
                theDialog.dispose();
            }
        });
        
        // disable focus
        closeButton.setFocusable(false);
        
        // add button
        footerBlock.add(closeButton);
        
        // add main block
        theDialog.add(mainBlock, "dock center");
        
        // add footer
        theDialog.add(footerBlock, "dock south");
        
        // pack
        theDialog.pack();
        
    }
    
    /**
     * Shows the changelog window.
     */
    public void show() {
        
        // center dialog
        theDialog.setLocationRelativeTo(null);
        
        // set visible
        theDialog.setVisible(true);
    }
    
}
