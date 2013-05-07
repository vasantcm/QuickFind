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

package net.quickfind.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.quickfind.config.PropertyPage;

/*
 * Utility.java
 * @author  Copyright (C) 2012 Vasantkumar Mulage
 */
public class Utility {

    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(Utility.class.getName());

    /*
     * Finds file size in human readable format
     * @param   absolute filepath
     * @return  file size in human readable format
     */
    public static String getFileSize(final String filePath) {
        String formattedFileSize = "0 B";
        File fileReference = new File(filePath);
        if (fileReference.exists()) {
            long fileSizeInBytes = fileReference.length();
            if (fileSizeInBytes < 1024) {
                return fileSizeInBytes + " B";
            }
            int unitIndex = (int) (Math.log(fileSizeInBytes) / Math.log(1024));
            String computedUnit = "KMGT".charAt(unitIndex - 1) + "";
            formattedFileSize = String.format("%.1f %sB", fileSizeInBytes / Math.pow(1024, unitIndex), computedUnit);
        }
        return formattedFileSize;
    }

    /*
     * Finds file extension from the given file path
     * @param   absolute filePath
     * @return  extension of the file or null
     */
    public static String getFileExtension(final String filePath) {
        if ((filePath.lastIndexOf(PropertyPage.FILE_SEPARATOR) < filePath.lastIndexOf(PropertyPage.FILE_EXTENSION_SEPARATOR))) {
            return (filePath.substring(filePath.lastIndexOf(PropertyPage.FILE_EXTENSION_SEPARATOR) + 1).toLowerCase());
        }
        return null;
    }

    /*
     * Finds last modified date of the file/directory
     * @param   absolute filepath
     * @return  last modified date
     */
    public static long getLastModifiedDate(final String filePath) {
        return (new File(filePath)).lastModified();
    }

    /*
     * Converts bitmask into file property code
     * @param   bitmask
     * @return  file property in "RWEHD" format
     */
    public static String getFileProperties(final int bitMask) {
        char bits[] = Integer.toBinaryString(bitMask).toCharArray();
        char codes[] = "RWEHD".toCharArray();
        if (bitMask == 0) {
            return "XXXXX";
        }
        for (int i = 0; i < codes.length; i++) {
            if (bits.length > i) {
                if (bits[i] == '0') {
                    codes[i] = 'X';
                }
            } else {
                codes[i] = 'X';
            }
        }
        return String.valueOf(codes, 0, codes.length);
    }

    /*
     * Finds bitmask of the file/directory property
     * @param   absolute filepath
     * @return  bitmask
     */
    public static int getFilePropertiesMask(final String filePath) {
        int fileBitMask = 0;
        File fileReference = new File(filePath);
        if (fileReference.exists()) {
            if (fileReference.canRead()) {
                fileBitMask = fileBitMask | 1;
            }
            if (fileReference.canWrite()) {
                fileBitMask = fileBitMask | 2;
            }
            if (fileReference.canExecute()) {
                fileBitMask = fileBitMask | 4;
            }
            if (fileReference.isHidden()) {
                fileBitMask = fileBitMask | 8;
            }
            if (fileReference.isDirectory()) {
                fileBitMask = fileBitMask | 16;
            }
        }
        return fileBitMask;
    }

    /*
     * Checks given bitmask for read flag
     * @param   file property bitMask
     * @return  true if readable
     */
    public static boolean canRead(int bitMask) {
        return ((bitMask & 1) == 1 ? true : false);
    }

    /*
     * Checks given bitmask for write flag
     * @param   file property bitMask
     * @return  true if writable
     */
    public static boolean canWrite(int bitMask) {
        return ((bitMask & 2) == 2 ? true : false);
    }

    /*
     * Checks given bitmask for execute flag
     * @param   file property bitMask
     * @return  true if executable
     */
    public static boolean canExecute(int bitMask) {
        return ((bitMask & 4) == 4 ? true : false);
    }

    /*
     * Checks given bitmask for hidden flag
     * @param   file property bitMask
     * @return  true if hidden flag is set
     */
    public static boolean isHidden(int bitMask) {
        return ((bitMask & 8) == 8 ? true : false);
    }

    /*
     * Checks given bitmask for directory flag
     * @param   file property bitMask
     * @return  true if directory flag is set
     */
    public static boolean isDirectory(int bitMask) {
        return ((bitMask & 16) == 16 ? true : false);
    }

    /*
     * Finds the FileName from the given absolute filePath
     * @param   absolute file path
     * @return  filename
     */
    public static String getFileName(final String filePath) {
        return filePath.substring(filePath.lastIndexOf(PropertyPage.FILE_SEPARATOR) + 1, filePath.length());
    }

    /*
     * Finds the given directory size
     * @param   absolute directory path
     * @return  directory size in bytes
     */
    public static long getDirectorySize(final File directory) {
        long directorySize = 0L;
        File[] childFiles = directory.listFiles();
        if (childFiles != null) {
            for (int i = 0; i < childFiles.length; i++) {
                if (childFiles[i].isDirectory()) {
                    directorySize += getDirectorySize(childFiles[i]);
                } else {
                    directorySize += childFiles[i].length();
                }
            }
        }
        return directorySize;
    }

