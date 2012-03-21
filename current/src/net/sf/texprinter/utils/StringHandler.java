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
 * StringHandler.java: This class provides a string handler for the Logging API.
 * Last revision: paulo at temperantia 26 Feb 2012 05:17
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Provides a string handler for the Logging API. Since the Logging API has no
 * handler that effectively logs entries into an array of strings, I decided to
 * write one.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.0
 */
public class StringHandler extends Handler {

    // the execution plan
    private static final ExecutionLogging execPlan = ExecutionLogging.getInstance();

    /**
     * Default constructor. It just sets the default formatter for this class.
     */
    public StringHandler() {

        // set formatter
        setFormatter(new SimpleFormatter());
    }

    /**
     * Publishes the record. This method is responsible for the entry
     * publication.
     *
     * @param record The record. It's handled by a higher level class.
     */
    @Override
    public void publish(LogRecord record) {

        // add the record to the execution plan
        execPlan.add(getFormatter().format(record));
    }

    /**
     * Flush method. Nothing to do, but it's required to be overriden, since
     * we are extending the Handler class.
     */
    @Override
    public void flush() {
    }

    /**
     * Close method. Again, nothing to do, but required to be overriden.
     *
     * @throws SecurityException A security exception is thrown in case
     * of error.
     */
    @Override
    public void close() throws SecurityException {
    }
}
