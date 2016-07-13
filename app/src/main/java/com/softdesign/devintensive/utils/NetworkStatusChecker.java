package com.softdesign.devintensive.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bolshakova on 10.07.2016.
 */
public class NetworkStatusChecker {

    public static boolean isNetworkAvailable (Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
    }
}
