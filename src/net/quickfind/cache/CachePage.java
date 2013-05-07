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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.quickfind.config.PropertyPage;
import net.quickfind.core.Utility;

/*
 * CachePage.java
 * @author  Copyright (C) 2011 Vasantkumar Mulage
 */
public class CachePage {

    /*
     * Collection of cache
     */
    private CacheCollection cacheCollection;
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(CachePage.class.getName());

    /*
     * Constructs cachePage
     * @param   cacheCollection
     */
    public CachePage(CacheCollection aCacheCollection) {
        cacheCollection = aCacheCollection;
    }

    /*
     * Loads the cache configuration from file
     */
    public void loadCacheCollection() {
        if (!loadCacheConfigFromFile(PropertyPage.CACHE_COLLECTION_CONFIG_FILE + PropertyPage.CONFIG_FILE_EXTENSION)) {
            cacheCollection = new CacheCollection();
            cacheCollection.setCacheList(new ArrayList<Cache>());
            cacheCollection.setName(PropertyPage.getSystemName());
            cacheCollection.setCacheIndex("-1");
        }
    }

    /*
     * Exctracts raw files from compressed file
     */
    public void generateRawData() {
        for (int i = 0; i < cacheCollection.getCachesList().size(); i++) {
            createRawData(((Cache) cacheCollection.getCachesList().get(i)).getName());
        }
    }

    /*
     * @return  Cache list from cache collection
     */
    public ArrayList getCacheList() {
        return cacheCollection.getCachesList();
    }

    /*
     * Adds new Cache to the list or replaces cache if it exists already
     * @param   cache object which is to be inserted
     */
    protected void addCache(Cache cache) {
        for (int i = 0; i < cacheCollection.getCachesList().size(); i++) {
            if (((Cache) cacheCollection.getCachesList().get(i)).getName().equals(cache.getName())) {
                cacheCollection.addCache(cache, i);//add new cache
                cacheCollection.getCachesList().remove(i + 1);//remove old cache
                return;
            }
        }
        cacheCollection.addCache(cache, cacheCollection.getCachesList().size());   //add new cache
    }

    /*
     * Calculates total items scanned
     * @return  total count of items scanned
     */
    private long getTotalItemsScanned() {
        long totalItemsScanned = 0L;
        for (int i = 0; i < cacheCollection.getCachesList().size(); i++) {
            totalItemsScanned += Long.valueOf(cacheCollection.getCachesList().get(i).getTotalItemsScanned());
        }
        return totalItemsScanned;
    }

    /*
     * Saves the cache configuration data to file
     * Data is organized as specified in Cache.java
     */
    protected void saveCacheConfigToFile() {
        cacheCollection.setTotalItemsScanned(String.valueOf(getTotalItemsScanned()));
        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(CacheCollection.class);
            Marshaller marshaller = jAXBContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            Writer writer = null;
            try {
                writer = new FileWriter(PropertyPage.getCacheDirectory() + PropertyPage.CACHE_COLLECTION_CONFIG_FILE + PropertyPage.CONFIG_FILE_EXTENSION);
                marshaller.marshal(cacheCollection, writer);
            } finally {
                try {
                    writer.close();
                } catch (IOException iOException) {
                    LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
                }
            }
        } catch (JAXBException jAXBException) {
            LOGGER.log(Level.SEVERE, "An error was encountered while creating the JAXBContext", jAXBException);
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Exctracts cache collection data from configuration file
     * @param   cache configuration file
     * @return  true if cache collection is valid otherwise false
     */
    private boolean loadCacheConfigFromFile(final String fileName) {
        File cacheConfigFile = new File(PropertyPage.getCacheDirectory() + fileName);
        if (cacheConfigFile.exists()) {
            try {
                JAXBContext jAXBContext = JAXBContext.newInstance(CacheCollection.class);
                Unmarshaller unMarshaller = jAXBContext.createUnmarshaller();
                cacheCollection = (CacheCollection) unMarshaller.unmarshal(new FileReader(cacheConfigFile));
                return true;
            } catch (JAXBException jAXBException) {
                LOGGER.log(Level.SEVERE, "An error was encountered while creating the JAXBContext", jAXBException);
            } catch (IOException iOException) {
                LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
            }
        }
        return false;
    }

    /*
     * Exctracts raw files from compressed file
     * @param   compressed cahe file name
     */
    private void createRawData(final String zipFileName) {
        byte[] buffer = new byte[1024];
        int len;
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(PropertyPage.getCacheDirectory() + zipFileName + PropertyPage.COMPRESSED_FILE_EXTENSION));
            //get the zipped file entry
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(PropertyPage.getCacheDirectory() + PropertyPage.FILE_SEPARATOR + fileName);
                //creates all non exists directories
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Deletes existing cache files
     * @param   encoded name of the cache
     */
    protected void deleteOldCacheFiles(final String cacheName) {
        Utility.cacheCleaner(cacheName, 3);
    }

    /*
     * Searches for existing cache index
     * @param   encoded name of the cache
     * @return  cache index if cacheName exists otherwise -1
     */
    private int getCacheIndex(String cacheName) {
        for (int i = 0; i < cacheCollection.getCachesList().size(); i++) {
            if (((Cache) cacheCollection.getCachesList().get(i)).getName().equals(cacheName)) {
                return Integer.valueOf(((Cache) cacheCollection.getCachesList().get(i)).getLocalCacheIndex());
            }
        }
        return -1;
    }

    /*
     * Searches for existing cache index
     * @param   encoded name of the cache
     * @return  cache index if cacheName exists otherwise new cacheIndex
     */
    protected int getNextCacheIndex(String cacheName) {
        int cacheIndex;
        if ((cacheIndex = getCacheIndex(cacheName)) != -1) {
            return cacheIndex;
        }
        cacheIndex = Integer.valueOf(cacheCollection.getCacheIndex());
        cacheCollection.setCacheIndex(String.valueOf(++cacheIndex));
        return cacheIndex;
    }
}
