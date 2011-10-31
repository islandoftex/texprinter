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
 * Reporter.java: This class provides the SOAP architecture to send the
 * execution plan to the application bugtracker webservice.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import net.sf.texprinter.config.Configuration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Provides the SOAP architecture to send the execution plan to the
 * application bugtracker webservice.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 2.0
 */
public class Reporter extends Thread {

    // the form label
    private JLabel label;
    
    // the message
    private String message;

    /**
     * Constructor method.
     * @param label The label.
     * @param message The message.
     */
    public Reporter(JLabel label, String message) {

        // set the label
        this.label = label;

        // set the message
        this.message = message;
    }

    /**
     * Sends the execution plan to the application bugtracker webservice.
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

            // create the SOAP connection factory
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();

            // create the connection
            SOAPConnection conn = scf.createConnection();

            // create a message factory
            MessageFactory mf = MessageFactory.newInstance();

            // then create a message
            SOAPMessage msg = mf.createMessage();

            // get the SOAP part
            SOAPPart sp = msg.getSOAPPart();

            // get the envelope
            SOAPEnvelope env = sp.getEnvelope();

            // add the namespaces
            env.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
            env.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            env.addNamespaceDeclaration("enc", "http://schemas.xmlsoap.org/soap/encoding/");
            env.addNamespaceDeclaration("env", "http://schemas.xmlsoap.org/soap/envelop/");

            // set encoding
            env.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

            // get body
            SOAPBody bd = env.getBody();

            // create a new element
            SOAPElement be = bd.addChildElement(env.createName(config.getAppBugTrackerMethod(), "m", config.getAppBugTrackerWebService()));

            // add the message to the body
            be.addChildElement("message").addTextNode(message).setAttribute("xsi:type", "xsd:string");

            // save it
            msg.saveChanges();

            // call it
            SOAPMessage rp = conn.call(msg, config.getAppBugTrackerWebService());

            // get the content
            Source sc = rp.getSOAPPart().getContent();

            // create a new result
            StreamResult res = new StreamResult();

            // create a output stream
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // set the result stream
            res.setOutputStream(out);

            // get a transformer
            Transformer trans = TransformerFactory.newInstance().newTransformer();

            // then transform
            trans.transform(sc, res);

            // create an input stream
            ByteArrayInputStream is = new ByteArrayInputStream(out.toByteArray());

            // close connection
            conn.close();

            // get the document builder factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // create a new document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // set the document from the input stream
            Document doc = db.parse(is);

            // normalize it
            doc.getDocumentElement().normalize();

            // get the result of the webservice
            String serviceReturn = (doc.getElementsByTagName("return")).item(0).getTextContent();

            // check if it is an OK message
            if (serviceReturn.equals("ok")) {

                // display success
                displaySuccessMessage();
            } else {

                // display error
                displayErrorMessage();
            }

        } catch (SOAPException se) {

            // display error
            displayErrorMessage();
            
        } catch (TransformerConfigurationException tce) {
            
            // display error
            displayErrorMessage();
            
        } catch (TransformerException te) {
            
            // display error
            displayErrorMessage();
            
        } catch (ParserConfigurationException pce) {
            
            // display error
            displayErrorMessage();
            
        } catch (SAXException se) {
            
            // display error
            displayErrorMessage();
            
        } catch (IOException ioe) {
            
            // display error
            displayErrorMessage();
            
        } catch (Exception e) {
            
            // display error
            displayErrorMessage();
        }
    }

    /**
     * Displays the success message.
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
     * Displays the error message.
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
