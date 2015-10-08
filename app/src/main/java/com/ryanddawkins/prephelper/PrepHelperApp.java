package com.ryanddawkins.prephelper;

import android.app.Application;

import com.parse.Parse;
import com.ryanddawkins.prephelper.data.auth.LoginAdapter;
import com.ryanddawkins.prephelper.data.auth.ParseLoginAdapter;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ryan on 9/15/15.
 */
public class PrepHelperApp extends Application {

    private static PrepHelperApp mInstance;
    public static PrepHelperApp getInstance() {
        return mInstance;
    }

    private LoginAdapter loginAdapter;

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        Parse.initialize(this);

        mInstance = this;

        this.loginAdapter = new ParseLoginAdapter();
    }

    public LoginAdapter getLoginAdapter() {
        return this.loginAdapter;
    }

}
