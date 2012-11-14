/*
    QuickFind (http://quickfind.sourceforge.net/)
    Cross-platform Java application for searching files in your Computer.

    Copyright (c) 2010, 2012 Vasantkumar Mulage

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

/*
 * Main.java
 * @author  Copyright (C) 2010 Vasantkumar Mulage
 */
package net.quickfind;

import net.quickfind.cache.CacheCleaner;
import net.quickfind.cache.CacheCollection;
import net.quickfind.config.Init;
import net.quickfind.cache.CachePage;
import net.quickfind.gui.QuickFind;
import net.quickfind.gui.QuickFindSplash;

public class Main {

    /*
     * Main GUI
     */
    private QuickFind quickFind;
    /*
     * Cachepage to handle cache related data
     */
    private CachePage cachePage;
    /*
     * Collection of cache
     */
    private CacheCollection cacheCollection;
    /*
     * Loads core components
     */
    private void initQuickFind() {
        Init init = new Init();
        /*
         * Loads splash screen
         */
        QuickFindSplash quickFindSplash = new QuickFindSplash();
        quickFindSplash.setVisible(true);

        cacheCollection = new CacheCollection();
        cachePage = new CachePage(cacheCollection);
        /*
         * Initialize the application
         */
        init.initializeApplication();

        cachePage.loadCacheCollection();
        cachePage.generateRawData();

        loadQuickFindGUI();
        
        /*
         * Unloads splash screen
         */
        quickFindSplash.setVisible(false);
        quickFindSplash.dispose();

        quickFindSplash = null;
        init = null;
        System.gc();    //let gc do its job
    }

    /*
     * Loads QuickFind Main GUI
     */
    private void loadQuickFindGUI() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                quickFind = new QuickFind(cachePage);
                quickFind.setVisible(true);
            }
        });
    }

    /*
     * Main method instantiates application
     */
    public static void main(String args[]) {
        Runnable mainLoaderThread = new Runnable() {
            
            public void run() {
                new Main().initQuickFind();
            }
        };
        mainLoaderThread.run();
        /*
         * Adds shutdownHook to call cache cleaner
         */
        Thread thread = new Thread(new CacheCleaner());
        Runtime.getRuntime().addShutdownHook(thread);
    }
}
