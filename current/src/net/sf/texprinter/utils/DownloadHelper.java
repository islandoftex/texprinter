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
 * <b>DownloadHelper.java</b>: This class provides download features for
 * other helper classes.
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Provides download features for other helper classes.
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class DownloadHelper {

    // define the file size
    final static int size = 1024;

    /**
     * Download the file from the URL.
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

        } catch (Exception e) {
            // something bad happened, but we won't do nothing
        } finally {
            
            // lets try to close the streams
            try {
                
                // close the input stream
                inputStream.close();

                // close the output stream
                outputStream.close();
                                
            } catch (IOException e) {
                // something bad happened, but we won't do nothing
            }
        }
    }
}