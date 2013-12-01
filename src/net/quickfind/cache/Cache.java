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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/*
 * Cache.java
 * @author  Copyright (C) 2012 Vasantkumar Mulage
 */
@XmlRootElement(name = "cache")
@XmlType(propOrder = {"includedPath", "excludedPath", "cacheName", "userGivenName", "totalItemsScanned", "seedStartedFrom",
    "startTimeStamp", "endTimeStamp", "timeTakenToCache", "cacheFileSize", "localCacheIndex"})
public class Cache {

    /*
     * Encoded name of the cache
     */
    private String cacheName = "";
    /*
     * User provided name of the cache
     */
    private String userGivenName = "";
    /*
     * Absolute include path of the cache
     */
    private String includedPath = "";
    /*
     * Excluded absolute path from the cache
     */
    private String excludedPath = "";
    /*
     * Intial seed value of the SymbolTable
     */
    private String seedStartedFrom = "";
    /*
     * Total number of items scanned
     */
    private String totalItemsScanned = "0";
    /*
     * File size of the cache file
     */
    private String cacheFileSize = "";
    /*
     * The time when caching process started
     */
    private String startTimeStamp = "";
    /*
     * The time when caching process finished
     */
    private String endTimeStamp = "";
    /*
     * Time elapsed to cache
     */
    private String timeTakenToCache = "";
    /*
     * Cache index of the cache
     */
    private String localCacheIndex = "";

    /*
     * @return  the cache name
     */
    public String getCacheName() {
        return cacheName;
    }

    /*
     * @param   name the cache name to set
     */
    public void setCacheName(String name) {
        this.cacheName = name;
    }

    /*
     * @return  the userGivenName
     */
    public String getuserGivenName() {
        return userGivenName;
    }

    /*
     * @param   userGivenName the userGivenName to set
     */
    public void setuserGivenName(String userGivenName) {
        this.userGivenName = userGivenName;
    }

    /*
     * @return  the included path
     */
    public String getIncludedPath() {
        return includedPath;
    }

    /*
     * @param   path the included path to set
     */
    public void setIncludedPath(String path) {
        this.includedPath = path;
    }

    /*
     * @return  the excludedPath
     */
    public String getExcludedPath() {
        return excludedPath;
    }

    /*
     * @param   excludedPath the excludedPath to set
     */
    public void setExcludedPath(String excludedPath) {
        this.excludedPath = excludedPath;
    }

    /*
     * @return  the startTimeStamp
     */
    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    /*
     * @param   startTimeStamp the startTimeStamp to set
     */
    public void setStartTimeStamp(String startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    /*
     * @return  the seedStartedFrom
     */
    public String getSeedStartedFrom() {
        return seedStartedFrom;
    }

    /*
     * @param   seedStartedFrom the seedStartedFrom to set
     */
    public void setSeedStartedFrom(String seedStartedFrom) {
        this.seedStartedFrom = seedStartedFrom;
    }

    /*
     * @return  the totalItemsScanned
     */
    public String getTotalItemsScanned() {
        return totalItemsScanned;
    }

    /*
     * @param   totalItemsScanned the totalItemsScanned to set
     */
    public void setTotalItemsScanned(String totalItemsScanned) {
        this.totalItemsScanned = totalItemsScanned;
    }

    /*
     * @return  the cacheFileSize
     */
    public String getCacheFileSize() {
        return cacheFileSize;
    }

    /*
     * @param   cacheFileSize the cacheFileSize to set
     */
    public void setCacheFileSize(String cacheFileSize) {
        this.cacheFileSize = cacheFileSize;
    }

    /*
     * @return  the timeTakenToCache
     */
    public String getTimeTakenToCache() {
        return timeTakenToCache;
    }

    /*
     * @param   timeTakenToCache the timeTakenToCache to set
     */
    public void setTimeTakenToCache(String timeTakenToCache) {
        this.timeTakenToCache = timeTakenToCache;
    }

    /*
     * @return  the endTimeStamp
     */
    public String getEndTimeStamp() {
        return endTimeStamp;
    }

    /*
     * @param   endTimeStamp the endTimeStamp to set
     */
    public void setEndTimeStamp(String endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    /*
     * @return  the localCacheIndex
     */
    public String getLocalCacheIndex() {
        return localCacheIndex;
    }

    /*
     * @param   localCacheIndex the cacheindex to set
     */
    public void setLocalCacheIndex(String localCacheIndex) {
        this.localCacheIndex = localCacheIndex;
    }
}
