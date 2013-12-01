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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import net.quickfind.cache.Cache;
import net.quickfind.core.CacheElement;
import net.quickfind.cache.CachePage;

/*
 * Search.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
public class Search extends SwingWorker<Void, Void> implements CacheElement {

    /*
     * Root directories list
     */
    private ArrayList<String> searchCacheList;
    /*
     * List of cache elements
     */
    private ArrayList<Cache> cachedCacheList;
    /*
     * Quick search object
     */
    private QuickSearch quickSearch;
    /*
     * Common raw data writter
     */
    private RawDataWriter rawDataWriter;
    /*
     * Search pattern
     */
    private String searchPattern;
    /*
     * Cachepage to handle cache related data
     */
    private CachePage cachePage;
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(Search.class.getName());

    /*
     * Constructs Search with  searchString and cachePage
     * @param   searchString->search pattern    cachePage->cache configuration
     */
    public Search(final String searchString, CachePage cachePage) {
        searchCacheList = new ArrayList<String>();
        rawDataWriter = new RawDataWriter();
        this.searchPattern = searchString;
        cachedCacheList = cachePage.getCacheList();
        this.cachePage = cachePage;
    }

    /*
     * Looks for the pattern match across the caches
     */
    @Override
    public Void doInBackground() {
        for (int i = 0; i < searchCacheList.size(); i++) {
            for (int j = 0; j < cachedCacheList.size(); j++) {
                if (((Cache) cachedCacheList.get(j)).getCacheName().equals(searchCacheList.get(i).toString())) {
                    try {
                        quickSearch = new QuickSearch(cachePage.getIncludedCachePath(searchCacheList.get(i).toString()), rawDataWriter, this);
                        quickSearch.findNow(searchPattern);
                        quickSearch.join();     //Waits for quickSearch thread
                        quickSearch = null;
                    } catch (InterruptedException interruptedEx) {
                        LOGGER.log(Level.SEVERE, "Current Thread interrupted", interruptedEx);
                    }
                }
            }
        }
        cachedCacheList = null;
        try {
            rawDataWriter.closeRawDataWriter();
        } catch (Exception iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
        System.gc();    //let gc do its job
        return null;
    }

    /*
     * Updates progress to user
     */
    protected void sendProgress() {
        setProgress(getProgress() ^ 1);
    }

    /*
     * Adds cache name to the search list
     * @param   cache name
     */
    @Override
    public void addCache(String cacheName) {
        searchCacheList.add(cacheName);
    }

    /*
     * Adds cache list to the search list
     * @param   cache list
     */
    @Override
    public void addCacheList(ArrayList cacheList) {
        this.searchCacheList = cacheList;
    }
}
