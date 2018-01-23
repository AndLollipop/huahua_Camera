package com.example.lenovo.workspace_huahua.util;

/**
 * Created by lenovo on 2018/1/11.
 */

public class Log {

    private static final String LOG_SUB = "PH_Cam_";

    public static void d(String TAG,String value){
        android.util.Log.d(LOG_SUB+TAG, value);
    }

    public static void v(String TAG,String value){
        android.util.Log.v(LOG_SUB+TAG, value);
    }

    public static void e(String TAG,String value){
        android.util.Log.e(LOG_SUB+TAG, value);
    }

    public static void i(String TAG,String value){
        android.util.Log.i(LOG_SUB+TAG, value);
    }

    public static void w(String TAG,String value){
        android.util.Log.w(LOG_SUB+TAG, value);
    }
}
