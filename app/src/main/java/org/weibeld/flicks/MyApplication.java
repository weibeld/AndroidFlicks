package org.weibeld.flicks;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Used solely for integrating Stetho (Chrome-based Android debugging bridge)
 */

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
