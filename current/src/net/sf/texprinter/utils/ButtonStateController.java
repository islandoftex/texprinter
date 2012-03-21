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
 * ButtonStateController.java: This class provides state controller features
 * for buttons.
 * Last revision: paulo at temperantia 26 Feb 2012 05:51
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Provides state controller features for buttons.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class ButtonStateController implements DocumentListener {

    // the button
    private JButton button;

    
    /**
     * Default constructor.
     * @param b The button.
     */
    public ButtonStateController(JButton b) {
        
        // set the value
        button = b;
    }

    /**
     * Validation on update. Overriden method.
     * 
     * @param e The event.
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        
        // validate
        disableIfEmpty(e);
    }

    /**
     * Validation on insertion. Overriden method.
     * 
     * @param e The event.
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        
        // validate
        disableIfEmpty(e);
    }

    /**
     * Validation on removal. Overriden method.
     * 
     * @param e The event.
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        
        // validate
        disableIfEmpty(e);
    }

    /**
     * Validation method. It disables the button in case of an
     * empty document.
     * 
     * @param e The event.
     */
    public void disableIfEmpty(DocumentEvent e) {
        
        // disable or enable the button according to the
        // document lenght
        button.setEnabled(e.getDocument().getLength() > 0);
    }
}