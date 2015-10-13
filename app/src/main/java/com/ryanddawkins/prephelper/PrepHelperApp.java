package com.ryanddawkins.prephelper;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.ryanddawkins.prephelper.data.auth.LoginAdapter;
import com.ryanddawkins.prephelper.data.auth.parse.ParseLoginAdapter;
import com.ryanddawkins.prephelper.data.pojo.Category;
import com.ryanddawkins.prephelper.data.pojo.Item;
import com.ryanddawkins.prephelper.data.pojo.Prep;
import com.ryanddawkins.prephelper.data.pojo.PrepItem;
import com.ryanddawkins.prephelper.data.storage.PrepStorageAdapter;
import com.ryanddawkins.prephelper.data.storage.parse.ParsePrepStorageAdapter;
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
    private PrepStorageAdapter prepStorageAdapter;

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        // Parse setup
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Prep.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Item.class);
        ParseObject.registerSubclass(PrepItem.class);

        Parse.initialize(this);

        // Setting the static instance
        mInstance = this;
    }

    public LoginAdapter getLoginAdapter() {
        if(this.loginAdapter == null) {
            this.loginAdapter = new ParseLoginAdapter();
        }
        return this.loginAdapter;
    }
    public void releaseLoginAdapter() {
        this.loginAdapter = null;
    }

    public PrepStorageAdapter getPrepStorageAdapter() {
        if(this.prepStorageAdapter == null) {
            this.prepStorageAdapter = new ParsePrepStorageAdapter();
        }
        return this.prepStorageAdapter;
    }
    public void releasePrepStorageAdapter() {
        this.prepStorageAdapter = null;
    }
}
