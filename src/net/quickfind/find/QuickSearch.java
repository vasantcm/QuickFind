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

package net.quickfind.find;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.quickfind.config.PropertyPage;

/*
 * QuickSearch.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
public class QuickSearch extends Thread {

    /*
     * Thread lock to hold current thread
     */
    private final Object QUICK_SEARCH_THREAD_LOCK = new Object();
    /*
     * Search pattern
     */
    private String searchPattern;
    /*
     * Common raw data writter
     */
    private RawDataWriter rawDataWriter;
    /*
     * Regular expression flag
     */
    private boolean isRegEx = false;
    /*
     * Symbols file writter
     */
    private BufferedWriter symbolWriter = null;
    /*
     * Contains directory symbol(belonging to cache formatted path) of the found filenames
     */
    private ArrayList<String> directorySymbolList;
    /*
     * Duplicate remover of rawPathList
     */
    private HashSet<String> directorySymbolListHashSet;
    /*
     * Root name/path of the cache
     */
    private String cacheRoot;
    /*
     * Search object to send progress
     */
    private Search search;
    /*
     * Search result flag
     */
    private boolean isItemFound=false;
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(QuickSearch.class.getName());


    /*
     * Constructs the QuickSearch with CacheRoot,RawDataWriter,Search
     * @param   aCacheRoot->root directory of the cache     aRawDataWriter->common raw data writter   aSearch->search
     */
    public QuickSearch(String aCacheRoot, RawDataWriter aRawDataWriter, Search aSearch) {
        cacheRoot = aCacheRoot;
        rawDataWriter = aRawDataWriter;
        search = aSearch;
        directorySymbolList = new ArrayList<String>(10000);
        directorySymbolListHashSet = new HashSet<String>(10000);
        try {
            symbolWriter = new BufferedWriter(new FileWriter((new File(PropertyPage.getCacheDirectory() + PropertyPage.SEARCH_SYMBOL_FILE + PropertyPage.SYMBOLS_FILE_EXTENSION)), true));
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Checks whether the searchContent is regular expression.
     * @param   searchContent is the search pattern
     * @return  true if searchContent enclosed in ""
     */
    private boolean isRegEx(String searchContent) {
        if (searchContent.startsWith("\"") && searchContent.endsWith("\"") && searchContent.length() > 1) {
            isRegEx = true;
            return true;
        }
        return false;
    }

    /*
     * Start the search thread
     * @param   searchContent is the search pattern
     * @return  true if item found
     */
    protected boolean findNow(final String searchContent) {
        if (isRegEx(searchContent)) {
            try {
                /*
                 * removing "" quotes, considering given string as pure regEx pattern
                 */
                searchPattern = searchContent.replaceAll("\"", "");
                Pattern.compile(searchPattern);
            } catch (PatternSyntaxException patternSyntaxException) {
                LOGGER.log(Level.SEVERE, "Invalid pattern", patternSyntaxException);
                patternSyntaxException.notifyAll();
                return isItemFound;
            }
        } else {
            searchPattern = convertAsWildCard(searchContent);
        }
        this.start();
        //current thread THREAD_LOCK
        synchronized (this.QUICK_SEARCH_THREAD_LOCK) {
            try {
                this.QUICK_SEARCH_THREAD_LOCK.wait();
            } catch (InterruptedException interruptedEx) {
                LOGGER.log(Level.SEVERE, "Current Thread interrupted", interruptedEx);
            }
        }
        return isItemFound;
    }

    /*
     * Releases the QUICK_SEARCH_THREAD_LOCK
     */
    private void signal() {
        synchronized (this.QUICK_SEARCH_THREAD_LOCK) {
            this.QUICK_SEARCH_THREAD_LOCK.notify();
        }
    }

    /*
     * Calls cache iterator
     */
    @Override
    public void run() {
        if (cacheRoot != null) {
            iterateCache();
        }
        signal();//release the QUICK_SEARCH_THREAD_LOCK
    }

    /*
     * Builds wildcard pattern from search string
     * @param   stringToSearch is the search pattern
     * @return  searchString in WildCard format
     */
    private String convertAsWildCard(String stringToSearch) {
        StringBuffer searchString = new StringBuffer(stringToSearch);
        for (int i = 0; i < searchString.length(); i++) {
            if (searchString.charAt(i) == '.') {
                searchString.replace(i, i + 1, "(.)");
                i = i + 2;
            }
            if (searchString.charAt(i) == '*') {
                searchString.replace(i, i + 1, ".*");
                i = i + 1;
            }
            if (searchString.charAt(i) == '?') {
                searchString.replace(i, i + 1, ".?");
                i = i + 1;
            }
        }
        if (stringToSearch.indexOf('*') == -1) {
            searchString.insert(0, ".*");
            searchString.append(".*");
        }
        return searchString.toString();
    }

    /*
     * Searches for pattern match
     * @param   possible data to be matched
     * @return  true if pattern matches
     */
    private boolean findForMatch(final String sourceData) {
        Pattern pattern;
        Matcher matcher;
        try {
            if (isRegEx) {
                pattern = Pattern.compile(searchPattern);
                matcher = pattern.matcher(sourceData);
            } else {
                pattern = Pattern.compile(searchPattern.toLowerCase());
                matcher = pattern.matcher(sourceData.toLowerCase());
            }
            if (matcher.matches()) {
                PropertyPage.incrementSearchedFilesCount();
                return true;
            }
        } catch (PatternSyntaxException patternSyntaxException) {
            LOGGER.log(Level.SEVERE, "Invalid pattern", patternSyntaxException);
        }
        return false;
    }

    /*
     * Writes the absolute path of the directory symbols to file
     */
    private void resolveSymbols() {
        Collections.sort(directorySymbolList);  //sorting to enable binary search
        BufferedReader symbolsReader = null;
        try {
            symbolsReader = new BufferedReader(new FileReader((new File(PropertyPage.getCacheDirectory() + cacheRoot.hashCode() + PropertyPage.SYMBOLS_FILE_EXTENSION))));
            String symbolValue;
            while ((symbolValue = symbolsReader.readLine()) != null) {
                String key = symbolValue.substring(0, symbolValue.indexOf(PropertyPage.FILE_SEPARATOR));
                /*
                 * Writes rawpath for the matched directorySymbol into search result symbol file
                 */
                if (Collections.binarySearch(directorySymbolList, key) != -1) {
                    symbolWriter.write(symbolValue);
                    symbolWriter.newLine();
                }
            }
            directorySymbolList.clear();
            symbolWriter.flush();
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        } finally {
            try {
                symbolsReader.close();
                symbolsReader = null;
            } catch (IOException iOException) {
                LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
            }
        }
    }

    /*
     * Iterates through cache file to search pattern
     */
    private void iterateCache() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader((new File(PropertyPage.getCacheDirectory() + cacheRoot.hashCode() + PropertyPage.RAW_FILE_EXTENSION))));
            while (bufferedReader.ready()) {
                //reads cache formatted raw path
                String rawPath = bufferedReader.readLine();
                String fileName = getFileName(rawPath);
                if (rawPath == null || fileName == null) {
                    continue;
                }
                if (findForMatch(fileName)) {
                    isItemFound = true;
                    search.sendProgress();  //Updates progress to user
                    rawDataWriter.writeToFile(rawPath);     //writes rawpath to search result raw file
                    directorySymbolList.add(rawPath.substring(0, rawPath.indexOf(PropertyPage.FILE_SEPARATOR)));
                    if (PropertyPage.isFirstPush()) {
                        if (PropertyPage.getSearchedFilesCount() > PropertyPage.get_SEARCH_RESULT_SET_LIMIT()) {
                            removeDuplicates();     // removes duplicate symbols
                            resolveSymbols();       // writes absolute path of the symbols to file
                            rawDataWriter.flushRawDataWriter();
                            symbolWriter.flush();
                        }
                    }
                }
                if ((directorySymbolList.size() % 5000) == 0) {
                    removeDuplicates();
                }
                if (directorySymbolList.size() > 9999) {
                    resolveSymbols();
                }
                if (PropertyPage.getCacheIteratorStopper()) {
                    break;
                }
            }
            removeDuplicates();
            resolveSymbols();
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                symbolWriter.flush();
                symbolWriter.close();
            } catch (IOException iOException) {
                LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
            }
        }
    }

    /*
     * Removes all the duplicate symbols using HashSet
     * Removing duplicate symbols reduces the resolveSymbols() processing time
     */
    private void removeDuplicates() {
        directorySymbolListHashSet.addAll(directorySymbolList);
        directorySymbolList.clear();
        directorySymbolList.addAll(directorySymbolListHashSet);   //removing duplicates
        directorySymbolListHashSet.clear();
    }

    /*
     * Finds filename from given rawpath
     * @param   rawPath cache formatted path
     * @return  fileName
     */
    private String getFileName(String rawPath) {
        StringTokenizer stringTokenizer = new StringTokenizer(rawPath, PropertyPage.FILE_SEPARATOR);
        if (stringTokenizer.hasMoreTokens()) {
            stringTokenizer.nextToken();
        }
        if (stringTokenizer.hasMoreTokens()) {
            return stringTokenizer.nextToken();
        }
        return null;
    }
}