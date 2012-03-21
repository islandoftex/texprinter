/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.texprinter.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.PrinterMouseListener;
import net.sf.texprinter.utils.PrintingFormat;
import net.sf.texprinter.utils.UIUtils;

/**
 *
 * @author Paulo
 */
public class NewPrintingWindow {
    
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
    public NewPrintingWindow() {
        
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
        JLabel lblScreenIcon = new JLabel(new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/printericon.png")));
        
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
        markerPDF = new JLabel(new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/checkicon.png")));
        
        // create the PDF button
        buttonPDF = new JButton();
        
        // set layout
        buttonPDF.setLayout(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][grow]", "[]20[grow]10[]"));
        
        // disable focus
        buttonPDF.setFocusable(false);
        
        // create PDF icon
        JLabel iconPDF = new JLabel(new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/ipdfoff.png")));
        
        // add icon to button
        buttonPDF.add(iconPDF, "cell 0 0 0 4, aligny top");
        
        // create PDF title
        JLabel titlePDF = new JLabel("<html>PDF output</html>");
        
        // format as title
        UIUtils.formatLabelAsTitle(titlePDF);
        
        // add title to button
        buttonPDF.add(titlePDF, "cell 1 0, growx, aligny top");
        
        // add listener
        buttonPDF.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // set the result
                theResult = PrintingFormat.PDF;
                
                // make it invisible
                theDialog.setVisible(false);
            }
        });
        
        // create the text
        JEditorPane textPDF = new JEditorPane("text/html", "<html>Generate a PDF output from the provided question ID. All resources (e.g. images) will be embedded in the resulting PDF file. The document will be ready for viewing and printing.</html>");
        
        // format as label
        UIUtils.formatEditorPaneAsLabel(textPDF);
        
        // set default font
        UIUtils.setDefaultFontToEditorPane(textPDF, true);
        
        // add to button
        buttonPDF.add(textPDF, "cell 1 1, growy, width 400!, aligny top");
        
        // format button
        UIUtils.formatButtonAsLabel(buttonPDF);
        
        // images
        ImageIcon onPDF = new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/ipdf.png"));
        ImageIcon offPDF = new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/ipdfoff.png"));
        
        // add mouse listeners
        buttonPDF.addMouseListener(new PrinterMouseListener(buttonPDF, iconPDF, textPDF, markerPDF, titlePDF, onPDF, offPDF));
        textPDF.addMouseListener(new PrinterMouseListener(buttonPDF, iconPDF, textPDF, markerPDF, titlePDF, onPDF, offPDF));
        
        // set the title foreground
        titlePDF.setForeground(Color.GRAY);
    
        // add PDF
        //mainBlock.add(buttonPDF, "cell 1 2, height 100, growx");
        mainBlock.add(buttonPDF, "cell 1 2, growx");
        
        // add PDF marker
        mainBlock.add(markerPDF, "cell 0 2");
        
        // marker is invisible
        markerPDF.setVisible(false);
        
        // create the TeX marker
        markerTeX = new JLabel(new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/checkicon.png")));
        
        // create the TeX button
        buttonTeX = new JButton();
        
        // set layout
        buttonTeX.setLayout(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][grow]", "[]20[grow]10[]"));
        
        // disable focus
        buttonTeX.setFocusable(false);
        
        // create TeX icon
        JLabel iconTeX = new JLabel(new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/itexoff.png")));
        
        // add icon to button
        buttonTeX.add(iconTeX, "cell 0 0 0 4, aligny top");
        
        // create TeX title
        JLabel titleTeX = new JLabel("<html>TeX output</html>");
        
        // format as title
        UIUtils.formatLabelAsTitle(titleTeX);
        
        // add title to button
        buttonTeX.add(titleTeX, "cell 1 0, growx, aligny top");
        
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
        
        // create the text
        JEditorPane textTeX = new JEditorPane("text/html", "<html>Generate a source TeX output from the provided question ID. All resources (e.g. images) will be downloaded to the current directory. You'll need to compile the TeX document.</html>");
        
        // format as label
        UIUtils.formatEditorPaneAsLabel(textTeX);
        
        // set default font
        UIUtils.setDefaultFontToEditorPane(textTeX, true);
        
        // add to button
        buttonTeX.add(textTeX, "cell 1 1, growy, width 400!, aligny top");
        
        // format button
        UIUtils.formatButtonAsLabel(buttonTeX);
        
        // images
        ImageIcon onTeX = new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/itex.png"));
        ImageIcon offTeX = new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/itexoff.png"));
        
        // add mouse listeners
        buttonTeX.addMouseListener(new PrinterMouseListener(buttonTeX, iconTeX, textTeX, markerTeX, titleTeX, onTeX, offTeX));
        textTeX.addMouseListener(new PrinterMouseListener(buttonTeX, iconTeX, textTeX, markerTeX, titleTeX, onTeX, offTeX));
        
        // set the title foreground
        titleTeX.setForeground(Color.GRAY);
        
        // add the button
        //mainBlock.add(buttonTeX, "cell 1 3, height 100, growx");
        mainBlock.add(buttonTeX, "cell 1 3, growx");
        
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
        JButton helpButton = new JButton(new ImageIcon(NewPrintingWindow.class.getResource("/net/sf/texprinter/ui/images/tinyhelp.png")));
        
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
