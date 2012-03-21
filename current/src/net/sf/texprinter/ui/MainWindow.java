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
 * MainWindow.java: This class provides the dialog with main window.
 * Last revision: paulo at temperantia 26 Feb 2012 10:01
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
import javax.swing.*;
import javax.swing.text.Document;
import net.miginfocom.swing.MigLayout;
import net.sf.texprinter.utils.ButtonStateController;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.NumericDocument;
import net.sf.texprinter.utils.UIUtils;


/**
 * Provides the dialog with main window.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class MainWindow {
    
    // the dialog
    private JDialog theDialog;
    
    // the result
    private String theResult;
    
    // the textfield
    private JTextField theInputField;

    /**
     * Default constructor.
     */
    public MainWindow() {
        
        // set a default value
        theResult = "";
        
        // new dialog
        theDialog = new JDialog();
        
        // set title
        theDialog.setTitle("TeXPrinter");
        
        // set minimum size
        theDialog.setMinimumSize(new Dimension(300, 150));
        
        // disable resize
        theDialog.setResizable(false);
        
        // set modal
        theDialog.setModal(true);
        
        // add listener
        theDialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                
                // set result to null
                theResult = null;
                
                // dispose dialo
                theDialog.dispose();
            }
        });

        // add new layout
        theDialog.setLayout(new MigLayout());
        
        // create main block
        JPanel mainBlock = new JPanel(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][grow]", "[][]10[grow][]"));
        
        // set background to white
        mainBlock.setBackground(Color.WHITE);
        
        // create icon
        JLabel lblScreenIcon = new JLabel(new ImageIcon(MainWindow.class.getResource("/net/sf/texprinter/ui/images/commenticon.png")));
        
        // create title
        JLabel lblTitle = new JLabel("<html>Welcome to TeXPrinter!</html>");
        
        // format title
        UIUtils.formatLabelAsTitle(lblTitle);
        
        // create text
        JEditorPane lblText = new JEditorPane("text/html", "<html>Please type the question ID you want me to print. If you want me to display the application version, just click the help icon.</html>");
        
        // format text as label
        UIUtils.formatEditorPaneAsLabel(lblText);
        
        // set default font
        UIUtils.setDefaultFontToEditorPane(lblText, true);
        
        // add icon
        mainBlock.add(lblScreenIcon, "cell 0 0 0 4, aligny top");
        
        // add title
        mainBlock.add(lblTitle, "cell 1 0, growx, aligny top");
        
        // add text
        mainBlock.add(lblText, "cell 1 1, growy, width 350, aligny top");        
        
        // create textfield
        theInputField = new JTextField();
        
        // set the document
        theInputField.setDocument(new NumericDocument());
        
        // add textfield
        mainBlock.add(theInputField, "cell 1 2, growx, aligny top");
        
        // create new license block
        JPanel licenseBlock = new JPanel();
        
        // add new layout
        licenseBlock.setLayout(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][]", "[]"));
        
        // turn off opaque
        licenseBlock.setOpaque(false);
        
        // add image
        licenseBlock.add(new JLabel(new ImageIcon(MainWindow.class.getResource("/net/sf/texprinter/ui/images/license.png"))));
        
        // create new license text
        JEditorPane licenseText = new JEditorPane("text/html", "<html>All user contributions licensed under cc-wiki with attribution required.</html>");
        
        // format text as label
        UIUtils.formatEditorPaneAsLabel(licenseText);
        
        // set default font
        UIUtils.setDefaultFontToEditorPane(licenseText, true);
        
        // add license block
        licenseBlock.add(licenseText, "growy, width 240");
        
        // add main block
        mainBlock.add(licenseBlock, "cell 1 3, aligny top");

        // create footer
        JPanel footerBlock = new JPanel();
        
        // set new layout
        footerBlock.setLayout(new MigLayout("ins dialog", "[][][][]", "[]"));
        
        // add separator
        footerBlock.add( new JSeparator(), "dock north");
        
        // create help button
        JButton helpButton = new JButton(new ImageIcon(MainWindow.class.getResource("/net/sf/texprinter/ui/images/tinyhelp.png")));
        
        // format as label
        UIUtils.formatButtonAsLabel(helpButton);
        
        // add listener
        helpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // show about window
                Dialogs.showAboutWindow();
            }
        });
                
        // disable focus
        helpButton.setFocusable(false);
        
        // add button
        footerBlock.add(helpButton);
        
        // add layout fix
	footerBlock.add(new JLabel(), "dock center" );
        
        // create continue button
        JButton continueButton = new JButton("Continue");
        
        // create exit button
        JButton exitButton = new JButton("Exit");
        
        // disable continue button
        continueButton.setEnabled(false);
        
        // add listener
        continueButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // set value
                theResult = theInputField.getText();
                
                // make it invisible
                theDialog.setVisible(false);
            }
        });
        
        // get document
        Document document = theInputField.getDocument();
        
        // add document listener
        document.addDocumentListener(new ButtonStateController(continueButton));
        
        // add listener
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // exit application
                System.exit(0);
            }
        });
        
        // disable focus
        continueButton.setFocusable(false);
        exitButton.setFocusable(false);
        
        // add continue button
        footerBlock.add(continueButton);
        
        // add exit button
        footerBlock.add(exitButton);
        
        // add main block
        theDialog.add(mainBlock, "dock center");
        
        // add footer
        theDialog.add(footerBlock, "dock south");
        
        // pack
        theDialog.pack();
        
    }

    /**
     * Shows the main window.
     * @return A string containing the question ID.
     */
    public String show() {
        
        // center dialog
        theDialog.setLocationRelativeTo(null);

        // set visible
        theDialog.setVisible(true);

        // get value
        String value = getResult();
        
        // dispose dialog
        theDialog.dispose();
        
        // return the value
        return value;
    }
    
    /**
     * Getter for result.
     * @return The result.
     */
    public String getResult() {
        
        // return the result
        return theResult;
    }

}
