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
 * ExceptionPanel.java: This class provides the panel for exception
 * transmission.
 */

// package definition
package net.sf.texprinter.ui;

// needed imports
import com.ezware.dialog.task.IContentDesign;
import javax.swing.UIManager;
import net.sf.texprinter.utils.ExecutionLogging;
import net.sf.texprinter.utils.Reporter;

/**
 * Provides the panel for exception transmission.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 2.0
 */
public class ExceptionPanel extends javax.swing.JPanel {

    // the reporter
    private Reporter reporter;
    
    // define the execution logging
    private static ExecutionLogging logging = ExecutionLogging.getInstance();

    /**
     * Constructor method.
     */
    public ExceptionPanel() {
        
        // init components
        initComponents();
        
        // set the button font
        btnSend.setFont(UIManager.getFont(IContentDesign.FONT_TEXT));
        
        // set the label font
        lblMessage.setFont(UIManager.getFont(IContentDesign.FONT_TEXT));
        
        // update everything
        this.revalidate();
        
        // set the component focus
        setFocusable(true);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSend = new javax.swing.JButton();
        panelMessage = new javax.swing.JPanel();
        lblMessage = new javax.swing.JLabel();

        setOpaque(false);

        btnSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/texprinter/ui/images/report.png"))); // NOI18N
        btnSend.setText("Send it!");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        panelMessage.setOpaque(false);

        lblMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/texprinter/ui/images/info.png"))); // NOI18N
        lblMessage.setText("Could you send me an error report?");

        javax.swing.GroupLayout panelMessageLayout = new javax.swing.GroupLayout(panelMessage);
        panelMessage.setLayout(panelMessageLayout);
        panelMessageLayout.setHorizontalGroup(
            panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 265, Short.MAX_VALUE)
            .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelMessageLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)))
        );
        panelMessageLayout.setVerticalGroup(
            panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
            .addGroup(panelMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelMessageLayout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(lblMessage)
                    .addContainerGap(16, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSend)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSend))
            .addComponent(panelMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Sends the execution report.
     * @param evt The event.
     */
    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        
        // create a new reporter and send the message
        reporter = new Reporter(lblMessage, logging.dump());
        
        // set the button to disabled
        btnSend.setEnabled(false);
        
        // start the sending process
        reporter.start();
        
    }//GEN-LAST:event_btnSendActionPerformed

    /**
     * Stops the sending process.
     */
    public void stop() {
        
        // stops the reporter
        reporter.interrupt();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JPanel panelMessage;
    // End of variables declaration//GEN-END:variables
}
