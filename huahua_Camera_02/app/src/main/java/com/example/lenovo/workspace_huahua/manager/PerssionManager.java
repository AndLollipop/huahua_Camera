package com.example.lenovo.workspace_huahua.manager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/1/11.
 */

public class PerssionManager {
    private static final String TAG = "PerssionManager";
    private List<String> mLaunchPerssion  = new ArrayList<String>() ;

    public PerssionManager() {
        initLaunchPerssion();
    }

    /**
     * init need perssions
     */
    private void initLaunchPerssion(){
        mLaunchPerssion.add(Manifest.permission.CAMERA);
        mLaunchPerssion.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mLaunchPerssion.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * request perssion from user by system dialog
     * @param activity
     * @return
     */
    public   boolean requesePerssion(Activity activity){
        List<String> perssions = checkPersssion(activity);
        if (perssions.size() > 0){
            //显示权限请求的dialog
            ActivityCompat.requestPermissions(activity,perssions.toArray(new String[perssions.size()]),100);
            return false;
        }
        return true;
    }

    /**
     * check and save need request perssion
     * @param activity
     * @return
     */
    private  List<String> checkPersssion(Activity activity){
        List<String> needRequestPerssion = new ArrayList<>();
        for (String perssion:mLaunchPerssion){
            if (ActivityCompat.checkSelfPermission(activity,perssion)  == PackageManager.PERMISSION_DENIED){
                needRequestPerssion.add(perssion);
            }
        }
        Log.e(TAG, "checkPersssion: needRequestPerssion size = "+needRequestPerssion.size());
        return needRequestPerssion;
    }


    /**
     * check whether all perssion is ready
     * @param activity
     * @return
     */
    public boolean isCameraPerssionReady(Activity activity,String[] perssions){
        for (String perssion:perssions){
            if(ActivityCompat.checkSelfPermission(activity,perssion) == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    public boolean isCameraPerssionReady(Activity activity){
        for (String perssion:mLaunchPerssion){
            if(ActivityCompat.checkSelfPermission(activity,perssion) == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }
}
