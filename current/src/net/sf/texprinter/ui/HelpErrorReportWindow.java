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
 * HelpErrorReportWindow.java: This class provides the dialog with the
 * help about the error report.
 * Last revision: paulo at temperantia 26 Feb 2012 06:54
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
import net.sf.texprinter.utils.UIUtils;

/**
 * Provides the dialog with the help about the error report.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class HelpErrorReportWindow {
    
    // the dialog
    private JDialog theDialog;
    
    /**
     * Default constructor.
     */
    public HelpErrorReportWindow() {
        
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
        
        // add listener
        theDialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                
                // dispose
                theDialog.dispose();
            }
        });
     
        // set new layout
        theDialog.setLayout(new MigLayout());
        
        // create new block
        JPanel mainBlock = new JPanel(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][grow]", "[][]10[]"));
        
        // set background to white
        mainBlock.setBackground(Color.WHITE);
        
        // create icon
        JLabel lblScreenIcon = new JLabel(new ImageIcon(HelpErrorReportWindow.class.getResource("/net/sf/texprinter/ui/images/helpicon.png")));
        
        // create title
        JLabel lblTitle = new JLabel("<html>About the error report</html>");
        
        // format title
        UIUtils.formatLabelAsTitle(lblTitle);
        
        // create text
        JEditorPane lblText = new JEditorPane("text/html", "<html>TeXPrinter keeps track of execution flow in order to ease the debugging process. When an exception is raised, all the debug messages are encoded in Base64 and then you have the possibility to send these messages to me. In case you decide to send me the debugging info, TeXPrinter will connect to the server and submit it. No sensible data or operating system variables are recorded.</html>");
        
        // format text as label
        UIUtils.formatEditorPaneAsLabel(lblText);
        
        // set default font
        UIUtils.setDefaultFontToEditorPane(lblText, true);

        // add icon
        mainBlock.add(lblScreenIcon, "cell 0 0 0 4, aligny top");
        
        // add title
        mainBlock.add(lblTitle, "cell 1 0, growx, aligny top");
        
        // add text
        mainBlock.add(lblText, "cell 1 1, growy, width 450, aligny top");
        
        // create footer
        JPanel footerBlock = new JPanel();
        
        // set layout
        footerBlock.setLayout(new MigLayout("ins dialog", "[][][][]", "[]"));
        
        // add separator
        footerBlock.add( new JSeparator(), "dock north");
        
        // add layout fix
        footerBlock.add(new JLabel(), "dock center" );
        
        // create close button
        JButton closeButton = new JButton("Close");
        
        // add listener
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
     * Shows the help window.
     */
    public void show() {
        
        // center it
        theDialog.setLocationRelativeTo(null);
        
        // make it visible
        theDialog.setVisible(true);
    }
    
}
