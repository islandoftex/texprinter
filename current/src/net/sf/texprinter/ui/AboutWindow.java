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
 * AboutWindow.java: This class provides the dialog with info
 * about the application.
 * Last revision: paulo at temperantia 26 Feb 2012 06:22
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
import net.sf.texprinter.config.Configuration;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.UIUtils;
import net.sf.texprinter.utils.VersionChecker;

/**
 * Provides the dialog with info about the application.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class AboutWindow {

    // the dialog
    private JDialog theDialog;
    
    // the copyright symbol
    private final String COPYRIGHT = "\u00a9";
    
    // the version checker
    private VersionChecker versionChecker;
    
    // the version checker label
    private JLabel versionLabel;

    /**
     * Default constructor.
     */
    public AboutWindow() {

        // create a new dialog
        theDialog = new JDialog();
        
        // add title
        theDialog.setTitle("TeXPrinter");
        
        // define the minimum size
        theDialog.setMinimumSize(new Dimension(300, 150));
        
        // disable resize
        theDialog.setResizable(false);
        
        // set modal
        theDialog.setModal(true);
        
        // add a window listener
        theDialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                
                // if there's a valid version checker object
                if (versionChecker != null) {
                    
                    // stop before disposing window
                    versionChecker.interrupt();
                }
                
                // then dispose
                theDialog.dispose();
            }
        });

        // set a new layout
        theDialog.setLayout(new MigLayout());

        // create a new panel with the main content
        JPanel mainBlock = new JPanel(new MigLayout("ins dialog, gapx 7, hidemode 3", "[][grow]", "[][]10[]"));
        
        // set the background to white
        mainBlock.setBackground(Color.WHITE);

        // create a new icon
        JLabel lblScreenIcon = new JLabel(new ImageIcon(AboutWindow.class.getResource("/net/sf/texprinter/ui/images/informationicon.png")));

        // create a title
        JLabel lblTitle = new JLabel("<html>TeXPrinter</html>");
        
        // and format it
        UIUtils.formatLabelAsTitle(lblTitle);

        // create a new configuration
        Configuration config = new Configuration();

        // create a new text
        JEditorPane lblText = new JEditorPane("text/html", "<html><i>Version " + config.getAppVersionNumber() + " - " + config.getAppVersionName() + "</i><br><br>Copyright " + COPYRIGHT + " 2012, Paulo Roberto Massa Cereda<br>All rights reserved.<br><br>This application is licensed under the <u>New BSD License</u>. I want to call your attention to the fact that the <i>New BSD License</i> has been verified as a <i>GPL-compatible free software license</i> by the Free Software Foundation, and has been vetted as an <i>open source license</i> by the Open Source Initiative.</html>");
        
        // format it as a label
        UIUtils.formatEditorPaneAsLabel(lblText);
        
        // set the default font
        UIUtils.setDefaultFontToEditorPane(lblText, true);

        // add icon
        mainBlock.add(lblScreenIcon, "cell 0 0 0 4, aligny top");
        
        // add title
        mainBlock.add(lblTitle, "cell 1 0, growx, aligny top");
        
        // add text
        mainBlock.add(lblText, "cell 1 1, growy, width 500, aligny top");

        // create a footer panel
        JPanel footerBlock = new JPanel();

        // create a new layout
        footerBlock.setLayout(new MigLayout("ins dialog", "[][][][]", "[]"));
        
        // add a separator
        footerBlock.add(new JSeparator(), "dock north");

        // create a new changelog button
        JButton changelogButton = new JButton("View changelog", new ImageIcon(AboutWindow.class.getResource("images/changelog.png")));
        
        // format it as a label
        UIUtils.formatButtonAsLabel(changelogButton);

        // add a listener
        changelogButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // simply display the changelog
                Dialogs.showChangelogWindow();
            }
        });
        
        // disable the button focus
        changelogButton.setFocusable(false);
        
        // add the button
        footerBlock.add(changelogButton);

        // create the version label
        versionLabel = new JLabel("Version checker.");
        
        // add it
        footerBlock.add(versionLabel, "gap 40");

        // add a fix to the layout
        footerBlock.add(new JLabel(), "dock center");

        // create a new close button
        JButton closeButton = new JButton("Close");
        
        // add a listener
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // if there's a valid version checker
                if (versionChecker != null) {
                    
                    // stop it
                    versionChecker.interrupt();
                }
                
                // make the dialog invisible
                theDialog.setVisible(false);
                
                // then dispose
                theDialog.dispose();
            }
        });

        // disable the button focus
        closeButton.setFocusable(false);
        
        // add the button
        footerBlock.add(closeButton);

        // add the main block
        theDialog.add(mainBlock, "dock center");
        
        // add the footer
        theDialog.add(footerBlock, "dock south");

        // pack
        theDialog.pack();

    }

    /**
     * Shows the window.
     */
    public void show() {
        
        // center dialog
        theDialog.setLocationRelativeTo(null);

        // create a new version checker
        versionChecker = new VersionChecker(versionLabel);
        
        // start it
        versionChecker.start();

        // set the dialog visible
        theDialog.setVisible(true);
    }
}
