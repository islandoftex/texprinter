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
 * Downloader.java: This class provides download features for other helper
 * classes.
 * Last revision: paulo at temperantia 26 Feb 2012 05:45
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides download features for other helper classes.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 1.0
 */
public class Downloader {

    // the application logger
    private static final Logger log = Logger.getLogger(Downloader.class.getCanonicalName());
    
    // define the file size
    final static int size = 1024;

    /**
     * Download the file from the URL.
     *
     * @param resourceURL The resource URL.
     * @param fileName The file name.
     */
    public static void download(String resourceURL, String fileName) {

        // set the output stream to null
        OutputStream outputStream = null;

        // set the URL connection to null
        URLConnection connection = null;

        // set the input stream to null
        InputStream inputStream = null;

        // log message
        log.log(Level.INFO, "Trying to download the file {0}", fileName);

        // lets try
        try {

            // create a URL
            URL theURL;

            // and create a buffer
            byte[] buffer;

            // create a reader of bytes
            int bytesRead;

            // set the URL
            theURL = new URL(resourceURL);

            // create the output stream
            outputStream = new BufferedOutputStream(new FileOutputStream(fileName));

            // open the connection
            connection = theURL.openConnection();

            // get the input stream
            inputStream = connection.getInputStream();

            // set the new buffer
            buffer = new byte[size];

            // while there are bytes to read
            while ((bytesRead = inputStream.read(buffer)) != -1) {

                // read and write them to the output stream
                outputStream.write(buffer, 0, bytesRead);
            }

            // log message
            log.log(Level.INFO, "File {0} downloaded successfully.", fileName);

        } catch (Exception e) {

            // log message
            log.log(Level.SEVERE, "A generic error happened during the file download. MESSAGE: {0}", StringUtils.printStackTrace(e));

        } finally {

            // lets try to close the streams
            try {

                // close the input stream
                inputStream.close();

                // close the output stream
                outputStream.close();

            } catch (IOException e) {

                // log message
                log.log(Level.SEVERE, "A IO error happened during the file download. MESSAGE: {0}", StringUtils.printStackTrace(e));
            }
        }
    }
}