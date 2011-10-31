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
 * VersionChecker.java: This class provides a version checker for TeXPrinter.
 */

// package definition
package net.sf.texprinter.utils;

// imports needed
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.texprinter.config.Configuration;
import org.w3c.dom.Document;

/**
 * Povides a version checker for TeXPrinter.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 2.0
 */
public class VersionChecker extends Thread {

    // the label
    private JLabel message;

    /**
     * Constructor method.
     * @param message 
     */
    public VersionChecker(JLabel message) {

        // set the label
        this.message = message;
    }

    /**
     * Checks for newer versions.
     */
    @Override
    public void run() {

        // display wait message
        displayWaitMessage();

        // create a new input stream
        InputStream is = null;

        // lets try
        try {

            // create a new configuration
            Configuration config = new Configuration();

            // create a new URL from config
            URL url = new URL(config.getAppVersionURL());

            // open stream
            is = url.openStream();

            // get the document builder factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // create a new document
            DocumentBuilder db = dbf.newDocumentBuilder();

            // parse the input stream
            Document doc = db.parse(is);

            // normalize it
            doc.getDocumentElement().normalize();

            // get the version number
            String versionNumber = (doc.getElementsByTagName("version")).item(0).getTextContent();

            // get the version name
            String versionName = (doc.getElementsByTagName("name")).item(0).getTextContent();

            // if it is different
            if ((!versionName.equals(config.getAppVersionName())) || (!versionNumber.equals(config.getAppVersionNumber()))) {

                // there is a new version available
                displayNewVersionMessage(versionNumber);
            } else {

                // current version is the latest
                displayLastVersionMessage();
            }
        } catch (MalformedURLException ex) {

            // display error
            displayErrorMessage();
            
        } catch (IOException ioe) {
            
            // display error
            displayErrorMessage();
            
        } catch (Exception e) {
            
            // display error
            displayErrorMessage();
            
        } finally {
            
            // lets try
            try {
                
                // close the input stream
                is.close();
                
            } catch (Exception h) {
                
                // do nothing
            }
        }

    }

    /**
     * Displays the waiting message.
     */
    private void displayWaitMessage() {
        
        // get icon
        ImageIcon icon = UIUtils.createImageIcon(this.getClass(), "/net/sf/texprinter/ui/images/wait.gif", "Please wait");
        
        // set icon
        message.setIcon(icon);
        
        // set text
        message.setText("Checking for updates...");
    }

    /**
     * Displays the new version.
     * @param versionNumber The new version number.
     */
    private void displayNewVersionMessage(String versionNumber) {
        
        // create the icon
        ImageIcon icon;
        
        // get the icon
        icon = UIUtils.createImageIcon(this.getClass(), "/net/sf/texprinter/ui/images/warning.png", "New version");
        
        // set icon
        message.setIcon(icon);
        
        // set message
        message.setText("A newer version is available: " + versionNumber);
    }

    /**
     * Displays the last version message.
     */
    private void displayLastVersionMessage() {
        
        // create icon
        ImageIcon icon;
        
        // get icon
        icon = UIUtils.createImageIcon(this.getClass(), "/net/sf/texprinter/ui/images/ok.png", "Last version");
        
        // set icon
        message.setIcon(icon);
        
        // set text
        message.setText("You have the last version.");
    }

    /**
     * Displays the error message.
     */
    private void displayErrorMessage() {
        
        // create icon
        ImageIcon icon;
        
        // get icon
        icon = UIUtils.createImageIcon(this.getClass(), "/net/sf/texprinter/ui/images/error.png", "Error");
        
        // set icon
        message.setIcon(icon);
        
        // set message
        message.setText("An error occurred, try again later.");
    }
}
