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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/*
 * CacheCollection.java
 * @author  Copyright (C) 2012 Vasantkumar Mulage
 */
@XmlRootElement
public class CacheCollection {

    @XmlElementWrapper(name = "cacheList")
    @XmlElement(name = "cache")

    /*
     * ArrayList to hold cache objects
     */
    private ArrayList<Cache> cacheList;
    /*
     * System name OR Computer name in which caching is done
     */
    private String name;
    /*
     * Total items scanned in this CacheCollection
     */
    private String totalItemsScanned;
    /*
     * Last used cache index
     */
    private String cacheIndex;

    /*
     * @return  the cacheList
     */
    public ArrayList<Cache> getCachesList() {
        return cacheList;
    }

    /*
     * @param   cacheList the cacheList to set
     */
    public void setCacheList(ArrayList<Cache> cacheList) {
        this.cacheList = cacheList;
    }

    /*
     * @return  the name
     */
    public String getName() {
        return name;
    }

    /*
     * @param   name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * Inserts the given cache at the specified position.
     */
    public void addCache(Cache cache, int index) {
        cacheList.add(index, cache);
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
     * @return  the cacheIndex
     */
    public String getCacheIndex() {
        return cacheIndex;
    }

    /*
     * @param   cacheIndex the cacheIndex to set
     */
    public void setCacheIndex(String cacheIndex) {
        this.cacheIndex = cacheIndex;
    }
}