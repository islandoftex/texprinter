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
 * PrintingWindow.java: This class provides the dialog with the output
 * formats.
 * Last revision: paulo at temperantia 26 Feb 2012 07:23
 */

// package definition
package net.sf.texprinter.ui;

// needed imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.PrintingFormat;
import net.sf.texprinter.utils.UIUtils;


/**
 * Provides the dialog with the output formats.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class PrintingWindow {
    
    // the dialog
    private JDialog theDialog;
    
    // the result
    private PrintingFormat theResult;
    
    // the PDF button
    private JButton buttonPDF;
    
    // the TeX button
    private JButton buttonTeX;
    
    // the PDF marker
    private JLabel markerPDF;
    
    // the TeX marker
    private JLabel markerTeX;
    
    /**
     * Default constructor.
     */
    public PrintingWindow() {
        
        // set the default value
        theResult = PrintingFormat.NONE;
        
        // create a new dialog
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
                
                // dispose it
                theDialog.dispose();
                
                // invalidate the result
                theResult = PrintingFormat.NONE;
            }
        });

        // set a new layout
        theDialog.setLayout(new MigLayout());
        
        // create a main block
        JPanel mainBlock = new JPanel(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][grow]", "[][]20[grow]20[grow]10[]"));
        
        // set background to white
        mainBlock.setBackground(Color.WHITE);
        
        // create icon
        JLabel lblScreenIcon = new JLabel(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/printericon.png")));
        
        // create title
        JLabel lblTitle = new JLabel("<html>Output format</html>");
        
        // format title
        UIUtils.formatLabelAsTitle(lblTitle);
        
        // create text
        JEditorPane lblText = new JEditorPane("text/html", "<html>I'm ready to print the question you provided me. Please choose your option.</html>");
        
        // format text as label
        UIUtils.formatEditorPaneAsLabel(lblText);
        
        // set default font
        UIUtils.setDefaultFontToEditorPane(lblText, true);
       
        // add icon
        mainBlock.add(lblScreenIcon, "cell 0 0 0 4, aligny top");
        
        // add title
        mainBlock.add(lblTitle, "cell 1 0, growx, aligny top");
        
        // add text
        mainBlock.add(lblText, "cell 1 1, growx, aligny top");
        
        // create the PDF marker
        markerPDF = new JLabel(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/checkicon.png")));
        
        // create the PDF button
        buttonPDF = new JButton("<html><b>PDF Output</b><br>Generate a PDF output from the provided question ID. All resources (e.g. images) will be<br> embedded in the resulting PDF file. The document will be ready for viewing and printing.</html>");
        
        // disable focus
        buttonPDF.setFocusable(false);

        // set icon
        buttonPDF.setIcon(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/ipdfoff.png")));
        
        // set rollover icon
        buttonPDF.setRolloverIcon(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/ipdf.png")));
        
        // set pressed icon
        buttonPDF.setPressedIcon(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/ipdf.png")));
        
        // add a listener
        buttonPDF.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // define the result
                theResult = PrintingFormat.PDF;
                
                // make it invisble
                theDialog.setVisible(false);
            }
        });
        
        // format as list
        UIUtils.formatButtonAsList(buttonPDF);
        
        // set foreground color
        buttonPDF.setForeground(Color.GRAY);
        
        // add mouse listener
        buttonPDF.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
                // change foreground color
                buttonPDF.setForeground(Color.BLACK);
                
                // make the marker visible
                markerPDF.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
                // change foreground color
                buttonPDF.setForeground(Color.GRAY);
                
                // make the marker invisible
                markerPDF.setVisible(false);
            }
        });
        
        // add PDF button
        mainBlock.add(buttonPDF, "cell 1 2, height 100, growx");
        
        // add PDF marker
        mainBlock.add(markerPDF, "cell 0 2");
        
        // marker is invisible
        markerPDF.setVisible(false);
        
        // create the TeX marker
        markerTeX = new JLabel(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/checkicon.png")));
        
        // create the TeX button
        buttonTeX = new JButton("<html><b>TeX Output</b><br>Generate a source TeX output from the provided question ID. All resources (e.g. images)<br>will be downloaded to the current directory. You'll need to compile the TeX document.</html>");
        
        // disable focus
        buttonTeX.setFocusable(false);

        // set icon
        buttonTeX.setIcon(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/itexoff.png")));
        
        // set rollover icon
        buttonTeX.setRolloverIcon(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/itex.png")));
        
        // set pressed icon
        buttonTeX.setPressedIcon(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/itex.png")));
        
        // add listener
        buttonTeX.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // set the result
                theResult = PrintingFormat.TEX;
                
                // make it invisible
                theDialog.setVisible(false);
            }
        });
        
        // format as list
        UIUtils.formatButtonAsList(buttonTeX);
        
        // set foreground color
        buttonTeX.setForeground(Color.GRAY);
        
        // add a mouse listener
        buttonTeX.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
                // change foreground color
                buttonTeX.setForeground(Color.BLACK);
                
                // make marker visible
                markerTeX.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
                // change foreground color
                buttonTeX.setForeground(Color.GRAY);
                
                // make marker invisible
                markerTeX.setVisible(false);
            }
        });
        
        // add TeX button
        mainBlock.add(buttonTeX, "cell 1 3, height 100, growx");
        
        // add TeX marker
        mainBlock.add(markerTeX, "cell 0 3");
        
        // marker is invisible
        markerTeX.setVisible(false);
        
        // create a new footer
        JPanel footerBlock = new JPanel();
        
        // add new layout
        footerBlock.setLayout(new MigLayout("ins dialog", "[][][][]", "[]"));
        
        // add separator
        footerBlock.add( new JSeparator(), "dock north");
        
        // create a help button
        JButton helpButton = new JButton(new ImageIcon(PrintingWindow.class.getResource("/net/sf/texprinter/ui/images/tinyhelp.png")));
        
        // format as label
        UIUtils.formatButtonAsLabel(helpButton);
        
        // add listener
        helpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // show help
                Dialogs.showHelpFormatWindow();
            }
        });
        
        // disable focus
        helpButton.setFocusable(false);

        // add button
        footerBlock.add(helpButton);

        // add layout fix
        footerBlock.add(new JLabel(), "dock center" );
        
        // add main block
        theDialog.add(mainBlock, "dock center");
        
        // add footer
        theDialog.add(footerBlock, "dock south");
        
        // pack
        theDialog.pack();
        
    }
    
    /**
     * Shows the printing window.
     * 
     * @return The selected output format.
     */
    public PrintingFormat show() {
        
        // center it
        theDialog.setLocationRelativeTo(null);
        
        // make it visible
        theDialog.setVisible(true);
        
        // get the result
        PrintingFormat value = getResult();
        
        // dispose dialog
        theDialog.dispose();
        
        // return value
        return value;
        
    }
    
    /**
     * Getter for the output format.
     * 
     * @return The output format.
     */
    public PrintingFormat getResult() {
        
        // return the result
        return theResult;
    }

}
