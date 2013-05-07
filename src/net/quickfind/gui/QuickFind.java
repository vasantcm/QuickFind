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

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.quickfind.config.PropertyPage;
import net.quickfind.cache.CachePage;
import net.quickfind.cache.Scanner;
import net.quickfind.core.Utility;
import net.quickfind.find.Search;

/*
 * QuickFind.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
public class QuickFind extends javax.swing.JFrame {

    /*
     * System tray icon
     */
    private TrayIcon QFTrayIcon;
    /*
     * Collection of available lookAndFeels
     */
    private final UIManager.LookAndFeelInfo[] lookAndFeelCollection = UIManager.getInstalledLookAndFeels();
    /*
     * Search scanner flag
     */
    private static boolean isSearchScannerRunning = false;
    /*
     * Search result flag
     */
    private boolean isItemFound;
    /*
     * Index to find out currently displayed result set
     */
    private int topListIndex = 0;
    /*
     * Icon to represent deleted/inaccessible File/Directory
     */
    private final DeletedFileIcon deletedFileIcon = new DeletedFileIcon();
    /*
     * Search disable flag
     */
    private boolean isSearchDisabled;
    /*
     * Cache page for cache configurations
     */
    private CachePage cachedPages;
    /*
     * Map to store icons and file extension as key
     */
    private HashMap<String, Icon> fileIconCache;
    /*
     * Map to store file extension and icon string as key
     */
    private HashMap<String, String> fileIconStringCache;
    /*
     * Caching flag
     */
    private boolean isCaching = false;
    /*
     * Default limit for displaying search resultset
     */
    private final int RESULT_SET_LIMIT = PropertyPage.get_SEARCH_RESULT_SET_LIMIT();
    //GUI Components Declaration
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu themesMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem copyPathMenuItem;
    private javax.swing.JMenuItem openLocationPopUpMenuItem;
    private javax.swing.JMenuItem openPopUpMenuItem;
    private javax.swing.JMenuItem renamePopUpMenuItem;
    private javax.swing.JMenuItem copyPathPopUpMenuItem;
    private javax.swing.JMenuItem selectAllMenuItem;
    //private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JPopupMenu popUpMenu;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton quickFindButton;
    private javax.swing.JButton cacheButton;
    private javax.swing.JComboBox searchComboBox;
    private javax.swing.JComboBox cacheComboBox;
    private javax.swing.JTable resultTable;
    private javax.swing.JToolBar statusToolBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel resultSetIndexLabel;
    private javax.swing.JButton previousRowsButton;
    private javax.swing.JButton nextRowsButton;
    private javax.swing.JTabbedPane qfTabbedPane;
    private javax.swing.JScrollPane resultTableScrollPane;
    private javax.swing.JPanel quickFindPanel;
    private javax.swing.JPanel advancedPanel;
    //private javax.swing.JPanel cacheManagerPanel;
    private javax.swing.JTextArea upcomingFeaturesTextArea;
    private javax.swing.JPanel quickFindSubComponentsPanel;
    /*
     * Sorts table rows
     */
    private TableRowSorter<DefaultTableModel> tableRowSorter;
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(QuickFind.class.getName());

    /*
     * Constructs all the GUI components and application specific preferences
     *      Draws GUI Components on to the Frame
     *      Creates application system tray icon with required systemTrayPopUp menuitems
     *      Initializes application specific resources
     */
    @SuppressWarnings("static-access")
    public QuickFind(CachePage cachedPage) {
        isSearchDisabled = false;
        cachedPages = cachedPage;
        fileIconCache = new HashMap<String, Icon>(100);
        fileIconStringCache = new HashMap<String, String>(100);
        isItemFound = false;
        initComponents();
        setQuickFindTray();
        initApplication();
        applyNewLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    /*
     * Adds QuickFind Tray Icon
     */
    private void setQuickFindTray() {
        if (SystemTray.isSupported()) {
            SystemTray quickFindSystemTray = SystemTray.getSystemTray();
            Image systemTrayImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.SYSTEM_ICON));
            PopupMenu systemTrayPopUp = new PopupMenu();
            MenuItem qfLogoMenuItem = new MenuItem("QuickFind© Instant File Search");
            final MenuItem systemTrayCacheMenuItem = new MenuItem("Refresh Cache");
            MenuItem systemTrayShowMenuItem = new MenuItem("Show Screen");
            MenuItem systemTrayAboutMenuItem = new MenuItem("About");
            MenuItem systemTrayExitMenuItem = new MenuItem("Exit");

            MouseListener mouseListener = new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    if (isCaching) {
                        systemTrayCacheMenuItem.setLabel("Stop Caching");
                    } else {
                        systemTrayCacheMenuItem.setLabel("Refresh Cache");
                    }
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        QuickFind.this.setVisible(true);
                        QuickFind.this.setExtendedState(QuickFind.this.getExtendedState() & ~JFrame.ICONIFIED);
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if (isCaching) {
                        systemTrayCacheMenuItem.setLabel("Stop Caching");
                    } else {
                        systemTrayCacheMenuItem.setLabel("Refresh Cache");
                    }
                }
            };

            ActionListener exitActionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("QuickFind Exiting...");
                    System.exit(0);
                }
            };

            ActionListener activeActionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    QFTrayIcon.displayMessage("QuickFind", "QuickFind is Active!", TrayIcon.MessageType.INFO);
                }
            };

            ActionListener showActionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    QuickFind.this.setVisible(true);
                    QuickFind.this.setExtendedState(QuickFind.this.getExtendedState() & ~JFrame.ICONIFIED);
                }
            };

            ActionListener cacheActionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (cacheButton.isEnabled()) {
                        cacheButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(QuickFind.this, "Please wait previous cache operation is still working", "Cache request rejected", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (systemTrayCacheMenuItem.getLabel().equals("Refresh Cache")) {
                        systemTrayCacheMenuItem.setLabel("Stop Caching");
                    } else {
                        systemTrayCacheMenuItem.setLabel("Refresh Cache");
                    }
                }
            };

            ActionListener aboutActionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new QuickFindAbout(QuickFind.this, true).setVisible(true);
                }
            };

            qfLogoMenuItem.setFont(new Font(null, Font.BOLD, 12));
            systemTrayCacheMenuItem.addActionListener(cacheActionListener);
            systemTrayShowMenuItem.addActionListener(showActionListener);
            systemTrayAboutMenuItem.addActionListener(aboutActionListener);
            systemTrayExitMenuItem.addActionListener(exitActionListener);

            systemTrayPopUp.add(qfLogoMenuItem);
            systemTrayPopUp.addSeparator();
            systemTrayPopUp.add(systemTrayCacheMenuItem);
            systemTrayPopUp.add(systemTrayShowMenuItem);
            systemTrayPopUp.addSeparator();
            systemTrayPopUp.add(systemTrayAboutMenuItem);
            systemTrayPopUp.addSeparator();
            systemTrayPopUp.add(systemTrayExitMenuItem);

            QFTrayIcon = new TrayIcon(systemTrayImage, "QuickFind", systemTrayPopUp);
            QFTrayIcon.setImageAutoSize(true);
            QFTrayIcon.addActionListener(activeActionListener);
            QFTrayIcon.addMouseListener(mouseListener);
            try {
                quickFindSystemTray.add(QFTrayIcon);
            } catch (NullPointerException nullPointerException) {
                LOGGER.log(Level.SEVERE, "TrayIcon could not be added", nullPointerException);
            } catch (IllegalArgumentException illegalArgumentException) {
                LOGGER.log(Level.SEVERE, "TrayIcon is already added", illegalArgumentException);
            } catch (AWTException aWTException) {
                LOGGER.log(Level.SEVERE, "System Tray is not available", aWTException);
            }
        }
    }

    /*
     * Changes current look and feel to new look if it is supported
     * @param   newLook 
     */
    private void applyNewLookAndFeel(final String newLook) {
        try {
            if (!isLookAndFeelAvailable(newLook)) {
                JOptionPane.showMessageDialog(this, "Sorry this look not Supported!");
                return;
            }
            UIManager.setLookAndFeel(newLook);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception exception) {
            LOGGER.log(Level.INFO, "Could not apply new Look and Feel", exception);
            JFrame.setDefaultLookAndFeelDecorated(true);
        }
    }

    /*
     * Checks for the availability of the look and feel
     * @param   newLookAndFeel
     */
    private boolean isLookAndFeelAvailable(final String newLookAndFeel) {
        try {
            Class newLookAndFeelClass = Class.forName(newLookAndFeel);
            LookAndFeel thisLookAndFeel = (LookAndFeel) (newLookAndFeelClass.newInstance());
            return thisLookAndFeel.isSupportedLookAndFeel();
        } catch (Exception exception) {
            LOGGER.log(Level.INFO, "This Look and Feel not available", exception);
            return false;
        }
    }

    /*
     * Initializes all the defined GUI components
     */
    private void initComponents() {
        popUpMenu = new javax.swing.JPopupMenu();
        openPopUpMenuItem = new javax.swing.JMenuItem();
        openLocationPopUpMenuItem = new javax.swing.JMenuItem();
        renamePopUpMenuItem = new javax.swing.JMenuItem();
        copyPathPopUpMenuItem = new javax.swing.JMenuItem();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        copyPathMenuItem = new javax.swing.JMenuItem();
        selectAllMenuItem = new javax.swing.JMenuItem();
        themesMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        //contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        searchComboBox = new javax.swing.JComboBox();
        cacheComboBox = new javax.swing.JComboBox();


        searchComboBox.setToolTipText("select the cache");
        cacheComboBox.setToolTipText("select the cache root");

        quickFindPanel = new JPanel(new BorderLayout(5, 5));
        quickFindPanel.setBorder(new TitledBorder("")); //Border with empty title

        advancedPanel = new JPanel(new BorderLayout(5, 5));
        advancedPanel.setBorder(new TitledBorder(""));//Border with empty title

        //cacheManagerPanel = new JPanel(new BorderLayout(5, 5));
        //cacheManagerPanel.setBorder(new TitledBorder(""));//Border with empty title

        upcomingFeaturesTextArea = new JTextArea();
        upcomingFeaturesTextArea.setText("Advanced features coming soon\n1) Auto/scheduled caching mechanism\n2) Multi-Select roots for both search and cache operations\n3) Search by FileDate, FileSize etc\n4) Predefined search filters\n5) Sort By options.\n6) Cut, copy, paste, delete operations on selected File/Directory");
        upcomingFeaturesTextArea.setEditable(false);
        upcomingFeaturesTextArea.setBackground(Color.WHITE);

        advancedPanel.add(upcomingFeaturesTextArea, BorderLayout.WEST);
        advancedPanel.setBackground(Color.WHITE);

        quickFindSubComponentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        qfTabbedPane = new javax.swing.JTabbedPane();
        qfTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        qfTabbedPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        qfTabbedPane.setVerifyInputWhenFocusTarget(false);

        DefaultTableModel defaultTableModel = new DefaultTableModel();

        resultTable = new javax.swing.JTable(defaultTableModel) {

            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };

        resultTable.setFont(PropertyPage.DEFAULT_RESULT_TABLE_FONT);
        resultTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        resultTable.setSelectionBackground(Color.LIGHT_GRAY);

        tableRowSorter = new TableRowSorter<DefaultTableModel>(defaultTableModel);
        resultTable.setRowSorter(tableRowSorter);

        resultTable.addMouseListener(new java.awt.event.MouseAdapter() {

            /*
             * On mouse double click event try to open the File/Directory
             */
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClickedOnResultTable(evt);
            }

            /*
             * If the selected item is directory then disables the Open PopUp MenuItem
             */
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mousePressedOnResultTable(evt);
            }
        });

        /*
         * Displays the tool tip information based on the mouse position
         * Filename, Absolute Path
         */
        resultTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mouseMovedOnResultTable(evt);
            }
        });

        ((DefaultTableModel) resultTable.getModel()).addColumn("Name");
        ((DefaultTableModel) resultTable.getModel()).addColumn("Size");

        resultTableScrollPane = new JScrollPane(resultTable);   //adding scrollbar to the resultTable
        Dimension tablePreferredSize = resultTableScrollPane.getPreferredSize();
        resultTableScrollPane.setPreferredSize(new Dimension(tablePreferredSize.width, tablePreferredSize.height));
        /*
         * Adjusting the column widths
         */
        int nameColumnWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.9);
        int sizeColumnWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - nameColumnWidth;
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(nameColumnWidth - 25);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(sizeColumnWidth - 15);
        resultTable.setRowHeight(PropertyPage.DEFAULT_RESULT_TABLE_ROW_HEIGHT);

        resultTable.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

        cacheButton = new javax.swing.JButton();
        cacheButton.setFont(PropertyPage.DEFAULT_BUTTON_FONT);
        cacheButton.setToolTipText("Cache here whenever you want");
        cacheButton.setText("Cache");

        /*
         * Calls cache button action perfomed
         */
        cacheButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cacheButtonActionPerformed(evt);
            }
        });

        quickFindButton = new javax.swing.JButton();
        quickFindButton.setFont(PropertyPage.DEFAULT_BUTTON_FONT);
        quickFindButton.setToolTipText("Quickly Find your file");
        quickFindButton.setIcon(new ImageIcon(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.PROCESSING_ICON)));

        quickFindButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quickFindButtonActionPerformed(evt);
            }
        });

        searchTextField = new javax.swing.JTextField((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 100) * 5));
        searchTextField.setFont(PropertyPage.DEFAULT_TEXT_FIELD_FONT);
        searchTextField.setToolTipText("Type Here");

        /*
         * Resize searchTextField width according to frame width
         */
        this.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                searchTextField.setColumns( (int) (QuickFind.this.getBounds().getWidth() / 100) * 5);
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }
        });


        /*
         * Processes key input
         */
        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                processTextFieldInput(evt);
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                processTextFieldInput(evt);
            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                processTextFieldInput(evt);
            }
        });

        /*
         * Calls Scan
         */
        searchTextField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!isSearchDisabled) {
                    startScan();
                }
            }
        });

        final JPanel statusBarPanel = new JPanel(new BorderLayout());
        JPanel statusBarFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        statusToolBar = new javax.swing.JToolBar("Status", JToolBar.HORIZONTAL);
        statusLabel = new javax.swing.JLabel();
        resultSetIndexLabel = new javax.swing.JLabel();

        statusLabel.setForeground(Color.RED);
        statusLabel.setText("QuickFind (Instant File Search)");
        resultSetIndexLabel.setForeground(Color.RED);
        resultSetIndexLabel.setFont(new Font(null, Font.BOLD, 11));
        resultSetIndexLabel.setText("0-0");

        statusBarPanel.add(statusLabel, BorderLayout.WEST);
        statusBarPanel.add(resultSetIndexLabel, BorderLayout.EAST);
        statusToolBar.setFloatable(false);
        previousRowsButton = new javax.swing.JButton();
        previousRowsButton.setContentAreaFilled(false);
        previousRowsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        previousRowsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.PREVIOUS_ROWS_ICON)));

        /*
         * Loads previous resultset into the result table
         */
        previousRowsButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousRowsButtonActionPerformed(evt);
            }
        });


        statusBarFlowPanel.add(previousRowsButton);

        nextRowsButton = new javax.swing.JButton();
        nextRowsButton.setContentAreaFilled(false);
        nextRowsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextRowsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.NEXT_ROWS_ICON)));
        nextRowsButton.addActionListener(new java.awt.event.ActionListener() {

            /*
             * Loads next resultset into the result table
             */
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextRowsButtonActionPerformed(evt);
            }
        });

        statusBarFlowPanel.add(nextRowsButton);
        statusBarPanel.add(statusBarFlowPanel);
        statusToolBar.add(statusBarPanel);

        quickFindSubComponentsPanel.add(searchTextField);
        quickFindSubComponentsPanel.add(searchComboBox);
        quickFindSubComponentsPanel.add(quickFindButton);
        quickFindSubComponentsPanel.add(cacheComboBox);
        quickFindSubComponentsPanel.add(cacheButton);


        quickFindPanel.add(quickFindSubComponentsPanel, BorderLayout.NORTH);
        quickFindPanel.add(resultTableScrollPane, BorderLayout.CENTER);
        quickFindPanel.add(statusToolBar, BorderLayout.SOUTH);

        openPopUpMenuItem.setText("Open");

        /*
         * Tries to open/execute the File
         */
        openPopUpMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openPopUpMenuItemActionPerformed(evt);
            }
        });

        popUpMenu.add(openPopUpMenuItem);

        openLocationPopUpMenuItem.setText("Open Location");

        /*
         * Tries to open the location of selected File/Directory
         */
        openLocationPopUpMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openLocationPopUpMenuItemActionPerformed(evt);
            }
        });

        popUpMenu.add(openLocationPopUpMenuItem);

        renamePopUpMenuItem.setText("Rename");
        renamePopUpMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renamePopUpMenuItemActionPerformed(evt);
            }
        });

        popUpMenu.add(renamePopUpMenuItem);

        copyPathPopUpMenuItem.setText("Copy Path(s)");

        /*
         * Copies the selected Path(s) to clipboard
         */
        copyPathPopUpMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyPathPopUpMenuItemActionPerformed(evt);
            }
        });

        popUpMenu.add(copyPathPopUpMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QuickFind");

        fileMenu.setText("File");
        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("New");

        /*
         * Resets the previous search result by clearing table contents and other objects
         */
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(newMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.setEnabled(false);
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.setEnabled(false);
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit");

        /*
         * Quits the application with normal termination
         */
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);
        mainMenuBar.add(fileMenu);
        editMenu.setText("Edit");
        copyPathMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyPathMenuItem.setText("Copy Path(s)");

        /*
         * Copies the selected Path(s) to clipboard
         */
        copyPathMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyPathMenuItemActionPerformed(evt);
            }
        });

        editMenu.add(copyPathMenuItem);

        selectAllMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        selectAllMenuItem.setText("Select All");

        /*
         * Selects all the rows in the resultTable
         */
        selectAllMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllMenuItemActionPerformed(evt);
            }
        });

        editMenu.add(selectAllMenuItem);
        mainMenuBar.add(editMenu);
        themesMenu.setText("Themes");

        /*
         * Creates MenuItem for each LookAndFeel dynamically
         * Adds action performed event to apply particular look.
         */
        for (int lookAndFeelIndex = 0; lookAndFeelIndex < lookAndFeelCollection.length; lookAndFeelIndex++) {
            javax.swing.JMenuItem lookAndFeelMenuItem = new javax.swing.JMenuItem();
            lookAndFeelMenuItem.setText(lookAndFeelCollection[lookAndFeelIndex].getName());
            final int selectedIndex = lookAndFeelIndex;

            lookAndFeelMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    applyNewLookAndFeel(lookAndFeelCollection[selectedIndex].getClassName());
                }
            });

            themesMenu.add(lookAndFeelMenuItem);
        }
        mainMenuBar.add(themesMenu);

        helpMenu.setText("Help");

        //contentsMenuItem.setText("Contents");
        //contentsMenuItem.setEnabled(false);
        //helpMenu.add(contentsMenuItem);

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        aboutMenuItem.setText("About");

        /*
         * Displays about dialog window
         */
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutMenuItem);
        mainMenuBar.add(helpMenu);
        setJMenuBar(mainMenuBar);

        qfTabbedPane.add("QuickFind", quickFindPanel);
        qfTabbedPane.add("Advanced", advancedPanel);
        //qfTabbedPane.add("CacheManager", cacheManagerPanel);
        this.setContentPane(qfTabbedPane);
        this.setLocationRelativeTo(this);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0,0, screenSize.width/2 , screenSize.height/2);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        if (SystemTray.isSupported()) {
            this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
        cacheButton.setToolTipText("Cache All/Selected root(s), if not yet done!");
    }

    private void cacheButtonActionPerformed(java.awt.event.ActionEvent evt) {
        startCache();
    }

    /*
     * Creates cache file for each root selected
     */
    private void startCache() {
        final long cacheStartTime = System.currentTimeMillis();
        QFTrayIcon.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.PROCESSING_ICON)));
        final Scanner scannerInstance = new Scanner(cachedPages);
        if (cacheButton.getText().equals("Cache")) {
            /*
             * Reset file iterator control flag
             */
            PropertyPage.setFileIteratorStopper(false);
            QFTrayIcon.displayMessage("QuickFind", "Caching Started!", TrayIcon.MessageType.INFO);
            quickFindButton.setEnabled(false);
            isSearchDisabled = true;
            isCaching = true;
            cacheButton.setText("Stop");
            if (cacheComboBox.getSelectedItem().toString().equals(PropertyPage.ALL_ROOTS)) {
                scannerInstance.addCacheList(PropertyPage.CACHE_LIST);

                /*
                 * Cleans the cache directory before proceeding
                 */
                Utility.cacheCleaner(null, 1);
            } else {
                scannerInstance.addCache(PropertyPage.CACHE_LIST.get(cacheComboBox.getSelectedIndex() - 1).toString());
            }

            scannerInstance.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        if (isCaching) {
                            statusLabel.setText("Scanned Files: " + PropertyPage.getCachedFilesCount());
                        } else {
                            statusLabel.setText(PropertyPage.getCachedFilesCount() + " Items scanned in " + (Utility.getReadableElapsedIntervalInSeconds(System.currentTimeMillis() - cacheStartTime)) + " Secs");
                        }
                        return;
                    }
                    if ("DONE".equals(evt.getNewValue().toString())) {
                        quickFindButton.setEnabled(true);
                        isSearchDisabled = false;
                        isCaching = false;
                        cacheButton.setText("Cache");
                        statusLabel.setText(PropertyPage.getCachedFilesCount() + " Items scanned in " + (Utility.getReadableElapsedIntervalInSeconds(System.currentTimeMillis() - cacheStartTime)) + " Secs");
                        QFTrayIcon.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.SYSTEM_ICON)));
                        QFTrayIcon.displayMessage("QuickFind", "Caching Over!", TrayIcon.MessageType.INFO);
                    }
                }
            });

            /*
             * Scanner worker thread runs in background
             */
            scannerInstance.execute();
        } else if (scannerInstance != null) {   // caching process is already running so let stop it
            cacheButton.setEnabled(false);
            cacheButton.setText("Wait...");
            QFTrayIcon.displayMessage("QuickFind", "Interupting Cache!", TrayIcon.MessageType.INFO);

            SwingWorker worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    PropertyPage.setFileIteratorStopper(true);    //signaling scanner to stop caching immediately
                    /*
                     * Lets wait for all the threads which are involved in caching
                     */
                    if (scannerInstance.stopScanner()) {
                        scannerInstance.cancel(true); // now cancel worker thread
                    }
                    statusLabel.setText("Scanned Files: " + PropertyPage.getCachedFilesCount());
                    cacheButton.setText("Cache");
                    cacheButton.setEnabled(true);
                    return null;
                }
            };

            worker.addPropertyChangeListener(new PropertyChangeListener() {

                /*
                 * On successful completion reset all buttons
                 */
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("DONE".equals(evt.getNewValue().toString())) {
                        cacheButton.setEnabled(true);
                        quickFindButton.setEnabled(true);
                        cacheButton.setText("Cache");
                    }
                }
            });

            worker.execute();
        }
    }

    /*
     * Starts scanner on action performed
     */
    private void quickFindButtonActionPerformed(java.awt.event.ActionEvent evt) {
        startScan();
    }

    /*
     * Updates status message
     */
    private void setStatusMessage(final String statusMessage) {
        statusLabel.setText(statusMessage);
    }

    /*
     * Validates text input
     */
    private void processTextFieldInput(java.awt.event.KeyEvent evt) {
        String invalidKeyChar = "<>:/\\|";    //invalid characters for filename in most of the Operating Systems
        if (searchTextField.getText().startsWith("\"") && searchTextField.getText().endsWith("\"")) {
            return;    //allow all characters
        }
        if (invalidKeyChar.indexOf(evt.getKeyChar()) != -1) {
            evt.consume();
            System.out.println("\007");
            System.out.flush();
            Toolkit.getDefaultToolkit().beep();
            return;
        }
    }

    /*
     * Scans cached files
     */
    private void startScan() {
        /*
         * Wait until previous thread stops
         */
        if (isSearchScannerRunning) {
            JOptionPane.showMessageDialog(this, "Previous search request is yet to finish", "Please Wait", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!Utility.isRegExPattern(searchTextField.getText())) {
            JOptionPane.showMessageDialog(this, "To enable java pattern search it should be enclosed in \"\"", "Invalid pattern", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Utility.isMemorySufficient()) {
            /*
             * Aborts the scanning if available memory is not more than 1% of max memory
             */
            JOptionPane.showMessageDialog(this, "Out of memory! Please increase the heap space", "Insufficient Memory", JOptionPane.ERROR_MESSAGE);
            return;
        }
        /*
         * Reset controls and flags to begin fresh scan
         */
        PropertyPage.setFirstPush(true);
        PropertyPage.setCacheIteratorStopper(false);
        isSearchScannerRunning = true;
        isItemFound = false;
        clearOldSearchResultset();
        nextRowsButton.setEnabled(false);
        previousRowsButton.setEnabled(false);
        topListIndex = 0;  //new search so set back to zero
        doScan();
    }


    /*
     * Starts searching thread
     */
    private void doScan() {
        Search search = new Search(searchTextField.getText(), cachedPages);
        final long scanStartTime = System.currentTimeMillis();
        if (searchComboBox.getSelectedItem().toString().equals(PropertyPage.ALL_ROOTS)) {
            search.addCacheList(PropertyPage.CACHE_LIST);
        } else {
            search.addCache(PropertyPage.CACHE_LIST.get(searchComboBox.getSelectedIndex() - 1).toString());
        }

        search.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    if (isSearchScannerRunning) {
                        setStatusMessage(PropertyPage.getSearchedFilesCount() + " Items found.");
                    } else {
                        setStatusMessage(PropertyPage.getSearchedFilesCount() + " Items found in " + Utility.getReadableElapsedIntervalInSeconds(System.currentTimeMillis() - scanStartTime) + " Secs");
                    }
                    if (PropertyPage.isFirstPush()) {
                        if (PropertyPage.getSearchedFilesCount() > RESULT_SET_LIMIT) {
                            PropertyPage.setFirstPush(false);
                            isItemFound = true;
                            displayTopList();
                        }
                    }
                    return;
                }
                if ("DONE".equals(evt.getNewValue().toString())) {
                    /*
                     * If user request for new search then clear current result
                     */
                    if (PropertyPage.getCacheIteratorStopper()) {
                        clearOldSearchResultset();
                    }
                    PropertyPage.setCacheIteratorStopper(false);    // Tries to stop cache iterator
                    isSearchScannerRunning = false;
                    if (PropertyPage.isFirstPush()) {
                        if (PropertyPage.getSearchedFilesCount() > 0) {
                            isItemFound = true;
                        }
                        displayTopList();
                    }
                    setStatusMessage(PropertyPage.getSearchedFilesCount() + " Items found in " + Utility.getReadableElapsedIntervalInSeconds(System.currentTimeMillis() - scanStartTime) + " Secs");
                    if (((int) (PropertyPage.getSearchedFilesCount() / RESULT_SET_LIMIT) > topListIndex)) {
                        nextRowsButton.setEnabled(true);
                    } else {
                        nextRowsButton.setEnabled(false);
                    }
                    previousRowsButton.setEnabled(false);
                }
            }
        });
        search.execute();
        search = null;
    }

    /*
     * Resets searched filescount
     */
    private synchronized void clearOldSearchResultset() {
        PropertyPage.resetSearchedFilesCount();
    }

    /*
     * Removes all the elements from result table
     */
    private void clearResultTable() {
        DefaultTableModel dtmodel = (DefaultTableModel) resultTable.getModel();
        int tableRowCount = dtmodel.getRowCount();
        for (int i = tableRowCount - 1; i >= 0; i--) {
            dtmodel.removeRow(i);
        }
        if (fileIconCache.size() > 1000) {
            fileIconCache.clear();      //free the Icon cache. let's build once again
            fileIconStringCache.clear();//free the IconString cache. let's build once again
        }
        dtmodel = null;
    }

    /*
     * Displays search result based on the resultSetLimit
     */
    private void displayTopList() {
        int currentTopElements = topListIndex * RESULT_SET_LIMIT;
        StringBuilder filePath = new StringBuilder(1024);
        HashMap<String, String> hashMap = new HashMap<String, String>(RESULT_SET_LIMIT);
        clearResultTable();
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        Icon icon = null;

        if (!isItemFound) {
            return;
        }

        ArrayList<String> cacheLevelResultSet = new ArrayList<String>(RESULT_SET_LIMIT);
        RandomAccessFile rawDataReader = null;
        try {
            rawDataReader = new RandomAccessFile(new File(PropertyPage.getCacheDirectory() + PropertyPage.SEARCH_RAW_FILE + PropertyPage.RAW_FILE_EXTENSION), "r");
            String rawPath;
            rawDataReader.seek(Utility.getSearchResultIndex(currentTopElements));
            while ((rawPath = rawDataReader.readLine()) != null) {
                ++currentTopElements;
                cacheLevelResultSet.add(rawPath);
                if (((topListIndex + 1) * RESULT_SET_LIMIT) <= currentTopElements) {
                    break;
                }
            }
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        } finally {
            try {
                if (rawDataReader != null) {
                    rawDataReader.close();
                }
            } catch (IOException iOException) {
                LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
            }
        }

        int cacheLevelResultSetCount = cacheLevelResultSet.size();

        for (int k = 0; k < cacheLevelResultSetCount; k++) {
            String rawPath = cacheLevelResultSet.get(k).toString();
            hashMap.put(rawPath.substring(0, rawPath.indexOf(PropertyPage.FILE_SEPARATOR)), null);
        }

        BufferedReader symbolReader = null;
        try {
            symbolReader = new BufferedReader(new FileReader((new File(PropertyPage.getCacheDirectory() + PropertyPage.SEARCH_SYMBOL_FILE + PropertyPage.SYMBOLS_FILE_EXTENSION))));
            while (symbolReader.ready()) {
                String symbolLine = symbolReader.readLine();
                String key = symbolLine.substring(0, symbolLine.indexOf(PropertyPage.FILE_SEPARATOR));
                if (hashMap.containsKey(key)) {
                    String value = symbolLine.substring(symbolLine.indexOf(PropertyPage.FILE_SEPARATOR) + 1, symbolLine.length());
                    hashMap.put(key, value);
                }
            }
            symbolReader.close();
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }

        for (int j = 0; j < cacheLevelResultSetCount; j++) {
            filePath.setLength(0);
            String rawPath = (cacheLevelResultSet.get(j).toString());
            StringTokenizer stringTokenizer = new StringTokenizer(rawPath, PropertyPage.FILE_SEPARATOR);

            if (stringTokenizer.countTokens() == 5) {
                String directoryKey = stringTokenizer.nextToken();
                String fileName = stringTokenizer.nextToken();
                String fileSize = stringTokenizer.nextToken();
                String lastModifiedDate = stringTokenizer.nextToken();
                int fileProperty = Integer.valueOf(stringTokenizer.nextToken());

                filePath.append(hashMap.get(directoryKey).toString()).append(PropertyPage.FILE_SEPARATOR).append(fileName);
                String readableFileSize = "";
                if (fileSize != null && !fileSize.equals("0")) {
                    readableFileSize = fileSize;
                }

                File fileReference = new File(filePath.toString());
                if (fileReference.exists()) {
                    if (Utility.isDirectory(fileProperty)) {
                        icon = PropertyPage.directoryIcon;
                    } else {
                        String fileext = Utility.getFileExtension(filePath.toString());
                        if (!fileIconCache.containsKey(fileext)) {
                            Icon tmpIcon = fileSystemView.getSystemIcon(fileReference);
                            if (fileIconStringCache.containsKey(tmpIcon.toString())) {
                                fileext = fileIconStringCache.get(tmpIcon.toString());
                            } else {
                                fileIconCache.put(fileext, tmpIcon);
                                fileIconStringCache.put(tmpIcon.toString(), fileext);
                            }
                            tmpIcon = null;
                        }
                        icon = (Icon) fileIconCache.get(fileext);
                    }
                }

                if (icon == null) {
                    icon = deletedFileIcon;
                }

                Object tableRow[] = new Object[2];
                tableRow[0] = new IconAndText(icon, fileName, filePath.toString());
                tableRow[1] = readableFileSize;

                ((DefaultTableModel) resultTable.getModel()).addRow(tableRow);

                tableRow[0] = null;
                tableRow[1] = null;
                readableFileSize = null;
                icon = null;
                fileReference = null;
            }
        }

        resultSetIndexLabel.setText(((((topListIndex + 1) * RESULT_SET_LIMIT) - RESULT_SET_LIMIT) + 1) + "-" + currentTopElements);
        resultTableScrollPane.getVerticalScrollBar().setValue(resultTableScrollPane.getVerticalScrollBar().getMinimum());

        if (cacheLevelResultSet != null) {
            cacheLevelResultSet.clear();
            cacheLevelResultSet = null;
        }

        if (hashMap != null) {
            hashMap.clear();
            hashMap = null;
        }
        System.gc();  // reminding JVM to Garbage Collect
    }

    /*
     * Selects all the rows in the result table
     */
    private void selectAllMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (resultTable.getModel().getRowCount() > 0) {
            resultTable.selectAll();
        }
    }

    /*
     * Displays QuickFind about window
     */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        new QuickFindAbout(this, true).setVisible(true);
    }

    /*
     * Terminates application
     */
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
        System.exit(0);
    }

    /*
     * Displays next top elements based on the resultSet Limit
     */
    private void nextRowsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ++topListIndex;
        if (((int) (PropertyPage.getSearchedFilesCount() / RESULT_SET_LIMIT) >= topListIndex)) {
            displayTopList();
        }
        if (((int) (PropertyPage.getSearchedFilesCount() / RESULT_SET_LIMIT) == topListIndex)) {
            nextRowsButton.setEnabled(false);
        }
        previousRowsButton.setEnabled(true);
    }

    /*
     * Displays previous top elements based on the resultSet Limit
     */
    private void previousRowsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        --topListIndex;
        displayTopList();
        if (topListIndex == 0) {
            previousRowsButton.setEnabled(false);
            nextRowsButton.setEnabled(true);
        }
        if (((int) (PropertyPage.getSearchedFilesCount() / RESULT_SET_LIMIT) != topListIndex)) {
            nextRowsButton.setEnabled(true);
        }
    }

    /*
     * Opens the File/Directory Location
     */
    private void openLocationPopUpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String selectedPath = ((IconAndText) resultTable.getModel().getValueAt(resultTable.getSelectedRow(), 0)).getFilePath();
            File selectedFile = new File(selectedPath);
            if (selectedFile.exists() && selectedFile.canRead()) {
                if (selectedFile.isDirectory()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(selectedFile);
                    }
                } else if (selectedFile.isFile()) {
                    File selectedFileDirectory = new File(selectedPath.substring(0, selectedPath.lastIndexOf(PropertyPage.FILE_SEPARATOR)));
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(selectedFileDirectory);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Cannot Open!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Could not open  this File/Directory Location", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SecurityException securityException) {
            LOGGER.log(Level.SEVERE, "Insufficient permission", securityException);
            JOptionPane.showMessageDialog(QuickFind.this, "Could not open  this File/Directory Location", "Insufficient File Permission", JOptionPane.ERROR_MESSAGE);
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
            JOptionPane.showMessageDialog(QuickFind.this, "Could not open  this File/Directory Location", "Interrupted", JOptionPane.ERROR_MESSAGE);
        } catch (Exception unknownException) {
            LOGGER.log(Level.SEVERE, "Unknown Error", unknownException);
            JOptionPane.showMessageDialog(QuickFind.this, "Unknown Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Executes the selected File
     */
    private void openPopUpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String selectedPath = ((IconAndText) resultTable.getModel().getValueAt(resultTable.getSelectedRow(), 0)).getFilePath();
            File selectedFile = new File(selectedPath);
            if (selectedFile.exists()) {
                if (selectedFile.canExecute() && selectedFile.canRead() && selectedFile.isFile()) {
                    if (PropertyPage.isWindows()) {
                        Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + selectedPath);
                    } else {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(selectedFile);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "You don't have enough permission to open this File", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Could not open this File", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SecurityException securityException) {
            LOGGER.log(Level.SEVERE, "Insufficient permission", securityException);
            JOptionPane.showMessageDialog(QuickFind.this, "Could not open  this File", "Insufficient File Permission", JOptionPane.ERROR_MESSAGE);
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
            JOptionPane.showMessageDialog(QuickFind.this, "Could not open  this File", "Interrupted", JOptionPane.ERROR_MESSAGE);
        } catch (Exception unknownException) {
            LOGGER.log(Level.SEVERE, "Unknown Error", unknownException);
            JOptionPane.showMessageDialog(QuickFind.this, "Unknown Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Renames the selected File for the new name
     */
    private void renamePopUpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String selectedPath = ((IconAndText) resultTable.getModel().getValueAt(resultTable.getSelectedRow(), 0)).getFilePath();
            String userInput = "";
            File selectedFile = new File(selectedPath);
            if (selectedFile.exists()) {
                userInput = JOptionPane.showInputDialog(this, "Enter the new name for " + selectedPath.substring(selectedPath.lastIndexOf(PropertyPage.FILE_SEPARATOR) + 1, selectedPath.length()), "FileName Required",
                        JOptionPane.QUESTION_MESSAGE);
                if (userInput == null) {
                    return;
                }
                if (selectedFile.renameTo(new File((selectedPath.substring(0, selectedPath.lastIndexOf(PropertyPage.FILE_SEPARATOR))) + userInput))) {
                    JOptionPane.showMessageDialog(this, "File has been renamed successfully", "Completed", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Could not rename this File", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SecurityException securityException) {
            LOGGER.log(Level.SEVERE, "Insufficient permission", securityException);
            JOptionPane.showMessageDialog(QuickFind.this, "Could not rename this File/Directory", "Insufficient File Permission", JOptionPane.ERROR_MESSAGE);
        } catch (Exception unknownException) {
            LOGGER.log(Level.SEVERE, "Unknown Error", unknownException);
            JOptionPane.showMessageDialog(QuickFind.this, "Unknown Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Resets the old search result
     */
    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        clearOldSearchResultset();
        setStatusMessage(PropertyPage.getSearchedFilesCount() + " Items found.");
        clearResultTable();
        resultSetIndexLabel.setText("0-0");
        searchTextField.setText("");
        nextRowsButton.setEnabled(false);
        previousRowsButton.setEnabled(false);
    }

    /*
     * On right click enables/disables open PopUpMenuItem
     */
    private void mousePressedOnResultTable(java.awt.event.MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            String selectedPath = getIconAndText(evt).getFilePath();
            File selectedFile = new File(selectedPath);
            if (selectedFile.exists()) {
                if (selectedFile.isDirectory()) {
                    openPopUpMenuItem.setEnabled(false);
                } else {
                    openPopUpMenuItem.setEnabled(true);
                }
            }
        }
    }

    /*
     * Exctracts IconAndText object from the selected row
     */
    private IconAndText getIconAndText(MouseEvent evt) {
        JTable sourceTable = (JTable) evt.getSource();
        int selectedRow = sourceTable.rowAtPoint(evt.getPoint());
        int selectedColumn = sourceTable.columnAtPoint(evt.getPoint());
        if (sourceTable.getModel().getColumnCount() >= selectedColumn && sourceTable.getModel().getRowCount() >= selectedRow && selectedRow != -1) {
            if ((sourceTable.getModel().getValueAt(selectedRow, selectedColumn)) instanceof IconAndText) {
                IconAndText iconAndText = (IconAndText) (sourceTable.getModel().getValueAt(selectedRow, selectedColumn));
                sourceTable = null;
                return iconAndText;
            }
        }
        sourceTable = null;
        return null;
    }

    /*
     * Displays tooltip on mouse move
     */
    private void mouseMovedOnResultTable(java.awt.event.MouseEvent evt) {
        IconAndText iconAndText = getIconAndText(evt);
        if (iconAndText != null) {
            resultTable.setToolTipText("<html><font color='black'>" + iconAndText.getFileName() + "<br>" + iconAndText.getFilePath() + "<br>" + "</font></html>");
        }
        iconAndText = null;
    }

    /*
     * On mouse double click opens the File/Directory Or executes File
     */
    private void mouseClickedOnResultTable(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            try {
                String selectedPath = ((IconAndText) resultTable.getModel().getValueAt(resultTable.getSelectedRow(), 0)).getFilePath();
                File selectedFile = new File(selectedPath);
                if (selectedFile.exists()) {
                    if (!selectedFile.canExecute() || !selectedFile.canRead()) {
                        JOptionPane.showMessageDialog(rootPane, "You Don't have enough permission to open this File");
                        return;
                    }
                    if (selectedFile.isDirectory()) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(selectedFile);
                        }
                    } else if (selectedFile.isFile()) {
                        if (PropertyPage.isWindows()) {
                            Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + selectedPath);
                        } else if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(selectedFile);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Could not open this File", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SecurityException securityException) {
                LOGGER.log(Level.SEVERE, "Insufficient permission", securityException);
                JOptionPane.showMessageDialog(QuickFind.this, "Could not open  this File/Directory Location", "Insufficient File Permission", JOptionPane.ERROR_MESSAGE);
            } catch (IOException iOException) {
                LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
                JOptionPane.showMessageDialog(QuickFind.this, "Could not open  this File/Directory Location", "Interrupted", JOptionPane.ERROR_MESSAGE);
            } catch (Exception unknownException) {
                LOGGER.log(Level.SEVERE, "Unknown Error", unknownException);
                JOptionPane.showMessageDialog(QuickFind.this, "Unknown Error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     * Calls copy path
     */
    private void copyPathMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        doCopyPath();
    }

    /*
     * Copies the selected File/Directory path to System Clipboard
     */
    private void doCopyPath() {
        final int selectedRowsIndices[] = resultTable.getSelectedRows();
        StringBuffer filePath = new StringBuffer("");
        if (selectedRowsIndices.length >= 1) {
            for (int i = 0; i < selectedRowsIndices.length; i++) {
                if (filePath.length() > 0) {
                    filePath.append("\n");
                }
                filePath.append(((IconAndText) resultTable.getModel().getValueAt(selectedRowsIndices[i], 0)).getFilePath());
            }
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(filePath.toString()), null);
            filePath = null;
        } else {
            JOptionPane.showMessageDialog(this, "Nothing is selected to copy!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Calls copy path method
     */
    private void copyPathPopUpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        doCopyPath();
    }

    /*
     * Initializes the application
     * Loads all root names to combo box
     * On resultTable mouse release shows PopUp MenuBar
     */
    private void initApplication() {
        cacheComboBox.addItem(PropertyPage.ALL_ROOTS);
        searchComboBox.addItem(PropertyPage.ALL_ROOTS);
        for (int i = 0; i < PropertyPage.CACHE_LIST.size(); i++) {
            cacheComboBox.addItem(PropertyPage.CACHE_LIST.get(i));
            searchComboBox.addItem(PropertyPage.CACHE_LIST.get(i));
        }

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(PropertyPage.IMAGES_PATH + PropertyPage.SYSTEM_ICON)));
        nextRowsButton.setEnabled(false);
        previousRowsButton.setEnabled(false);

        this.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                searchTextField.requestFocusInWindow();
            }
        });

        resultTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                triggerPopUpMenu(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                triggerPopUpMenu(e);
            }
        });
    }

    /*
     * Shows popUpMenu
     */
    private void triggerPopUpMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            int column = source.columnAtPoint(e.getPoint());

            if (!source.isRowSelected(row)) {
                source.changeSelection(row, column, false, false);
            }
            popUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}

/*
 * Merges Icon and Text
 */
class IconAndText {

    private String fileName;
    private String filePath;
    private Icon fileIcon;
    static final String startHtmlTag = "<html>";
    static final String intermediateHtmlTag = "<br> <font color='#A0A0A0' >";
    static final String endHtmlTag = "</font></html>";

    public IconAndText(Icon _icon, String _fileName, String _filePath) {
        fileIcon = _icon;
        fileName = _fileName;
        filePath = _filePath;
    }

    Icon getIcon() {
        return fileIcon;
    }

    String getFileName() {
        return fileName;
    }

    String getFilePath() {
        return filePath;
    }
}

/*
 * Custom ImageRenderer to display Icon and Text into the Table Cell
 */
class ImageRenderer extends DefaultTableCellRenderer {

    @Override
    protected void setValue(Object value) {
        if (value instanceof IconAndText) {
            if (value != null) {
                setIcon(((IconAndText) value).getIcon());
                setText(IconAndText.startHtmlTag + ((IconAndText) value).getFileName() + IconAndText.intermediateHtmlTag + ((IconAndText) value).getFilePath() + IconAndText.endHtmlTag);
                setHorizontalTextPosition(SwingConstants.RIGHT);
                setVerticalTextPosition(SwingConstants.CENTER);
                setHorizontalAlignment(SwingConstants.LEFT);
                setVerticalAlignment(SwingConstants.CENTER);
                setIconTextGap(5);
            } else {
                setText("");
                setIcon(null);
            }
        } else {
            super.setValue(value);
        }
    }
}

/*
 * InMemory deleted File Icon
 */
class DeletedFileIcon implements Icon {

    private int width = 16;
    private int height = 16;
    private BasicStroke basicStroke = new BasicStroke(4);

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D graphics2d = (Graphics2D) g.create();

        graphics2d.setColor(Color.WHITE);
        graphics2d.fillRect(x + 1, y + 1, width - 2, height - 2);
        graphics2d.setColor(Color.RED);
        graphics2d.setStroke(basicStroke);
        graphics2d.drawLine(x + 12, y + 12, x + width - 12, y + height - 12);
        graphics2d.drawLine(x + 12, y + height - 12, x + width - 12, y + 12);

        graphics2d.dispose();
        graphics2d = null;
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }
}