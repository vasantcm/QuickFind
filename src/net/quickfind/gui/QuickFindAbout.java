/*
    QuickFind (http://quickfind.sourceforge.net/)
    Cross-platform Java application for searching files in your Computer.

    Copyright (c) 2010, 2012 Vasantkumar Mulage

    All rights reserved.

    Redistribution and use in source and binary forms, with or without modification,
    are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright notice,
          this list of conditions and the following disclaimer.
        * Redistributions in binary form must reproduce the above copyright notice,
          this list of conditions and the following disclaimer in the documentation
          and/or other materials provided with the distribution.
        * Neither the name of the QuickFind nor the names of its contributors
          may be used to endorse or promote products derived from this software without
          specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
    A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
    CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
    PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
    PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package net.quickfind.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.quickfind.config.PropertyPage;

/*
 * QuickFindAbout.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
public class QuickFindAbout extends javax.swing.JDialog {

    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(QuickFindAbout.class.getName());

    /*
     * Creates QuickFindAbout
     */
    public QuickFindAbout(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setUndecorated(true);
        initComponents();
        pack();
        this.setLocationRelativeTo(this);
        productNameLabel.grabFocus();
        authorNameLabel.setText(PropertyPage.COPY_RIGHT);
        productVersionValueLabel.setText(PropertyPage.PRODUCT_VERSION);
        homePageValueLabel.setText(PropertyPage.WEBSITE);
        donateIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.DONATE_ICON)));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        productIconLabel = new javax.swing.JLabel();
        productVersionLabel = new javax.swing.JLabel();
        productNameLabel = new javax.swing.JLabel();
        authorLabel = new javax.swing.JLabel();
        authorNameLabel = new javax.swing.JLabel();
        productVersionValueLabel = new javax.swing.JLabel();
        homePageLabel = new javax.swing.JLabel();
        sectionSeparator = new javax.swing.JSeparator();
        homePageValueLabel = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        productLicenseScrollPane = new javax.swing.JScrollPane();
        productLicenseEditorPane = new javax.swing.JEditorPane();
        donateIconLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setForeground(java.awt.Color.white);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        mainPanel.setBackground(java.awt.Color.white);
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        productIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertyPage.IMAGES_PATH +PropertyPage.SPLASH_ICON)));

        productVersionLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        productVersionLabel.setText("Product Version:");

        productNameLabel.setFont(new java.awt.Font("Times New Roman", 1, 18));
        productNameLabel.setText("QuickFind");

        authorLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        authorLabel.setText("Author:");

        authorNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        authorNameLabel.setText("Vasantkumar Mulage");

        productVersionValueLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        productVersionValueLabel.setText("1.0.0.0 BETA");

        homePageLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        homePageLabel.setText("Home Page:");

        homePageValueLabel.setText("<html><a href=\\\"http://quickfind.sf.net/\">http://quickfind.sf.net/</a></html>");
        homePageValueLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        homePageValueLabel.setName("http://quickfind.sf.net/"); // NOI18N
        homePageValueLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homePageValueLabelMouseClicked(evt);
            }
        });

        closeButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        productLicenseEditorPane.setContentType("text/html");
        productLicenseEditorPane.setEditable(false);
        productLicenseEditorPane.setText("<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p>\n<font size='4'>\n\tQuickFind (<a href='http://quickfind.sourceforge.net/'>http://quickfind.sourceforge.net/</a>)<br>\n\tCross-platform Java application for searching files in your Computer.\n<br><br>\n\tCopyright (c) 2010, 2012 Vasantkumar Mulage\n<br><br>\n\tAll rights reserved.\n<br><br>\n\tRedistribution and use in source and binary forms, with or without modification,\n\tare permitted provided that the following conditions are met:\n<br><br>\n\t    * Redistributions of source code must retain the above copyright notice,\n\t      this list of conditions and the following disclaimer.<br>\n\t    * Redistributions in binary form must reproduce the above copyright notice,\n\t      this list of conditions and the following disclaimer in the documentation\n\t      and/or other materials provided with the distribution.<br>\n\t    * Neither the name of the QuickFind nor the names of its contributors\n\t      may be used to endorse or promote products derived from this software without\n\t      specific prior written permission.\n<br><br>\n\n\tTHIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS<br>\n\t\"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT<br>\n\tLIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR<br>\n\tA PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR<br>\n\tCONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,<br>\n\tEXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,<br>\n\tPROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR\n\tPROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF<br>\n\tLIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING<br>\n\tNEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS<br>\n\tSOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.<br>\n</font>\n</p>\n</body>\r\n</html>\r");
        productLicenseEditorPane.setAutoscrolls(false);
        productLicenseScrollPane.setViewportView(productLicenseEditorPane);

        donateIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.DONATE_ICON)));
        donateIconLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        donateIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                donateIconLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sectionSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(238, 238, 238)
                        .addComponent(productNameLabel))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(productLicenseScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(productIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(authorLabel)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(authorNameLabel))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(homePageLabel)
                                        .addGap(35, 35, 35))
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(productVersionLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(donateIconLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(homePageValueLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(productVersionValueLabel, javax.swing.GroupLayout.Alignment.LEADING))))
                        .addGap(15, 15, 15)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(262, Short.MAX_VALUE)
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(259, 259, 259))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(authorLabel)
                            .addComponent(authorNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productVersionValueLabel)
                            .addComponent(productVersionLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(homePageLabel)
                            .addComponent(homePageValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(donateIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sectionSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productLicenseScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(closeButton))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*
     * Opens QuickFind home page in new browser window
     */
    private void homePageValueLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homePageValueLabelMouseClicked
        try {
            java.net.URI uri = new java.net.URI(homePageValueLabel.getName());
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException uRISyntaxException) {
            LOGGER.log(Level.SEVERE, "Invalid URI passed", uRISyntaxException);
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "Browser failed to lauch", iOException);
        }
    }//GEN-LAST:event_homePageValueLabelMouseClicked

    /*
     * Unloads the QuickFindAbout
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    /*
     * Resets productLicenseScrollPane position
     */
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        productLicenseScrollPane.getVerticalScrollBar().setValue(productLicenseScrollPane.getVerticalScrollBar().getMinimum());
    }//GEN-LAST:event_formWindowOpened

    /*
     * Opens QuickFind donate page in new browser window
     */
    private void donateIconLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_donateIconLabelMouseClicked
        try {
            java.net.URI uri = new java.net.URI("http://sourceforge.net/project/project_donations.php?group_id=330819");
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException URISyntaxEx) {
            LOGGER.log(Level.SEVERE, "Invalid URI passed", URISyntaxEx);
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "Browser failed to lauch", iOException);
        }
    }//GEN-LAST:event_donateIconLabelMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel authorLabel;
    private javax.swing.JLabel authorNameLabel;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel donateIconLabel;
    private javax.swing.JLabel homePageLabel;
    private javax.swing.JLabel homePageValueLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel productIconLabel;
    private javax.swing.JEditorPane productLicenseEditorPane;
    private javax.swing.JScrollPane productLicenseScrollPane;
    private javax.swing.JLabel productNameLabel;
    private javax.swing.JLabel productVersionLabel;
    private javax.swing.JLabel productVersionValueLabel;
    private javax.swing.JSeparator sectionSeparator;
    // End of variables declaration//GEN-END:variables
}