    /*
     * Converts the elapsed time into readable format
     * @param   elapsed Time in Millis
     * @return  time in HH:MM:SS.mmm format
     */
    public static String getReadableElapsedInterval(final long elapsedTimeInMillis) {
        final long hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeInMillis);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeInMillis - TimeUnit.HOURS.toMillis(hours));
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeInMillis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));
        final long milliSeconds = TimeUnit.MILLISECONDS.toMillis(elapsedTimeInMillis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds));
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliSeconds);
    }

    /*
     * Converts elapsed time to seconds and returns in readable format
     * @param   elapsed Time in Millis
     * @return  time in SS.mmm format
     */
    public static String getReadableElapsedIntervalInSeconds(final long elapsedTimInMillis) {
        final long hours = TimeUnit.MILLISECONDS.toHours(elapsedTimInMillis);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimInMillis - TimeUnit.HOURS.toMillis(hours));
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimInMillis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes)) + (hours * 60 * 60) + (minutes * 60);
        final long milliSeconds = TimeUnit.MILLISECONDS.toMillis(elapsedTimInMillis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(elapsedTimInMillis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes))));
        return String.format("%02d.%03d", seconds, milliSeconds);
    }

    /*
     * Deletes old cache files
     * @param   cacheName->encoded name of the cache    cleanType->type of the files to be deleted
     */
    public static void cacheCleaner(String cacheName, int cleanType) {
        File absolutePath = new File(PropertyPage.getCacheDirectory());
        if (!absolutePath.isDirectory()) {
            absolutePath.mkdirs();
            return;
        }
        File allFiles[] = absolutePath.listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            String fileName = allFiles[i].getName();
            if (cleanType == 1) {     // cleans all cache files
                if (fileName.endsWith(PropertyPage.COMPRESSED_FILE_EXTENSION)
                        || fileName.endsWith(PropertyPage.RAW_FILE_EXTENSION)
                        || fileName.endsWith(PropertyPage.SYMBOLS_FILE_EXTENSION)
                        || fileName.endsWith(PropertyPage.INDEX_FILE_EXTENSION)
                        || fileName.endsWith(PropertyPage.CONFIG_FILE_EXTENSION)) {
                    allFiles[i].delete();
                }
            } else if (cleanType == 2) {  //cleans all cache files except compressed and configuration
                if (fileName.endsWith(PropertyPage.SYMBOLS_FILE_EXTENSION)
                        || fileName.endsWith(PropertyPage.RAW_FILE_EXTENSION)
                        || fileName.endsWith(PropertyPage.INDEX_FILE_EXTENSION)) {
                    allFiles[i].delete();
                }
            } else if (cleanType == 3 && cacheName != null) { //cleans all cache files begining with cacheName
                if (fileName.startsWith(cacheName)) {
                    allFiles[i].delete();
                }
            }
        }
        absolutePath = null;
    }

    /*
     * Finds index of the search raw file for the given line number
     * @param   line number
     * @return  index number
     */
    public static long getSearchResultIndex(int lineNumber) {
        try {
            BufferedReader rawFileIndexBufferedReader = null;
            rawFileIndexBufferedReader = new BufferedReader(new FileReader(new File(PropertyPage.getCacheDirectory() + PropertyPage.SEARCH_RAW_FILE_INDEX + PropertyPage.INDEX_FILE_EXTENSION)));
            String indexLine;
            while ((indexLine = rawFileIndexBufferedReader.readLine()) != null) {
                String tokens[] = indexLine.split("=");
                if (tokens[0].equals(Integer.toString(lineNumber))) {
                    return Long.valueOf(tokens[1]);
                }
            }
        } catch (Exception iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
        return 0L;
    }

    /*
     * Checks whether available memory is more than 1% of max memory
     * @return  true if more than 1% memory is available
     */
    public static boolean isMemorySufficient() {
        final long maxMemory = ((Runtime.getRuntime().maxMemory() / 1024) / 1024);
        final long freeMemory = ((Runtime.getRuntime().freeMemory() / 1024) / 1024);
        if ((freeMemory) < ((1 * maxMemory) / 100)) {
            return false;
        }
        return true;
    }

    /*
     * Checks whether the given string is regular expression.
     * When the search Pattern is enclosed in "" it is regExp
     * @param   search pattern
     * @return  true if searchPattern enclosed in ""
     */
    public static boolean isRegExPattern(String searchPattern) {
        String searchContent = searchPattern;
        if (!searchContent.isEmpty()) {
            if (searchPattern.startsWith("\"") && searchPattern.endsWith("\"") && searchContent.length() > 1) {
                if (((searchContent.length() - searchContent.replaceAll("\"", "").length()) > 2)) {
                    return false;
                }
            } else if (searchContent.contains("\"")) {
                return false;
            }
        }
        return true;
    }
}
