package com.example.task1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zayed on 12-Jan-18.
 */

public class NetworkAccess {

    private Context context;

    public NetworkAccess(Context context) {
        this.context=context;
    }


    boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                haveConnectedWifi = true;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                haveConnectedMobile=true;
            }
        } else {
            // not connected to the internet
            haveConnectedWifi = false;
            haveConnectedMobile = false;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
