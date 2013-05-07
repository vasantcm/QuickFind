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

import java.io.File;
import java.util.logging.Logger;
import net.quickfind.core.Utility;

/*
 * Init.java
 * @author  Copyright (C) 2012 Vasantkumar Mulage
 */
public class Init {

    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(Init.class.getName());

    /*
     * Initiates application essentials
     */
    public void initializeApplication() {
        createApplicationWorkingDirectory();
        createRootList();
        cleanCacheFiles();
    }

    /*
     * Creates cache directory in the user home directory
     */
    private void createApplicationWorkingDirectory() {
        String applicationWorkingDirecoty = System.getProperty("user.home");
        File applicationWorkingDirecotyFile = new File(applicationWorkingDirecoty);
        PropertyPage.setCacheDirectory(applicationWorkingDirecotyFile.toString() + PropertyPage.FILE_SEPARATOR + "cache" + PropertyPage.FILE_SEPARATOR);
        File absolutePath = new File(PropertyPage.getCacheDirectory());
        if (!absolutePath.isDirectory()) {
            if (!absolutePath.exists()) {
                absolutePath.mkdirs();
            }
        }
        applicationWorkingDirecotyFile = null;
        absolutePath = null;
    }

    /*
     * Creates rootList of the system
     */
    private void createRootList() {
        File[] rootList;
        if (PropertyPage.isLinux() || PropertyPage.isUnix()) {
            File parentDirectory = new File("/");
            rootList = parentDirectory.listFiles();
        } else {
            rootList = File.listRoots();
        }
        for (int i = 0; i < rootList.length; i++) {
            if (rootList[i].isDirectory()) {
                PropertyPage.CACHE_LIST.add(rootList[i].toString());
            }
        }
    }

    /*
     * Cleans all cache files except compressed and configuration
     */
    private void cleanCacheFiles() {
        Utility.cacheCleaner(null, 2);
    }
}
