package com.jaivin.saver.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by DS on 15/12/2017.
 */

public class Constants {

    public static boolean ADS_STATUS = true;
    // if you show ads in your app then its true otherwise set it false

    public static String ADS_TYPE = "admob";
    // if you require google abmob ads then write 'admob'
    // if you require facebook audiance network ads then write 'facebook'

    public static String ADS_ADMOB_ADS_ID = "ca-app-pub-9464499798392265~2140283963";
    // here set your admob app id

    public static String ADS_ADMOB_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    // here set your Admob banner ad unit id






    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
