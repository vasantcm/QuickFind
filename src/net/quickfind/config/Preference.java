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

import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.UIManager;

/*
 * Preference.java
 * @author  Copyright (C) 2013 Vasantkumar Mulage
 */
public class Preference {

    /*
     * Initialize the preference node
     */
    static Preferences preferences = Preferences.userNodeForPackage(Preference.class);
    /*
     * Preference nodes used by application
     */
    static final String SEARCH_RESULT_LIMIT = "SrcLim";
    static final String CURRENT_SELECTED_THEME = "CurLim";
    static final String LOAD_DEFAULTS = "IsDft";

    /*
     * Exception logger
     */
    private final static Logger LOGGER = Logger.getLogger(Preference.class.getName());
    /*
     * @return  the SEARCH_RESULT_LIMIT
     */

    public static int getSearchResultLimit() {
        return preferences.getInt(SEARCH_RESULT_LIMIT, PropertyPage.getSearchResultLimit());
    }

    /*
     * @param newValue the SEARCH_RESULT_LIMIT to set
     * @return  the SEARCH_RESULT_LIMIT
     */
    public static void setSearchResultLimit(int newValue) {
        preferences.putInt(SEARCH_RESULT_LIMIT, newValue);
    }

    /*
     * @return  the LOAD_DEFAULTS
     */
    public static boolean isLoadDefaults() {
        return preferences.getBoolean(LOAD_DEFAULTS, PropertyPage.getLoadDefaults());
    }

    /*
     * @param  newValue the LOAD_DEFAULTS to set
     */
    public static void setLoadDefaults(boolean newValue) {
        preferences.putBoolean(LOAD_DEFAULTS, newValue);
    }

    /*
     * @return  the CURRENT_SELECTED_THEME
     */
    public static String getCurrentSelectedTheme() {
        return preferences.get(CURRENT_SELECTED_THEME, UIManager.getSystemLookAndFeelClassName());
    }

    /*
     * @param newValue the CURRENT_SELECTED_THEME to set
     */
    public static void setCurrentSelectedTheme(String newValue) {
        preferences.put(CURRENT_SELECTED_THEME, newValue);
    }
}
