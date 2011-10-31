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
 * ExecutionLogging.java: This class logs the execution plan.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.util.ArrayList;
import org.apache.commons.codec.binary.Base64;

/**
 * Logs the execution plan.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 2.0
 */
public class ExecutionLogging {
    
    // singleton reference of ExecutionLogging.
    private static ExecutionLogging selfRef;
    
    // the execution plan
    private ArrayList<String> executionPlan;

    /**
     * Constructs singleton instance of ExecutionLogging.
     */
    private ExecutionLogging() {
        
        // set the reference
        selfRef = this;
        
        // create the array
        executionPlan = new ArrayList<String>();
    }

    /**
     * Provides reference to singleton object of ExecutionLogging.
     * @return The singleton instance.
     */
    public static final ExecutionLogging getInstance() {
        
        // if null
        if (selfRef == null) {
            
            // create it
            selfRef = new ExecutionLogging();
        }
        
        // return it
        return selfRef;
    }
    
    /**
     * Add message to the list.
     * @param message The message.
     */
    public void add(String message) {
        
        // add the message to the array
        executionPlan.add(message);
    }
    
    /**
     * Dumps the execution plan.
     * @return A Base64 URL safe string.
     */
    public String dump() {
        
        // encode the execution plan as a URL safe string and return it
        return Base64.encodeBase64URLSafeString(executionPlan.toString().getBytes());

    }
    
}
