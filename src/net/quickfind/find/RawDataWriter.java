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

package net.quickfind.find;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.quickfind.config.PropertyPage;
import net.quickfind.core.Utility;

/*
 * RawDataWriter.java
 * @author  Copyright (C) 2012 Vasantkumar Mulage
 */
public class RawDataWriter {

    /*
     * Raw data writter
     */
    private BufferedWriter rawDataBufferedWriter = null;
    /*
     * Index writter for rawdata file
     */
    private BufferedWriter rawFileIndexWriter = null;
    /*
     * Lines counter
     */
    private long totalLinesWrote;
    /*
     * File object to find file length which is used by index writter
     */
    private File lengthFinder;
    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(RawDataWriter.class.getName());

    /*
     * Constructs raw data writter
     */
    public RawDataWriter() {
        totalLinesWrote = 0L;
        try {
            Utility.cacheCleaner(PropertyPage.SEARCH_RAW_FILE, 3);
            Utility.cacheCleaner(PropertyPage.SEARCH_RAW_FILE_INDEX, 3);
            Utility.cacheCleaner(PropertyPage.SEARCH_SYMBOL_FILE, 3);
            rawDataBufferedWriter = new BufferedWriter(new FileWriter(PropertyPage.getCacheDirectory() + PropertyPage.SEARCH_RAW_FILE + PropertyPage.RAW_FILE_EXTENSION));
            rawFileIndexWriter = new BufferedWriter(new FileWriter((new File(PropertyPage.getCacheDirectory() + PropertyPage.SEARCH_RAW_FILE_INDEX + PropertyPage.INDEX_FILE_EXTENSION))));
            lengthFinder = new File(PropertyPage.getCacheDirectory() + PropertyPage.SEARCH_RAW_FILE + PropertyPage.RAW_FILE_EXTENSION);
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }

    /*
     * Writes raw data into file
     * Writes file index for totalLinesWrote in multiple of SEARCH_RESULT_SET_LIMIT
     * @param   rawData
     */
    protected void writeToFile(String rawData) {
        synchronized (this) {
            try {
                rawDataBufferedWriter.write(rawData);
                rawDataBufferedWriter.newLine();
                ++totalLinesWrote;
                if ((totalLinesWrote % PropertyPage.get_SEARCH_RESULT_SET_LIMIT()) == 0) {
                    flushRawDataWriter();
                    rawFileIndexWriter.write(totalLinesWrote + "=" + lengthFinder.length());
                    rawFileIndexWriter.newLine();
                }
            } catch (IOException iOException) {
                LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
            }

        }
    }

    /*
     * Flushes stream of the write buffers
     */
    protected void flushRawDataWriter() {
        synchronized (this) {
            try {
                if (rawDataBufferedWriter != null) {
                    rawDataBufferedWriter.flush();
                }
            } catch (IOException iOException) {
                LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
            }
        }
    }

    /*
     * Closes write buffers
     */
    protected void closeRawDataWriter() {
        try {
            if (rawDataBufferedWriter != null && rawFileIndexWriter != null) {
                rawDataBufferedWriter.flush();
                rawDataBufferedWriter.close();
                rawFileIndexWriter.flush();
                rawFileIndexWriter.close();
            }
        } catch (IOException iOException) {
            LOGGER.log(Level.SEVERE, "An IO error occured", iOException);
        }
    }
}
