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

package net.quickfind.cache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.quickfind.config.PropertyPage;
import net.quickfind.core.Utility;

/*
 * SymbolTable.java
 * @author  Copyright (C) 2012 Vasantkumar Mulage
 */
public class SymbolTable {

    /*
     * Map to hold directory and symbol (key,value)
     */
    private HashMap<String, String> directoryTable;
    /*
     * Reference map to directoryTable
     */
    private HashMap<String, String> referenceDirectoryTable;
    /*
     * Root directory of the cache
     */
    private String cacheRoot;
    /*
     * Formatted path
     */
    private StringBuilder cacheFormattedFilePath;
    /*
     * Raw File writer
     */
    private BufferedWriter rawFileWriter = null;
    /*
     * Cache index for current cache
     */
    private int cacheIndex;
    /*
     * Directory symbols file writter
     */
    private BufferedWriter symbolsFileWriter = null;
    /*
     * Seed value at cache level
     */
    private long localseedValue = 0L;
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(SymbolTable.class.getName());

    /*
     * Constructs SymbolTable with cacheRootPath and cacheIndex
     * @param   cacheRootPath->root directory     cacheIndex-> index at cache level
     */
    public SymbolTable(String cacheRootPath, int cacheIndex) {
        cacheRoot = cacheRootPath;
        this.cacheIndex = cacheIndex;
        directoryTable = new HashMap<String, String>();
        cacheFormattedFilePath = new StringBuilder(1024);
        try {
            rawFileWriter = new BufferedWriter(new FileWriter((new File(PropertyPage.getCacheDirectory() + cacheRoot.hashCode() + PropertyPage.RAW_FILE_EXTENSION))));
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
        try {
            symbolsFileWriter = new BufferedWriter(new FileWriter((new File(PropertyPage.getCacheDirectory() + cacheRoot.hashCode() + PropertyPage.SYMBOLS_FILE_EXTENSION))));
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Adds root directory of the cache to directory Table
     * @param   cacheRoot is a root directory
     */
    protected void addCacheRoot(String cacheRoot) {
        String seedValue = cacheIndex + String.valueOf(localseedValue);
        increaseSeedValue();
        directoryTable.put(cacheRoot, seedValue);
        writeSymbolData(seedValue + PropertyPage.FILE_SEPARATOR + cacheRoot);
    }

    /*
     * Converts given filePath to cache format and writes to file
     * Converts directory as symbol+filePath and writes to file
     * @param   filePath->absolute path    isDirectorytype->denotes directory or file
     */
    protected void add(String filePath, boolean isDirectorytype) {
        PropertyPage.incrementCachedFilesCount();
        cacheFormattedFilePath.delete(0, cacheFormattedFilePath.length());
        if (isDirectorytype) {
            String seedValue = cacheIndex + String.valueOf(localseedValue);
            increaseSeedValue();
            writeSymbolData(seedValue + PropertyPage.FILE_SEPARATOR + filePath);
            directoryTable.put(filePath, seedValue);
            cacheFormattedFilePath.append(directoryTable.get(getParentPath(filePath))).append(getDirectoryName(filePath)).append(PropertyPage.FILE_SEPARATOR).append("0").append(PropertyPage.FILE_SEPARATOR).append(Utility.getLastModifiedDate(filePath)).append(PropertyPage.FILE_SEPARATOR).append(Utility.getFilePropertiesMask(filePath));
            writeRawData(cacheFormattedFilePath.toString());
            if (directoryTable.size() > 100) {
                shrinkData(filePath);
            }
        } else {
            cacheFormattedFilePath.append(directoryTable.get(getParentPath(filePath))).append(getFileName(filePath)).append(PropertyPage.FILE_SEPARATOR).append(Utility.getFileSize(filePath)).append(PropertyPage.FILE_SEPARATOR).append(Utility.getLastModifiedDate(filePath)).append(PropertyPage.FILE_SEPARATOR).append(Utility.getFilePropertiesMask(filePath));
            writeRawData(cacheFormattedFilePath.toString());
        }
    }

    /*
     * Removes symbols from directory table except symbols refering to current path
     * @param   currentPath->path in which current cache is working on
     */
    private void shrinkData(String currentPath) {
        HashMap<String, String> hashMap = null;
        if (!currentPath.isEmpty()) {
            hashMap = getReuiredElements(currentPath);
        }
        removeDuplicateDirectories();
        if (hashMap != null) {
            directoryTable.clear();
            directoryTable = null;
            directoryTable = new HashMap<String, String>(hashMap);
            referenceDirectoryTable = new HashMap<String, String>(hashMap); //reference copy
        }
        hashMap = null;
    }

    /*
     * Writes cache formatted file path to file
     * @param   cacheFormattedFilePath-> formatted path of the cache
     */
    private void writeRawData(String cacheFormattedFilePath) {
        try {
            rawFileWriter.write(cacheFormattedFilePath);
            rawFileWriter.newLine();
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Writes directory symbol and absolute path
     * @param   symbolData-> symbol and directory path seperated by fileSeperator
     */
    private void writeSymbolData(String symbolData) {
        try {
            symbolsFileWriter.write(symbolData);
            symbolsFileWriter.newLine();
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Removes duplicate directories using directory reference copy
     */
    private void removeDuplicateDirectories() {
        Iterator it = directoryTable.entrySet().iterator();
        if (referenceDirectoryTable == null) {
            return;  //no elements remain
        }
        while (it.hasNext()) {
            if (referenceDirectoryTable.isEmpty()) {
                break;  //no elements remain
            }
            Map.Entry pairs = (Map.Entry) it.next();
            if (pairs.getKey().toString().equals(cacheRoot)) {
                continue;
            }
            if (referenceDirectoryTable.containsKey(pairs.getKey().toString())) {
                it.remove(); // avoids a ConcurrentModificationException
                directoryTable.remove(pairs.getKey().toString());
                referenceDirectoryTable.remove(pairs.getKey().toString());
            }
        }
        referenceDirectoryTable = null;
    }

    /*
     * Finds directory symbols for each directory of the currentPath
     * @param   currentPath-> path in which current cache is working on
     */
    private HashMap<String, String> getReuiredElements(String currentPath) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(currentPath, (String) directoryTable.get(currentPath));
        while (getElementCount(currentPath) >= 2) {
            currentPath = getParentPath(currentPath);
            if (directoryTable.get(currentPath) != null) {
                hashMap.put(currentPath, (String) directoryTable.get(currentPath));
            }
        }
        return hashMap;
    }

    /*
     * Counts number of directories
     * @param   filePath-> absolute path
     * @return  count of directories
     */
    private int getElementCount(String filePath) {
        StringTokenizer stringTokenizer = new StringTokenizer(filePath, PropertyPage.FILE_SEPARATOR);
        if (PropertyPage.isLinux() || PropertyPage.isUnix()) {
            return (stringTokenizer.countTokens() + 1);
        }
        return stringTokenizer.countTokens();
    }

    /*
     * Finds absolute path of parent directory
     * @param   filePath-> absolute path
     * @return  absolute directory path
     */
    private String getParentPath(String filePath) {
        if (filePath.contains(PropertyPage.FILE_SEPARATOR)) {
            if (getElementCount(filePath) <= 2) {
                return (filePath.substring(0, filePath.lastIndexOf(PropertyPage.FILE_SEPARATOR) + 1));
            } else {
                return (filePath.substring(0, filePath.lastIndexOf(PropertyPage.FILE_SEPARATOR)));
            }
        }
        return filePath;
    }

    /*
     * Finds fileName from the given absolute path
     * @param   filePath-> absolute path
     * @return  fileName
     */
    private String getFileName(String filePath) {
        return (filePath.substring(filePath.lastIndexOf(PropertyPage.FILE_SEPARATOR), filePath.length()));
    }

    /*
     * Finds last directory name
     * @param   filePath-> absolute path
     * @return  directory name
     */
    private String getDirectoryName(String filePath) {
        if (filePath.contains(PropertyPage.FILE_SEPARATOR)) {
            if (getElementCount(filePath) < 2) {
                return PropertyPage.FILE_SEPARATOR;
            } else {
                return (filePath.substring(filePath.lastIndexOf(PropertyPage.FILE_SEPARATOR), filePath.length()));
            }
        }
        return filePath;
    }

    /*
     * Finds compressed fileSize
     * @return  file size
     */
    protected String getCompressedFileSize() {
        return Utility.getFileSize(PropertyPage.getCacheDirectory() + cacheRoot.hashCode() + PropertyPage.COMPRESSED_FILE_EXTENSION);
    }

    /*
     * Compresses raw and symbol file into single file
     */
    protected void saveToCompressedFile() {
        byte[] buffer = new byte[1024];
        int len;
        String destFileName = PropertyPage.getCacheDirectory() + cacheRoot.hashCode() + PropertyPage.COMPRESSED_FILE_EXTENSION;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(destFileName);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            File rawFile = new File(PropertyPage.getCacheDirectory() + cacheRoot.hashCode() + PropertyPage.SYMBOLS_FILE_EXTENSION);
            File symbolFile = new File(PropertyPage.getCacheDirectory() + cacheRoot.hashCode() + PropertyPage.RAW_FILE_EXTENSION);
            if (!(symbolFile.exists() && rawFile.exists())) {
                return;
            }
            ZipEntry rawFileZipEntry = new ZipEntry(rawFile.getName());
            zipOutputStream.setLevel(9);
            zipOutputStream.putNextEntry(rawFileZipEntry);
            FileInputStream rawFileFileInputStream = new FileInputStream(rawFile);
            while ((len = rawFileFileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            rawFileFileInputStream.close();
            ZipEntry symbolFileZipEntry = new ZipEntry(symbolFile.getName());
            zipOutputStream.putNextEntry(symbolFileZipEntry);
            FileInputStream symbolFileFileInputStream = new FileInputStream(symbolFile);
            while ((len = symbolFileFileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            symbolFileFileInputStream.close();
            zipOutputStream.closeEntry();
            zipOutputStream.close();
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Closes all the opened files
     */
    protected void closeAll() {
        try {
            rawFileWriter.close();
            symbolsFileWriter.close();
            rawFileWriter = null;
            symbolsFileWriter = null;

        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Increments seed value by 1
     */
    private void increaseSeedValue() {
        ++localseedValue;
    }
}