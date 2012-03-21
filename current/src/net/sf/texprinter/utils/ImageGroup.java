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
 * ImageGroup.java: This is a helper class, actually a POJO for handling images.
 * Last revision: paulo at temperantia 26 Feb 2012 05:41
 */

// package definition
package net.sf.texprinter.utils;

/**
 * Provides a POJO for handling images. This class holds some basic info about
 * images.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 1.0
 */
public class ImageGroup {

    // the image URL
    private String url;
    
    // the image name
    private String name;

    /**
     * Default constructor. It simply sets some parameters.
     *
     * @param imgURL The image URL.
     */
    public ImageGroup(String imgURL) {

        // set the image URL
        this.url = imgURL;

        // and set the image name
        this.name = getNameFromLink(imgURL);
    }

    /**
     * Getter for the image name.
     *
     * @return The image name.
     */
    public String getName() {

        // return the name
        return name;
    }

    /**
     * Setter for the image name.
     *
     * @param name The image name.
     */
    public void setName(String name) {

        // set the image name
        this.name = name;
    }

    /**
     * Getter for the image URL.
     *
     * @return The image URL.
     */
    public String getURL() {

        // return the image URL
        return url;
    }

    /**
     * Setter for the image URL.
     *
     * @param url The image URL.
     */
    public void setURL(String url) {

        // set the image URL
        this.url = url;
    }

    /**
     * Gets the image name from the image URL. This method tries to retrieve
     * the image name from the URL.
     *
     * @param link The image URL.
     * @return The image name.
     */
    private static String getNameFromLink(String link) {

        // split the image URL into several parts
        String[] parts = link.split("/");

        // get the last part
        String imgName = parts[parts.length - 1];

        // return the name
        return imgName;
    }
}
