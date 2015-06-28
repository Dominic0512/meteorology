package com.meteorology.meteorology.Class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * Created by lizhe on 2015/6/28.
 */
public class Internet {
    public static boolean haveNetworkConnection(Context context) {

        ConnectivityManager CManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NWInfo = CManager.getActiveNetworkInfo();
        return NWInfo != null && NWInfo.isConnectedOrConnecting();
    }
}
