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
 * AboutPanel.java: This class provides the panel for changelog and updates.
 */

// package definition
package net.sf.texprinter.ui;

// needed imports
import com.ezware.dialog.task.IContentDesign;
import javax.swing.UIManager;
import net.sf.texprinter.utils.Dialogs;
import net.sf.texprinter.utils.UIUtils;
import net.sf.texprinter.utils.VersionChecker;

/**
 * Provides the panel for changelog and updates.
 * @author Paulo Roberto Massa Cereda
 * @version 2.0
 * @since 2.0
 */
public class AboutPanel extends javax.swing.JPanel {

    // the version checker
    private VersionChecker versionChecker;
    
    /**
     * Constructor method.
     */
    public AboutPanel() {
        
        // create the panel components
        initComponents();
        
        // set the button font
        btnChangelog.setFont(UIManager.getFont(IContentDesign.FONT_TEXT));
        
        // convert the changelog button to a label
        UIUtils.convertToLabel(btnChangelog);
        
        // set the label font
        lblVersionChecker.setFont(UIManager.getFont(IContentDesign.FONT_TEXT));
        
        // create a new version checker object
        versionChecker = new VersionChecker(lblVersionChecker);
        
        // revalidate
        this.revalidate();
        
        // and start it
        versionChecker.start();
    }
    
    /**
     * Stops the version checker thread.
     */
    public void stop() {
        
        // stops the version checker thread.
        versionChecker.interrupt();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnChangelog = new javax.swing.JButton();
        lblVersionChecker = new javax.swing.JLabel();

        setOpaque(false);

        btnChangelog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/texprinter/ui/images/changelog.png"))); // NOI18N
        btnChangelog.setText("View changelog");
        btnChangelog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangelogActionPerformed(evt);
            }
        });

        lblVersionChecker.setText("Version checker.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnChangelog)
                .addGap(56, 56, 56)
                .addComponent(lblVersionChecker)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangelog)
                    .addComponent(lblVersionChecker))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Displays the changelog window.
     * @param evt The event.
     */
    private void btnChangelogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangelogActionPerformed
        Dialogs.showChangelog(null);
    }//GEN-LAST:event_btnChangelogActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangelog;
    private javax.swing.JLabel lblVersionChecker;
    // End of variables declaration//GEN-END:variables
    
}
