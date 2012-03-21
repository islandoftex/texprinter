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
 * OutputController.java: This class provides the output controller for command
 * line usage.
 * Last revision: paulo at temperantia 26 Feb 2012 05:32
 */
// package definition
package net.sf.texprinter.utils;

/**
 * Provides the output controller for command line usage. This class will
 * help us to keep track of which execution we should have.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.0
 */
public class OutputController {

    // command line mode
    private boolean commandLineMode;
    
    // singleton reference
    private static OutputController selfRef;

    /**
     * Constructs the singleton instance.
     */
    private OutputController() {

        // set self
        selfRef = this;

        // set the flag to false
        commandLineMode = false;
    }

    /**
     * Provides reference to singleton object of OutputController.
     *
     * @return The singleton instance.
     */
    public static final OutputController getInstance() {

        // if it is not defined
        if (selfRef == null) {

            // call the private constructor
            selfRef = new OutputController();
        }

        // return the reference
        return selfRef;
    }

    /**
     * Checks if the application is in command line mode.
     *
     * @return A boolean indicating command line mode or not.
     */
    public boolean isCommandLineMode() {

        // return the flag
        return commandLineMode;
    }

    /**
     * Setter for the command line mode flag.
     *
     * @param commandLineMode The command line mode flag.
     */
    public void setCommandLineMode(boolean commandLineMode) {

        // set the flag
        this.commandLineMode = commandLineMode;
    }
}
