package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;

import org.greenrobot.greendao.database.Database;


public class DevintensiveApplication extends Application {

    public static SharedPreferences sSharedPreferences;
    public static Context sContext;
    private static DaoSession sDaoSession;

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getsSharedPreferences() {
        return sSharedPreferences;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = this;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devintensive-db");
        Database db = helper.getWritableDb();

        sDaoSession = new DaoMaster(db).newSession();
        Stetho.initializeWithDefaults(this);
    }
}

