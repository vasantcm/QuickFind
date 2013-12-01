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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import net.quickfind.core.CacheElement;
import net.quickfind.config.PropertyPage;

/*
 * Scanner.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
public class Scanner extends SwingWorker<Void, Void> implements CacheElement {

    /*
     * List of cache elements to scan
     */
    private ArrayList<String> scanList;
    /*
     * Number of cache elements to scan
     */
    private int scanListSize = 0;
    /*
     * File iterator to walk through child files/directory
     */
    private FileIterator fileIterator[];
    /*
     * Cachepage to handle cache related data
     */
    private CachePage cachePage;
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(Scanner.class.getName());

    /*
     * Constructs the scanner with cachepage
     * @param   cache configuration
     */
    public Scanner(CachePage aCachePage) {
        scanList = new ArrayList<String>();
        cachePage = aCachePage;
    }

    /*
     * Background process to handle scanner
     */
    @Override
    protected Void doInBackground() {
        PropertyPage.resetCachedFilesCount();
        scanListSize = scanList.size();
        /*
         * Timer to update cache progress
         */
        final int TIMER_VALUE = 10 * scanListSize;

        fileIterator = new FileIterator[scanList.size()];
        for (int i = 0; i < scanListSize; i++) {
            fileIterator[i] = new FileIterator(cachePage.getIncludedCachePath(scanList.get(i).toString()), cachePage, this);
            fileIterator[i].runScanner();
            sendProgress(); //update progress to user
        }
        while (!isCancelled() && PropertyPage.getIteratorThreadAliveCount() >= 1) {
            sendProgress(); //update progress to user
            try {
                if (!PropertyPage.getFileIteratorStopper()) {
                    Thread.sleep(TIMER_VALUE);
                }
            } catch (InterruptedException interruptedEx) {
                LOGGER.log(Level.SEVERE, "Current Thread interrupted", interruptedEx);
                Thread.currentThread().interrupt();
            }
        }
        cachePage.saveCacheConfigToFile();
        return null;
    }

    /*
     * Updates progress to user
     */
    protected void sendProgress() {
        setProgress(getProgress() ^ 1);
    }

    /*
     * Stops the running scanner
     * @return  true when scanner stops successfully
     */
    public boolean stopScanner() {
        /*
         * Timer to wait until caching process aborts completely
         */
        long LONGER_TIMER_VALUE = 100 * scanListSize;
        for (int i = 0; i < scanListSize; i++) {
            while (fileIterator[i].isAlive()) {
                try {
                    Thread.sleep(LONGER_TIMER_VALUE);
                } catch (InterruptedException interruptedEx) {
                    LOGGER.log(Level.SEVERE, "Current Thread interrupted", interruptedEx);
                    Thread.currentThread().interrupt();
                }
            }
        }
        return true;
    }

    /*
     * Adds cache name to the scan list
     * @param   cache name
     */
    @Override
    public void addCache(String cacheName) {
        scanList.add(cacheName);
    }

    /*
     * Adds cache list to the scan list
     * @param   cache list
     */
    @Override
    public void addCacheList(ArrayList<String> cacheList) {
        scanList = cacheList;
    }
}