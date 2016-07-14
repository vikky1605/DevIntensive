package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bolshakova on 27.06.2016.
 */
public class DevintensiveApplication extends Application {

    public static SharedPreferences sSharedPreferences;
    public static Context sContext;

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


    }
}

