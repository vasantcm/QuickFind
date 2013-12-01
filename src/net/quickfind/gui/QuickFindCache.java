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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import net.quickfind.cache.Cache;
import net.quickfind.config.PropertyPage;

/*
 * QuickFindCache.java
 * @author  Copyright (C) 2013 Vasantkumar Mulage
 */
public class QuickFindCache extends javax.swing.JDialog {

    /*
     * Cache object to hold cache related data
     */
    private Cache cache;
    /*
     * To confirm user action
     */
    private boolean isCancelled = true;
    /*
     * Includes list model
     */
    private DefaultListModel includesPathListModel = new DefaultListModel();
    /*
     * Excludes list model
     */
    private DefaultListModel excludesPathListModel = new DefaultListModel();
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(QuickFindCache.class.getName());

    /** Creates new form QuickFindCache */
    public QuickFindCache(java.awt.Frame parent, boolean modal, String title) {
        super(parent, modal);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.SYSTEM_ICON)));
        initComponents();
        setTitle(title);
        this.setLocationRelativeTo(this);

        /*
         * Processes key input
         */
        includesPathList.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                processIncludesPathListFieldInput(evt);
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                processIncludesPathListFieldInput(evt);
            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                processIncludesPathListFieldInput(evt);
            }
        });

        /*
         * Processes key input
         */
        excludesPathList.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                processExcludesPathListFieldInput(evt);
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                processExcludesPathListFieldInput(evt);
            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                processExcludesPathListFieldInput(evt);
            }
        });

        cache = new Cache();
        includesPathList.setModel(includesPathListModel);
        excludesPathList.setModel(excludesPathListModel);
    }

    /*
     * Deletes selected include path from the list if no child exist(if nothing excluded)
     */
    private void processIncludesPathListFieldInput(java.awt.event.KeyEvent evt) {
        if (KeyEvent.VK_DELETE == evt.getKeyChar()) {

            int selectedIndices[] = includesPathList.getSelectedIndices();

            for (int i = 0, k = 0; i < selectedIndices.length; i++, k++) {
                int matchCount = 0;
                int absoluteCount = -1;
                for (int j = 0; j < excludesPathListModel.getSize(); j++) {
                    StringTokenizer excludesPathTokenizer = new StringTokenizer(excludesPathListModel.get(j).toString(), PropertyPage.FILE_SEPARATOR);
                    StringTokenizer includesPathTokenizer = new StringTokenizer(includesPathListModel.get(selectedIndices[i] - k).toString(), PropertyPage.FILE_SEPARATOR);
                    absoluteCount = includesPathTokenizer.countTokens();
                    while (excludesPathTokenizer.hasMoreTokens() && includesPathTokenizer.hasMoreTokens()) {
                        if (excludesPathTokenizer.nextToken().equals(includesPathTokenizer.nextToken())) {
                            matchCount++;
                        } else {
                            break;
                        }
                    }
                    /*
                     * Child found in the excluded list
                     */
                    if (matchCount == absoluteCount) {
                        break;
                    }
                    matchCount = 0;
                    absoluteCount = -1;
                }
                if (matchCount == absoluteCount) {
                    JOptionPane.showMessageDialog(this, "Remove the excluded child first");
                    continue;
                } else {
                    includesPathListModel.remove(selectedIndices[i] - k);
                }
            }
        }
    }

    /*
     * Deletes selected exclude path from the list
     */
    private void processExcludesPathListFieldInput(java.awt.event.KeyEvent evt) {
        if (KeyEvent.VK_DELETE == evt.getKeyChar()) {
            int selectedIndices[] = excludesPathList.getSelectedIndices();
            for (int i = 0, k = 0; i < selectedIndices.length; i++, k++) {
                excludesPathListModel.remove(selectedIndices[i] - k);
            }
        }
    }

    /*
     * Opens new cache dialogue with cache id as user given cache name
     * @param   cache id as seed value
     */
    protected Cache showDialog(int cacheId) {
        cacheNameTextField.setText("Cache-" + cacheId);
        cacheNameTextField.selectAll();
        setVisible(true);
        cache.setuserGivenName(cacheNameTextField.getText());
        String includes = "";
        for (int i = 0; i < includesPathListModel.size(); i++) {
            includes += includesPathListModel.get(i) + "|";
        }
        cache.setIncludedPath(includes);

        String excludes = "";
        for (int i = 0; i < excludesPathListModel.size(); i++) {
            excludes += excludesPathListModel.get(i) + "|";
        }
        cache.setExcludedPath(excludes);

        cache.setCacheName(String.valueOf(includes.hashCode()));

        if (isCancelled) {
            return null;
        }
        return cache;
    }

    /*
     * Opens new cache dialogue with cache object in edit mode
     * @param   cache object to be edited
     */
    protected Cache showDialog(Cache cache) {

        cacheNameTextField.setText(cache.getuserGivenName());
        includesPathListModel.removeAllElements();
        excludesPathListModel.removeAllElements();
        String includedPaths = cache.getIncludedPath();
        StringTokenizer includedPathsTokenizer = new StringTokenizer(includedPaths, "|");
        while (includedPathsTokenizer.hasMoreElements()) {
            includesPathListModel.addElement(includedPathsTokenizer.nextToken());
        }

        String excludedPaths = cache.getExcludedPath();
        StringTokenizer excludedPathsTokenizer = new StringTokenizer(excludedPaths, "|");
        while (excludedPathsTokenizer.hasMoreElements()) {
            excludesPathListModel.addElement(excludedPathsTokenizer.nextToken());
        }

        setVisible(true);

        cache.setuserGivenName(cacheNameTextField.getText());
        includedPaths = "";
        for (int i = 0; i < includesPathListModel.size(); i++) {
            includedPaths += includesPathListModel.get(i) + "|";
        }
        cache.setIncludedPath(includedPaths);

        excludedPaths = "";
        for (int i = 0; i < excludesPathListModel.size(); i++) {
            excludedPaths += excludesPathListModel.get(i) + "|";
        }
        cache.setExcludedPath(excludedPaths);

        cache.setCacheName(String.valueOf(includedPaths.hashCode()));

        if (isCancelled) {
            return null;
        }
        return cache;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        QuickFindCacheMainPanel = new javax.swing.JPanel();
        cacheNameLabel = new javax.swing.JLabel();
        cacheNameTextField = new javax.swing.JTextField();
        includesPathLabel = new javax.swing.JLabel();
        includesPathListScrollPane = new javax.swing.JScrollPane();
        includesPathList = new javax.swing.JList();
        includesBrowseButton = new javax.swing.JButton();
        excludesPathLabel = new javax.swing.JLabel();
        excludesPathListScrollPane = new javax.swing.JScrollPane();
        excludesPathList = new javax.swing.JList();
        excludesBrowseButton = new javax.swing.JButton();
        addCacheButton = new javax.swing.JButton();
        cancelCacheButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        QuickFindCacheMainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        cacheNameLabel.setText("Cache Name");

        includesPathLabel.setText("Includes");

        includesPathListScrollPane.setViewportView(includesPathList);

        includesBrowseButton.setText("Browse");
        includesBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includesBrowseButtonActionPerformed(evt);
            }
        });

        excludesPathLabel.setText("Excludes");

        excludesPathListScrollPane.setViewportView(excludesPathList);

        excludesBrowseButton.setText("Browse");
        excludesBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excludesBrowseButtonActionPerformed(evt);
            }
        });

        addCacheButton.setText("Add");
        addCacheButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCacheButtonActionPerformed(evt);
            }
        });

        cancelCacheButton.setText("Cancel");
        cancelCacheButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelCacheButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout QuickFindCacheMainPanelLayout = new javax.swing.GroupLayout(QuickFindCacheMainPanel);
        QuickFindCacheMainPanel.setLayout(QuickFindCacheMainPanelLayout);
        QuickFindCacheMainPanelLayout.setHorizontalGroup(
            QuickFindCacheMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuickFindCacheMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cacheNameLabel)
                .addContainerGap(364, Short.MAX_VALUE))
            .addGroup(QuickFindCacheMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cacheNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addGap(36, 36, 36))
            .addGroup(QuickFindCacheMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(includesPathLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(includesBrowseButton)
                .addContainerGap(313, Short.MAX_VALUE))
            .addGroup(QuickFindCacheMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(includesPathListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addGap(34, 34, 34))
            .addGroup(QuickFindCacheMainPanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(excludesPathLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excludesBrowseButton)
                .addContainerGap(310, Short.MAX_VALUE))
            .addGroup(QuickFindCacheMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(excludesPathListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addGap(34, 34, 34))
            .addGroup(QuickFindCacheMainPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(addCacheButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(cancelCacheButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        QuickFindCacheMainPanelLayout.setVerticalGroup(
            QuickFindCacheMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuickFindCacheMainPanelLayout.createSequentialGroup()
                .addComponent(cacheNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cacheNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(QuickFindCacheMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(includesPathLabel)
                    .addComponent(includesBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(includesPathListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(QuickFindCacheMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(excludesBrowseButton)
                    .addComponent(excludesPathLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excludesPathListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(QuickFindCacheMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCacheButton)
                    .addComponent(cancelCacheButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(QuickFindCacheMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(QuickFindCacheMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*
     * Adds/Edits cache
     */
    private void addCacheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCacheButtonActionPerformed
        if (!cacheNameTextField.getText().matches("^[a-zA-Z0-9_-]{1,15}$")) {
            JOptionPane.showMessageDialog(this, "Please use only letters (a-z), numbers, underscore and hyphen, Use between 1 and 15 characters");
            return;
        }
        if (includesPathListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Minimum one path must be included for a cache");
            return;
        }

        for (int i = 0; i < includesPathListModel.getSize(); i++) {
            if (excludesPathListModel.contains(includesPathListModel.elementAt(i))) {
                JOptionPane.showMessageDialog(this, "Included path cannot be excluded\n" + includesPathListModel.elementAt(i));
                return;
            }
        }

        isCancelled = false;
        setVisible(false);
        dispose();
}//GEN-LAST:event_addCacheButtonActionPerformed

    /*
     * Browse to add new include path
     */
    private void includesBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_includesBrowseButtonActionPerformed
        JFileChooser includesPathFileChooser = new JFileChooser();
        includesPathFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        includesPathFileChooser.setMultiSelectionEnabled(true);
        includesPathFileChooser.setFileHidingEnabled(true);
        int returnState = includesPathFileChooser.showOpenDialog(this);

        if (returnState == JFileChooser.APPROVE_OPTION) {
            File selectdFiles[] = includesPathFileChooser.getSelectedFiles();
            for (int i = 0; i < selectdFiles.length; i++) {
                String absolutePath = selectdFiles[i].getAbsolutePath();
                absolutePath = absolutePath.replace(PropertyPage.FILE_SEPARATOR + "..", PropertyPage.FILE_SEPARATOR);
                StringTokenizer selectedAbsolutePathTokenizer = new StringTokenizer(absolutePath, PropertyPage.FILE_SEPARATOR);

                int includedPathItemsCount = -1;
                int matchCount = 0;

                for (int j = 0; j < includesPathListModel.getSize(); j++) {
                    StringTokenizer includesPathTokenizer = new StringTokenizer(includesPathListModel.get(j).toString(), PropertyPage.FILE_SEPARATOR);
                    includedPathItemsCount = includesPathTokenizer.countTokens();

                    while (includesPathTokenizer.hasMoreTokens() && selectedAbsolutePathTokenizer.hasMoreTokens()) {
                        if (includesPathTokenizer.nextToken().equals(selectedAbsolutePathTokenizer.nextToken())) {
                            matchCount++;
                        } else {
                            break;
                        }
                    }
                    /*
                     * Parent exists in the included list
                     */
                    if (matchCount == includedPathItemsCount) {
                        break;
                    }
                    includedPathItemsCount = -1;
                    matchCount = 0;
                }

                if (matchCount == includedPathItemsCount) {
                    JOptionPane.showMessageDialog(this, "Its parent path already included");
                    continue;
                }

                includedPathItemsCount = -1;
                matchCount = 0;

                for (int j = 0; j < excludesPathListModel.getSize(); j++) {
                    StringTokenizer excludesPathTokenizer = new StringTokenizer(excludesPathListModel.get(j).toString(), PropertyPage.FILE_SEPARATOR);
                    includedPathItemsCount = excludesPathTokenizer.countTokens();

                    while (excludesPathTokenizer.hasMoreTokens() && selectedAbsolutePathTokenizer.hasMoreTokens()) {
                        if (excludesPathTokenizer.nextToken().equals(selectedAbsolutePathTokenizer.nextToken())) {
                            matchCount++;
                        } else {
                            break;
                        }
                    }

                    /*
                     * Child found in excluded list
                     */
                    if (matchCount == includedPathItemsCount) {
                        break;
                    }
                    includedPathItemsCount = -1;
                    matchCount = 0;
                }

                if (matchCount == includedPathItemsCount) {
                    JOptionPane.showMessageDialog(this, "Its child path already excluded");
                    continue;
                }


                includedPathItemsCount = -1;
                matchCount = 0;

                for (int j = 0; j < includesPathListModel.getSize(); j++) {
                    StringTokenizer includesPathTokenizer = new StringTokenizer(includesPathListModel.get(j).toString(), PropertyPage.FILE_SEPARATOR);
                    includedPathItemsCount = selectedAbsolutePathTokenizer.countTokens();

                    while (includesPathTokenizer.hasMoreTokens() && selectedAbsolutePathTokenizer.hasMoreTokens()) {
                        if (includesPathTokenizer.nextToken().equals(selectedAbsolutePathTokenizer.nextToken())) {
                            matchCount++;
                        } else {
                            break;
                        }
                    }
                    /*
                     * Child found in the included list
                     */
                    if (matchCount == includedPathItemsCount) {
                        break;
                    }
                    includedPathItemsCount = -1;
                    matchCount = 0;
                }

                if (matchCount == includedPathItemsCount) {
                    JOptionPane.showMessageDialog(this, "Its child path included remove it first");
                    continue;
                }
                includesPathListModel.addElement(absolutePath);
            }
        }
    }//GEN-LAST:event_includesBrowseButtonActionPerformed

    /*
     * Unloads cache dialogue
     */
    private void cancelCacheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelCacheButtonActionPerformed
        isCancelled = true;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelCacheButtonActionPerformed

    /*
     * Browse to add new exclude path
     */
    private void excludesBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excludesBrowseButtonActionPerformed

        JFileChooser excludesPathFileChooser = new JFileChooser();
        excludesPathFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        excludesPathFileChooser.setMultiSelectionEnabled(true);
        excludesPathFileChooser.setFileHidingEnabled(true);
        int returnState = excludesPathFileChooser.showOpenDialog(this);

        if (returnState == JFileChooser.APPROVE_OPTION) {
            File selectdFiles[] = excludesPathFileChooser.getSelectedFiles();
            for (int i = 0; i < selectdFiles.length; i++) {
                String absolutePath = selectdFiles[i].getAbsolutePath();
                absolutePath = absolutePath.replace(PropertyPage.FILE_SEPARATOR + "..", PropertyPage.FILE_SEPARATOR);
                StringTokenizer selectedAbsolutePathTokenizer = new StringTokenizer(absolutePath, PropertyPage.FILE_SEPARATOR);
                int matchCount = 0;
                int absoluteCount = -1;

                for (int j = 0; j < includesPathListModel.getSize(); j++) {
                    StringTokenizer includesPathTokenizer = new StringTokenizer(includesPathListModel.get(j).toString(), PropertyPage.FILE_SEPARATOR);
                    absoluteCount = selectedAbsolutePathTokenizer.countTokens();
                    while (includesPathTokenizer.hasMoreTokens() && selectedAbsolutePathTokenizer.hasMoreTokens()) {
                        if (includesPathTokenizer.nextToken().equals(selectedAbsolutePathTokenizer.nextToken())) {
                            matchCount++;
                        } else {
                            break;
                        }
                    }
                    /*
                     * Child found in the included list
                     */
                    if (matchCount == absoluteCount) {
                        break;
                    }
                    matchCount = 0;
                    absoluteCount = -1;
                }

                if (matchCount == absoluteCount) {
                    JOptionPane.showMessageDialog(this, "Its child path already included");
                    continue;
                }


                int modelCount = -1;
                matchCount = 0;

                for (int j = 0; j < includesPathListModel.getSize(); j++) {
                    StringTokenizer includesPathTokenizer = new StringTokenizer(includesPathListModel.get(j).toString(), PropertyPage.FILE_SEPARATOR);
                    modelCount = includesPathTokenizer.countTokens();
                    while (includesPathTokenizer.hasMoreTokens() && selectedAbsolutePathTokenizer.hasMoreTokens()) {
                        if (includesPathTokenizer.nextToken().equals(selectedAbsolutePathTokenizer.nextToken())) {
                            matchCount++;
                        } else {
                            break;
                        }
                    }
                    /*
                     * Parent not found in the included list
                     */
                    if (matchCount == modelCount) {
                        break;
                    }
                    modelCount = -1;
                    matchCount = 0;
                }

                if (matchCount != modelCount) {
                    JOptionPane.showMessageDialog(this, "Its Parent path not included");
                    continue;
                }

                if (excludesPathListModel.contains(absolutePath)) {
                    JOptionPane.showMessageDialog(this, absolutePath + "\nSame path already excluded!");
                    continue;
                }
                excludesPathListModel.addElement(absolutePath);
            }
        }
    }//GEN-LAST:event_excludesBrowseButtonActionPerformed

    /*
     * Register escape key listener
     */
    @Override
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {

            /*
             * Hides cache window whenever escape is pressed
             */
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isCancelled = true;
                setVisible(false);
                dispose();
            }
        };
        JRootPane jRootPane = new JRootPane();
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        jRootPane.registerKeyboardAction(actionListener, keyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return jRootPane;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel QuickFindCacheMainPanel;
    private javax.swing.JButton addCacheButton;
    private javax.swing.JLabel cacheNameLabel;
    private javax.swing.JTextField cacheNameTextField;
    private javax.swing.JButton cancelCacheButton;
    private javax.swing.JButton excludesBrowseButton;
    private javax.swing.JLabel excludesPathLabel;
    private javax.swing.JList excludesPathList;
    private javax.swing.JScrollPane excludesPathListScrollPane;
    private javax.swing.JButton includesBrowseButton;
    private javax.swing.JLabel includesPathLabel;
    private javax.swing.JList includesPathList;
    private javax.swing.JScrollPane includesPathListScrollPane;
    // End of variables declaration//GEN-END:variables
}
