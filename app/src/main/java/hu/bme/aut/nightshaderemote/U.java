package hu.bme.aut.nightshaderemote;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Utility methods.
 *
 * Created by akos on 2014.03.15..
 */
public class U {
    protected static Context context;
    public static void init(Context context) {
        U.context = context;
    }

    public static String getServerAddressPref() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("serverAddress", "");
    }

    public static String getServerPortPref() {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("serverPort", "");
    }


    /* ############################################################################################
                Helper methods
       ############################################################################################ */

    public static long getLongPref(String key) {
        return getLongPref(key, 0);
    }

    public static long getLongPref(String key, long defValue) {
        String sDefVal = Long.toString(defValue);
        String val = PreferenceManager.getDefaultSharedPreferences(context).getString(key, sDefVal);
        return Long.valueOf(val);
    }

    public static int getIntPref(String key) {
        return getIntPref(key, 0);
    }

    public static int getIntPref(String key, int defValue) {
        String sDefVal = Integer.toString(defValue);
        String val = PreferenceManager.getDefaultSharedPreferences(context).getString(key, sDefVal);
        return Integer.valueOf(val);
    }
}
