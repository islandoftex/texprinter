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
 * Reporter.java: This class provides the SOAP architecture to send the
 * execution plan to the application bugtracker webservice.
 * Last revision: paulo at temperantia 26 Feb 2012 05:25
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.sf.texprinter.config.Configuration;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/**
 * Provides the SOAP architecture to send the execution plan to the application
 * bugtracker webservice. This class will get the execution plan, connect to a
 * webservice an then submit it. I had to use Apache Axis in order to support
 * Java 5 series.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.0
 */
public class Reporter extends Thread {

    // the form label
    private JLabel label;
    
    // the message
    private String message;
    
    // the buttons
    private JButton sendButton;
    private JButton closeButton;

    /**
     * Default constructor. It simply sets all private fields.
     *
     * @param sendButton The send button.
     * @param closeButton The close button.
     * @param label The label.
     * @param message The message.
     */
    public Reporter(JButton sendButton, JButton closeButton, JLabel label, String message) {

        // set the label
        this.label = label;

        // set the message
        this.message = message;

        // set the buttons
        this.sendButton = sendButton;
        this.closeButton = closeButton;
    }

    /**
     * Sends the execution plan to the application bugtracker webservice. It
     * relies on Apache Axis to submit the execution plan to the bugtracker
     * webservice.
     */
    @Override
    public void run() {

        // lets try
        try {

            // load the configuration
            Configuration config = new Configuration();

            // get icon
            ImageIcon icon = UIUtils.createImageIcon(this.getClass(), "/net/sf/texprinter/ui/images/wait.gif", "Please wait");

            // set icon
            label.setIcon(icon);

            // set text
            label.setText("Sending error report...");

            // create and configure a call to the service
            Call call = (Call) new Service().createCall();
            
            // set the address
            call.setTargetEndpointAddress(config.getAppBugTrackerWebService());
            
            // set the method to be called
            call.setOperationName(config.getAppBugTrackerMethod());

            // add the parameters to be sent
            Object[] param = new Object[]{message};
            
            // call it and get the return
            String serviceReturn = (String) call.invoke(param);

            // check if it is an OK message
            if (serviceReturn.equals("ok")) {

                // display success
                displaySuccessMessage();
                
            } else {

                // display error
                displayErrorMessage();
            }

        } catch (Exception e) {

            // display error
            displayErrorMessage();
        }

        // set the send button to be invisible
        sendButton.setVisible(false);
        
        // replace the text
        closeButton.setText("Close");
        
        // and enable it again
        closeButton.setEnabled(true);

    }

    /**
     * Displays the success message. Plain and simple, the JLabel will
     * provide a visual feedback on what's going on.
     */
    private void displaySuccessMessage() {

        // create an icon
        ImageIcon icon;

        // get icon
        icon = UIUtils.createImageIcon(this.getClass(), "/net/sf/texprinter/ui/images/ok.png", "Success");

        // set icon
        label.setIcon(icon);

        // set text
        label.setText("Error report sent, thank you.");
    }

    /**
     * Displays the error message. In case of any error, a generic message
     * will be provided.
     */
    private void displayErrorMessage() {

        // create and get the icon
        ImageIcon icon = UIUtils.createImageIcon(this.getClass(), "/net/sf/texprinter/ui/images/error.png", "Error");

        // set the icon
        label.setIcon(icon);

        // set the text
        label.setText("An error occurred.");
    }
}
