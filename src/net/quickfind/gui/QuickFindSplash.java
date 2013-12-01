/*
    QuickFind (http://quickfind.sourceforge.net/)
    Cross-platform Java application for searching files in your Computer.

    Copyright (c) 2010, 2013 Vasantkumar Mulage

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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.quickfind.config.PropertyPage;

/*
 * QuickFindAbout.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
public class QuickFindSplash extends javax.swing.JFrame {

    /*
     * Exception Logger
     */
    private final static Logger LOGGER = Logger.getLogger(QuickFindSplash.class.getName());

    /*
     * Creates QuickFindSplash
     */
    public QuickFindSplash() {
        setUndecorated(true);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.SYSTEM_ICON)));
        initComponents();
        pack();
        this.setLocationRelativeTo(this);
        productNameLabel.grabFocus();
        productVersionValueLabel.setText(PropertyPage.PRODUCT_VERSION);
        homePageValueLabel.setText(PropertyPage.WEBSITE);
        mainPanel.setBackground(Color.WHITE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        spashImageLabel = new javax.swing.JLabel();
        productVersionLabel = new javax.swing.JLabel();
        productNameLabel = new javax.swing.JLabel();
        productVersionValueLabel = new javax.swing.JLabel();
        homePageLabel = new javax.swing.JLabel();
        homePageValueLabel = new javax.swing.JLabel();
        loadingLabel = new javax.swing.JLabel();
        splashUnderlineLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QuickFind");
        setBackground(java.awt.Color.white);
        setForeground(java.awt.Color.white);
        setResizable(false);

        mainPanel.setBackground(java.awt.Color.white);
        mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        spashImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.SPLASH_ICON)));

        productVersionLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        productVersionLabel.setText("Product Version:");

        productNameLabel.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        productNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productNameLabel.setText("QuickFind");
        productNameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        productVersionValueLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        productVersionValueLabel.setText("<html> \n<font size='4'>1.0.0.1</font>\n<font size='2'>BETA</font>\n</html>");

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

        loadingLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        loadingLabel.setText("Loading...");

        splashUnderlineLabel.setText("______________________________________");

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Instant File Search");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(splashUnderlineLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spashImageLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productNameLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productVersionLabel)
                    .addComponent(homePageLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productVersionValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(loadingLabel)
                        .addComponent(homePageValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productVersionLabel)
                            .addComponent(productVersionValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(homePageLabel)
                            .addComponent(homePageValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(spashImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splashUnderlineLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadingLabel)
                    .addComponent(productNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*
     * Opens QuickFind donate page in new browser window
     */
    private void homePageValueLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homePageValueLabelMouseClicked
        try {
            java.net.URI uri = new java.net.URI(homePageValueLabel.getName());
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException URISyntaxEx) {
            LOGGER.log(Level.SEVERE, "Invalid URI passed", URISyntaxEx);
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "Browser failed to launch", iOException);
        }
    }//GEN-LAST:event_homePageValueLabelMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel homePageLabel;
    private javax.swing.JLabel homePageValueLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel loadingLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel productNameLabel;
    private javax.swing.JLabel productVersionLabel;
    private javax.swing.JLabel productVersionValueLabel;
    private javax.swing.JLabel spashImageLabel;
    private javax.swing.JLabel splashUnderlineLabel;
    // End of variables declaration//GEN-END:variables
}
