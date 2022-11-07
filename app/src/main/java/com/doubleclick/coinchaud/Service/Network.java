package com.doubleclick.coinchaud.Service;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created By Eslam Ghazy on 9/22/2022
 */
public class Network {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
