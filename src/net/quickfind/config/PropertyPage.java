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

package net.quickfind.config;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

/*
 * PropertyPage.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
public class PropertyPage {

    /*
     * Counters and Flags used by multiple threads
     */
    private volatile static boolean isFirstPush = true;
    private volatile static long cachedFilesCount = 0L;
    private volatile static long searchedFilesCount = 0L;
    private volatile static int fileIteratorThreadAliveCount = 0;
    private volatile static boolean fileIteratorStopper = false;
    private volatile static boolean cacheIteratorStopper = false;
    private static boolean isSystemStartUp = false;

    /*
     * File formats and File Names
     */
    public static final String IMAGES_PATH = "/img/";
    public static final String SYSTEM_ICON = "system.png";
    public static final String NEXT_ROWS_ICON = "next.png";
    public static final String PREVIOUS_ROWS_ICON = "previous.png";
    public static final String PROCESSING_ICON = "small.png";
    public static final String SPLASH_ICON = "splash.png";
    public static final String DONATE_ICON = "donate.gif";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String FILE_EXTENSION_SEPARATOR = ".";
    public static final String COMPRESSED_FILE_EXTENSION = ".dat";
    public static final String RAW_FILE_EXTENSION = ".raw";
    public static final String SYMBOLS_FILE_EXTENSION = ".syb";
    public static final String INDEX_FILE_EXTENSION = ".ind";
    public static final String CONFIG_FILE_EXTENSION = ".xml";
    public static final String SEARCH_RAW_FILE_INDEX = "searchResult";
    public static final String SEARCH_RAW_FILE = "searchResult";
    public static final String SEARCH_SYMBOL_FILE = "searchResult";
    public static final String CACHE_COLLECTION_CONFIG_FILE = "CacheConfig";
    public static final String ALL_ROOTS = "All Roots";
    public static final ArrayList<String> CACHE_LIST = new ArrayList<String>();

    /*
     * GUI Constants
     */
    public static final Font DEFAULT_RESULT_TABLE_FONT = new Font("Tahoma", 0, 11);
    public static final Font DEFAULT_BUTTON_FONT = new Font("Tahoma", 0, 12);
    public static final Font DEFAULT_TEXT_FIELD_FONT = new Font("Tahoma", 0, 12);
    public static final int DEFAULT_RESULT_TABLE_ROW_HEIGHT = 30;
    public static final int DEFAULT_RESULT_TABLE_HEADER_HEIGHT = 28;
    public static final String DEFAULT_DATETIME_FORMAT = "dd MMM yyyy hh:mm a";
    public static final int DEFAULT_CACHEMANAGER_TABLE_ROW_HEIGHT = 20;
    public static final Font DEFAULT_CACHEMANAGER_TABLE_FONT = new Font("Tahoma", 0, 11);
    public static final int DEFAULT_CACHEMANAGER_TABLE_HEADER_HEIGHT = 28;

    /*
     * Application Information
     */
    public static final String COPY_RIGHT = "© 2013 Vasantkumar Mulage";
    public static final String PRODUCT_VERSION = "<html>  <font size='4'>1.1.0.3</font> <font size='2'>BETA</font> </html>";
    public static final String WEBSITE = "<html><a href=\\'http://quickfind.sf.net/'>http://quickfind.sf.net/</a></html>";
    public static Icon directoryIcon;
    public static Icon defaultFileIcon;
    private static String cacheDirectory;
    private static String currentOS = null;
    private static int SEARCH_RESULT_LIMIT = 1000;
    private static boolean LOAD_DEFAULTS = false;
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(PropertyPage.class.getName());

    /*
     * Loading directory icon from a temp directory
     */
    static {
        String directoryPath = System.getProperty("java.io.tmpdir");
        directoryIcon = FileSystemView.getFileSystemView().getSystemIcon(new File(directoryPath));
    }

    /*
     * Loading file icon from a temp file
     */
    static {
        try {
            /*
             * creates a temp file
             */
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            File tempFile = File.createTempFile("temp_" + dateFormat.format(new Date()), null);
            defaultFileIcon = FileSystemView.getFileSystemView().getSystemIcon(tempFile);
            tempFile.delete();
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        } catch (Exception unknownException) {
            LOGGER.log(Level.SEVERE, "Unknown error", unknownException);
        }
    }

    /*
     * @return  the firstPush
     */
    public static boolean isFirstPush() {
        return isFirstPush;
    }

    /*
     * @param   aisFirstPush the isFirstPush to set
     */
    public static void setFirstPush(boolean aisFirstPush) {
        isFirstPush = aisFirstPush;
    }

    /*
     * @return  the SEARCH_RESULT_LIMIT
     */
    public static int getSearchResultLimit() {
        return SEARCH_RESULT_LIMIT;
    }

    /*
     * updates the SEARCH_RESULT_LIMIT
     */
    public static void updateSearchResultLimit() {
        SEARCH_RESULT_LIMIT = Preference.getSearchResultLimit();
    }

    /*
     * @return  the LOAD_DEFAULTS
     */
    public static boolean getLoadDefaults() {
        return LOAD_DEFAULTS;
    }

    /*
     * updates  the LOAD_DEFAULTS
     */
    public static void updateLoadDefaults() {
        LOAD_DEFAULTS = Preference.isLoadDefaults();
    }

    /**
     * @return the isSystemStartUp
     */
    public static boolean isSystemStartUp() {
        return isSystemStartUp;
    }

    /**
     * @param aIsSystemStartUp the isSystemStartUp to set
     */
    public static void setSystemStartUp(boolean aIsSystemStartUp) {
        isSystemStartUp = aIsSystemStartUp;
    }

    public PropertyPage() {
    }

    /*
     * @return  fileIteratorStopper
     */
    public static boolean getFileIteratorStopper() {
        return fileIteratorStopper;
    }

    /*
     * @param   stopperValue to start/stop fileIterator
     */
    public static void setFileIteratorStopper(boolean stopperValue) {
        fileIteratorStopper = stopperValue;
    }

    /*
     * @return  cacheIteratorStopper
     */
    public static boolean getCacheIteratorStopper() {
        return cacheIteratorStopper;
    }

    /*
     * @param   stopperValue to start/stop cacheIterator
     */
    public static void setCacheIteratorStopper(boolean stopperValue) {
        cacheIteratorStopper = stopperValue;
    }

    /*
     * Increments cahedFilesCount
     */
    public static synchronized void incrementCachedFilesCount() {
        ++cachedFilesCount;
    }

    /*
     * @return  cachedFilesCount
     */
    public static synchronized long getCachedFilesCount() {
        return cachedFilesCount;
    }

    /*
     * Resets cachedFilesCount
     */
    public static synchronized void resetCachedFilesCount() {
        cachedFilesCount = 0L;
    }

    /*
     * @return  fileiteraor alive thread count
     */
    public static synchronized int getIteratorThreadAliveCount() {
        return fileIteratorThreadAliveCount;
    }

    /*
     * Increments fileiterator alive thread count
     */
    public static synchronized void incrementIteratorThreadAliveCount() {
        ++fileIteratorThreadAliveCount;
    }

    /*
     * Decrements fileiteraor alive thread count
     */
    public static synchronized void decrementIteratorThreadAliveCount() {
        if (fileIteratorThreadAliveCount > 0) {
            --fileIteratorThreadAliveCount;
        }
    }

    /*
     * @return  searchedFilesCount
     */
    public static synchronized long getSearchedFilesCount() {
        return searchedFilesCount;
    }

    /*
     * Increments searchedFilesCount
     */
    public static synchronized void incrementSearchedFilesCount() {
        ++searchedFilesCount;
    }

    /*
     * Resets searchedFilesCount to 0
     */
    public static synchronized void resetSearchedFilesCount() {
        searchedFilesCount = 0L;
    }

    /*
     * @return  cacheDirectory
     */
    public static String getCacheDirectory() {
        return cacheDirectory;
    }

    /*
     * @param   set cache directory path
     */
    public static void setCacheDirectory(String path) {
        cacheDirectory = path;
    }

    /*
     * @return  current Operating System
     */
    private static String getOsName() {
        if (currentOS == null) {
            currentOS = System.getProperty("os.name").toLowerCase();
        }
        return currentOS;
    }

    /*
     * @return  whether current Operating System is Windows OS
     */
    public static boolean isWindows() {
        return getOsName().contains("windows");
    }

    /*
     * @return  whether current Operating System is Unix OS
     */
    public static boolean isUnix() {
        return getOsName().contains("unix");
    }

    /*
     * @return  whether current Operating System is Linux OS
     */
    public static boolean isLinux() {
        return getOsName().contains("linux");
    }

    /*
     * @return  current Operating System
     */
    public static String getSystemName() {
        return System.getenv("COMPUTERNAME");
    }
}
