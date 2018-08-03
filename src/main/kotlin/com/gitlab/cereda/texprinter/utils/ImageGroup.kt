/******************************************************************************
 * Copyright 2012-2018 Paulo Roberto Massa Cereda                             *
 *                                                                            *
 * Redistribution and use in source and binary forms, with or                 *
 *  without modification, are permitted provided that the following           *
 *  conditions are met:                                                       *
 *                                                                            *
 * 1. Redistributions of source code must retain the above copyright          *
 * notice, this list of conditions and the following disclaimer.              *
 *                                                                            *
 * 2. Redistributions in binary form must reproduce the above copyright       *
 * notice, this list of conditions and the following disclaimer in the        *
 * documentation and/or other materials provided with the distribution.       *
 *                                                                            *
 * 3. Neither the name of the copyright holder nor the names of it            *
 *  contributors may be used to endorse or promote products derived           *
 *  from this software without specific prior written permission.             *
 *                                                                            *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS       *
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT         *
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS         *
 *  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE            *
 *  COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,      *
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,      *
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS     *
 *  OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND    *
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR     *
 *  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE    *
 *  USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *
 ******************************************************************************/
package com.gitlab.cereda.texprinter.utils

/**
 * Provides a POJO for handling images. This class holds some basic info about
 * images.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
class ImageGroup
/**
 * Default constructor. It simply sets some parameters.
 *
 * @param imgURL The image URL.
 */
(
    /**
     * @property url The url associated with the img tag.
     */
    val url: String,
    /**
     * @property altText The alternative text associated with the img tag which
     *  will be used as caption.
     */
    var altText: String = "") {

  /**
   * @property name The name of the image (file name with extension).
   */
  var name: String = getNameFromLink(url)

  /**
   * Gets the image name from the image URL. This method tries to retrieve
   * the image name from the URL.
   *
   * @param link The image URL.
   * @return The image name.
   */
  private fun getNameFromLink(link: String): String {
    return link.substringAfterLast("/")
  }
}
