/**
 * \cond LICENSE
 * ******************************************************************** This is
 * a conditional block for preventing the DoxyGen documentation tool to include
 * this license header within the description of each source code file. If you
 * want to include this block, please define the LICENSE parameter into the
 * provided DoxyFile.
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
 * ******************************************************************** \endcond
 *
 * ProgressMessage.java: This class adds a progress message to all methods that
 * might take time to run. Last revision: paulo at temperantia 26 Feb 2012 14:06
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import javax.swing.SwingUtilities;
import net.sf.texprinter.ui.WaitWindow;

/**
 * Adds a progress message to all methods that might take time to run.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class ProgressMessage {

    // the wait window
    private WaitWindow theWaitWindow;
    
    // the message
    private String theMessage;

    /**
     * Default constructor.
     * @param message The message to be displayed.
     */
    public ProgressMessage(String message) {

        // instantiate the object
        theMessage = message;

        // lets try
        try {

            // invoke later
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    
                    // create new wait window
                    theWaitWindow = new WaitWindow(theMessage);
                    
                    // and show
                    theWaitWindow.show();
                }
            });
        } catch (Exception e) {
            
            // do nothing
        }

    }

    /**
     * Interrupts the window display.
     */
    public void interrupt() {

        // lets try to hide the window
        try {
            
            // hide the window
            theWaitWindow.hide();
            
        } catch (Exception e) {
            
            // do nothing
        }

    }

}
