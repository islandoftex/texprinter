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
 * ExceptionWindow.java: This class provides the dialog with the
 * exception and error report.
 * Last revision: paulo at temperantia 26 Feb 2012 06:48
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
import net.miginfocom.swing.MigLayout;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.ExecutionLogging;
import net.sf.texprinter.utils.Reporter;
import net.sf.texprinter.utils.UIUtils;

/**
 * Provides the dialog with the exception and error report.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class ExceptionWindow {
    
    // the dialog
    private JDialog theDialog;
    
    // the report status message
    private JLabel reportMessage;
    
    // the close button
    private JButton closeButton;
    
    // the send button
    private JButton sendButton;
    
    // the error reporter
    private Reporter reporter;
    
    // define the execution logging
    private static ExecutionLogging logging = ExecutionLogging.getInstance();

    /**
     * Default constructor.
     */
    public ExceptionWindow() {
        
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
                
                // if the reporter is valid
                if (reporter != null) {
                    
                    // stop it
                    reporter.interrupt();
                }
                
                // dispose
                theDialog.dispose();
                
                // exit
                System.exit(0);
            }
        });
        
        // set new layout
        theDialog.setLayout(new MigLayout());
        
        // create main block
        JPanel mainBlock = new JPanel(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][grow]", "[][]20[grow]10[]"));
        
        // set background to white
        mainBlock.setBackground(Color.WHITE);
        
        // create the icon
        JLabel lblScreenIcon = new JLabel(new ImageIcon(ExceptionWindow.class.getResource("/net/sf/texprinter/ui/images/erroricon.png")));
        
        // create the title
        JLabel lblTitle = new JLabel("<html>Houston, we have a problem.</html>");
        
        // format title
        UIUtils.formatLabelAsTitle(lblTitle);
        
        // create the text
        JEditorPane lblText = new JEditorPane("text/html", "<html>Unfortunately, TeXPrinter raised an exception. It might be a bug, or simply a temporary technical dificulty.<br><br>TeXPrinter keeps track of every internal behaviour in order to ease the debugging process. The generated error report does not include sensible data of any form neither operating system variables. Only the walkthrough execution plan is sent. You can click the help icon to learn more about this feature.</html>");
        
        // format as label
        UIUtils.formatEditorPaneAsLabel(lblText);
        
        // set default font
        UIUtils.setDefaultFontToEditorPane(lblText, true);
       
        // add icon
        mainBlock.add(lblScreenIcon, "cell 0 0 0 4, aligny top");
        
        // add title
        mainBlock.add(lblTitle, "cell 1 0, growx, aligny top");
        
        // add text
        mainBlock.add(lblText, "cell 1 1, growy, width 400, aligny top");
        
        // create message
        reportMessage = new JLabel("Could you send me an error report?");
        
        // set message icon
        reportMessage.setIcon(new ImageIcon(ExceptionWindow.class.getResource("/net/sf/texprinter/ui/images/info.png")));
        
        // add message
        mainBlock.add(reportMessage, "cell 1 2, growx, aligny top");
        
        // create footer
        JPanel footerBlock = new JPanel();
        
        // set layout
        footerBlock.setLayout(new MigLayout("ins dialog", "[][][][]", "[]"));
        
        // add separator
        footerBlock.add( new JSeparator(), "dock north");
        
        // create help button
        JButton helpButton = new JButton(new ImageIcon(ExceptionWindow.class.getResource("/net/sf/texprinter/ui/images/tinyhelp.png")));
        
        // format as label
        UIUtils.formatButtonAsLabel(helpButton);
        
        // add listener
        helpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // show help window
                Dialogs.showHelpErrorReportWindow();
            }
        });
        
        // disable focus
        helpButton.setFocusable(false);
        
        // add button
        footerBlock.add(helpButton);
        
        // add layout fix
	footerBlock.add(new JLabel(), "dock center" );
        
        // create new send button
        sendButton = new JButton("Yes, send it");
        
        // add listener
        sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // disable the send button
                sendButton.setEnabled(false);
                
                // disable the close button
                closeButton.setEnabled(false);
                
                // create a new reporter
                reporter = new Reporter(sendButton, closeButton, reportMessage, logging.dump());
                
                // start
                reporter.start();
            }
        });
        
        // disable focus
        sendButton.setFocusable(false);
        
        // add send button
        footerBlock.add(sendButton);
        
        // create new close button
        closeButton = new JButton("No, thanks");
        
        // add listener
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // check if there is a valid reporter
                if (reporter != null) {
                    
                    // stop it
                    reporter.interrupt();
                }
                
                // exit application
                System.exit(0);
            }
        });
        
        // disable focus
        closeButton.setFocusable(false);
        
        // add close button
        footerBlock.add(closeButton);
        
        // add main block
        theDialog.add(mainBlock, "dock center");
        
        // add footer
        theDialog.add(footerBlock, "dock south");
        
        // pack
        theDialog.pack();
        
    }
    
    /**
     * Shows the exception window.
     */
    public void show() {
        
        // center
        theDialog.setLocationRelativeTo(null);
        
        // set visible
        theDialog.setVisible(true);
    }
    
}
