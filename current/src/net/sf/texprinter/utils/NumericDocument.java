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
 * NumericDocument.java: This class represents a document format which only
 * accepts numbers.
 * Last revision: paulo at temperantia 26 Feb 2012 05:32
 */

// package definition
package net.sf.texprinter.utils;

//Import packages
import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Represents a document format which only accepts numbers. It's used by the
 * main textfield to only accept numbers.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class NumericDocument extends PlainDocument {

    /**
     * Constructor. Just instantiate the upper class.
     */
    public NumericDocument() {

        // the upper class
        super();
    }

    /**
     * Inserts the string into the component. It evaluates if the string is
     * valid; if true, inserts it.
     * 
     * @param offset The offset.
     * @param str The string.
     * @param attr The attributes set.
     * @throws BadLocationException An exception is thrown in case of some
     * error.
     */
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

        // check if string is not null
        if (str != null) {

            // if the string contains something that is not a number
            if (hasOnlyNumbers(str) == false) {

                // play a beep
                Toolkit.getDefaultToolkit().beep();

                // and return
                return;
            }

            // everything is ok, insert
            super.insertString(offset, str, attr);
        }
    }

    /**
     * Checks if the string has only numbers. This method implementation
     * relies on an exception checking.
     *
     * @param str The string.
     * @return A boolean value.
     */
    public static boolean hasOnlyNumbers(String str) {
        
        // lets try
        try {
            
            // we try to convert the string
            int value = Integer.parseInt(str);
            
            // if it's ok, return true
            return true;
            
        } catch (NumberFormatException exception) {
            
            // there was an error, so it's not a number
            return false;
        }
    }
}