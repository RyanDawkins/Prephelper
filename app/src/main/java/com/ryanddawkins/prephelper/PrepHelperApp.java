package com.ryanddawkins.prephelper;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ryan on 9/15/15.
 */
public class PrepHelperApp extends Application {

    private static PrepHelperApp mInstance;

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        mInstance = this;
    }

    public static PrepHelperApp getInstance() {
        return mInstance;
    }

}
