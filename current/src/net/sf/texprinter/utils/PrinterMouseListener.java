/**
 * \cond LICENSE
 * ******************************************************************** This is
 * a conditional block for preventing the DoxyGen documentation tool to include
 * this license header within the description of each source code file. If you
 * want to include this block, please define the LICENSE parameter into the
 * provided DoxyFile.
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
 * ******************************************************************** \endcond
 *
 * PrinterMouseListener.java: This class implements a mouse listener for the
 * output format window. Last revision: paulo at temperantia 26 Feb 2012 05:29
 */

// package definition
package net.sf.texprinter.utils;

// needed imports
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;

/**
 * Implements a mouse listener for the output format window.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 2.1
 * @since 2.1
 */
public class PrinterMouseListener implements MouseListener {

    // the button
    private JButton button;
    
    // the icon
    private JLabel icon;
    
    // the pane
    private JEditorPane pane;
    
    // the marker
    private JLabel marker;
    
    // the title
    private JLabel title;
    
    // the color
    private Color color;
    
    // the image for ON
    private ImageIcon onImg;
    
    // the image for OFF
    private ImageIcon offImg;

    /**
     * Default constructor.
     *
     * @param button The button.
     * @param icon The icon.
     * @param pane The pane.
     * @param marker The marker.
     * @param title The title.
     * @param on The ON image.
     * @param off The OFF image.
     */
    public PrinterMouseListener(JButton button, JLabel icon, JEditorPane pane, JLabel marker, JLabel title, ImageIcon on, ImageIcon off) {

        // set everything
        this.button = button;
        this.icon = icon;
        this.pane = pane;
        this.marker = marker;
        this.title = title;
        this.onImg = on;
        this.offImg = off;
        color = title.getForeground();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // if it comes from the pane
        if (e.getSource() instanceof JEditorPane) {

            // do button click
            button.doClick();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        // set ON image
        icon.setIcon(onImg);

        // set marker
        marker.setVisible(true);

        // change color
        pane.setDisabledTextColor(Color.BLACK);

        // set title color
        title.setForeground(color);

    }

    @Override
    public void mouseExited(MouseEvent e) {

        // set OFF image
        icon.setIcon(offImg);

        // disable marker
        marker.setVisible(false);

        // change color
        pane.setDisabledTextColor(Color.GRAY);

        // set title color
        title.setForeground(Color.GRAY);
    }
}