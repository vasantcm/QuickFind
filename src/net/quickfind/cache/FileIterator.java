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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.quickfind.config.PropertyPage;
import net.quickfind.core.Utility;

/*
 * FileIterator.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
public class FileIterator extends Thread {

    /*
     * Root directory of the cache
     */
    private String cacheRoot;
    /*
     * Files count
     */
    private long localItemsCount;
    /*
     * Symbol table of filename and parent directory 
     */
    private SymbolTable symbolTable;
    /*
     * Cachepage to hanlde cache data
     */
    private CachePage cachePage;
    /*
     * Cache object to hold cache related data
     */
    private Cache cache;
    /*
     * Scanner is a parent controller of the fileIterator
     */
    private Scanner scanner;
    /*
     * Cache start time
     */
    private long startTime;
    /*
     * Cache end time
     */
    private long endTime;
    /*
     * Cache index of the current cache
     */
    private int cacheIndex;
    /*
     * Thread lock to hold current thread
     */
    private final Object FILE_ITERATOR_THREAD_LOCK = new Object();
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(FileIterator.class.getName());

    /*
     * Constructs the FileIterator with cache root path, cachepage and scanner
     * @param   aCacheRoot->cache root path   aCachePage->cache configuration    aScanner->scanner
     */
    public FileIterator(String aCacheRoot, CachePage aCachePage, Scanner aScanner) {
        cacheRoot = aCacheRoot;
        cachePage = aCachePage;
        scanner = aScanner;
        cachePage.deleteOldCacheFiles(String.valueOf(cacheRoot.hashCode()));
        cacheIndex = cachePage.getNextCacheIndex(String.valueOf(cacheRoot.hashCode()));
        symbolTable = new SymbolTable(cacheRoot, cacheIndex);
        localItemsCount = startTime = endTime = 0L;
        cache = new Cache();
    }

    /*
     * Starts the thread with normal priority
     */
    protected void runScanner() {
        this.setPriority(NORM_PRIORITY);
        this.start();
        synchronized (this.FILE_ITERATOR_THREAD_LOCK) {
            try {
                this.FILE_ITERATOR_THREAD_LOCK.wait();    //blocking thread to enable single thread operation
            } catch (InterruptedException interruptedEx) {
                LOGGER.log(Level.SEVERE, "Current Thread interrupted", interruptedEx);
            }
        }
    }

    /*
     * Initiates the cache
     */
    private void initializeFileIterator() {
        startTime = System.currentTimeMillis();
        PropertyPage.incrementIteratorThreadAliveCount();
        cache.setLocalCacheIndex(String.valueOf(cacheIndex));
        cache.setName(String.valueOf(cacheRoot.hashCode()));
        cache.setPath(cacheRoot);
        cache.setStartTimeStamp(String.valueOf(startTime));
        cache.setSeedStartedFrom(String.valueOf(1));
        symbolTable.addCacheRoot(cacheRoot);
    }

    /*
     * Stores cache related data into cache collection
     */
    private void finalizeFileIterator() {
        scanner.sendProgress(); // finally update the progress to user
        cache.setTotalItemsScanned(String.valueOf(localItemsCount));
        symbolTable.closeAll();     //closes all opened files
        endTime = System.currentTimeMillis();
        cache.setEndTimeStamp(String.valueOf(endTime));
        cache.setTimeTakenToCache(Utility.getReadableElapsedInterval(endTime - startTime));
        symbolTable.saveToCompressedFile();     //puts raw and symbol files into compressed file
        cache.setCacheFileSize(symbolTable.getCompressedFileSize());
        cachePage.addCache(cache);
        PropertyPage.decrementIteratorThreadAliveCount();   //removing this thread count from queue
    }

    /*
     * Calls file iterator to cache all Files/Directories under cacheRoot
     */
    @Override
    public void run() {
        initializeFileIterator();
        iterateFilesInDirectory(cacheRoot);
        finalizeFileIterator();
        signal();   //release the FILE_ITERATOR_THREAD_LOCK
    }

    /*
     * Releases the FILE_ITERATOR_THREAD_LOCK
     */
    private void signal() {
        synchronized (this.FILE_ITERATOR_THREAD_LOCK) {
            this.FILE_ITERATOR_THREAD_LOCK.notify();
        }
    }

    /*
     * Iterates through all child files/directory under rootDirectory
     * @param   rootDirectory of the iteration
     */
    private void iterateFilesInDirectory(final String rootDirectory) {
        scanner.sendProgress(); // update the progress to user

        if (PropertyPage.getFileIteratorStopper()) {
            return; //caching process aborted by user
        }

        if (!rootDirectory.equals(cacheRoot)) {
            symbolTable.add(rootDirectory, true);   //cache the directory
            localItemsCount++;
        }

        File parentDirectory = new File(rootDirectory);
        File[] childFiles = parentDirectory.listFiles();
        parentDirectory = null;
        if (childFiles != null) {
            for (int i = 0; i < childFiles.length; i++) {
                
                if (childFiles[i].isDirectory()) {
                    iterateFilesInDirectory(childFiles[i].getAbsolutePath()); //recursive call to subdirectory
                } else {
                    symbolTable.add(childFiles[i].toString(), false);   //cache the file
                    localItemsCount++;
                }
            }
        }
    }
}