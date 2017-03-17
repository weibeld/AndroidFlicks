package org.weibeld.flicks.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dw on 17/03/17.
 */

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private SharedPreferences mPrefs;

    private SharedPrefManager(Context c) {
        mPrefs = c.getSharedPreferences(c.getPackageName(), Context.MODE_PRIVATE);
    }

    public static SharedPrefManager getInstance(Context c) {
        if (instance == null) instance = new SharedPrefManager(c);
        return instance;
    }

    public SharedPreferences getPrefs() {
        return mPrefs;
    }
}
