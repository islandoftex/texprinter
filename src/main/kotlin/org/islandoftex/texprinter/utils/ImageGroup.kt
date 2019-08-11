// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.utils

/**
 * Provides a POJO for handling images. This class holds some basic info about
 * images.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
class ImageGroup(
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
