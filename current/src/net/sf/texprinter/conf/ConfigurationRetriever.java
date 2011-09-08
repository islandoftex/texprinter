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
 * <b>ConfigurationRetriever.java</b>: This class retrieves the application
 * properties.
 */

// package definition
package net.sf.texprinter.conf;

// needed imports
import java.io.InputStream;
import java.util.Properties;

/**
 * Retrieves the application properties.
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.1
 */
public class ConfigurationRetriever {
    
    // the properties
    private Properties properties;

    /**
     * Constructor method.
     */
    public ConfigurationRetriever() {
        
        // create a new properties object
        properties = new Properties();
        
        // lets try to load the configuration
        try {
            
            // get the configuration file
            InputStream inStream = getClass().getResourceAsStream("/net/sf/texprinter/conf/texprinter.properties");
            
            // load it
            properties.load(inStream);
            
            // close the stream
            inStream.close();
        }
        catch (Exception e) {
            // something bad happened
            
            // set a dummy app version number
            properties.setProperty("AppVersionNumber","0.0");
            
            // and a dummy app version name
            properties.setProperty("AppVersionName", "Lazy developer");
        }
        
    }
    
    /**
     * Gets the version number.
     * @return The version number.
     */
    public String getAppVersionNumber() {
        
        // return the property
        return properties.getProperty("AppVersionNumber");
    }
    
    /**
     * Gets the version name.
     * @return The version name.
     */
    public String getAppVersionName() {
        
        // return the property
        return properties.getProperty("AppVersionName");
    }
    
}
