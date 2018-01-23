package com.example.lenovo.workspace_huahua.util;

import android.app.Activity;
import android.hardware.Camera;
import android.util.*;
import android.view.Surface;

import java.util.List;

/**
 * Created by lenovo on 2018/1/17.
 */

public class Util {
    private static final String TAG = "Util";
    public static final double FULL_SCREEN = 16 /9d;
    public static final double DEVIATION = 0.02;

    public static int getDegree(Activity activity){
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Log.d(TAG, "getDegree: rotation = "+rotation);
        switch (rotation){
            case Surface.ROTATION_0:
                return 0;
                case Surface.ROTATION_90:
                    return 90;
                    case Surface.ROTATION_180:
                        return 180;
                        case Surface.ROTATION_270:
                            return 270;
        }
        return 0;
    }

    public static int getCameraOrientation(int sDegree,int cameraId){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,info);
        Log.d(TAG, "getCameraOrientation: sDegree = "+sDegree+"; infoOriention = "+info.orientation);

        int result;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            Log.d(TAG, "getCameraOrientation: front camera");
            result = (info.orientation + sDegree) % 360;
            result = (360 - result) % 360;
        }else {
            Log.d(TAG, "getCameraOrientation: back camera");
            result = (info.orientation - sDegree +360) % 360;
        }
        Log.d(TAG, "getCameraOrientation: result = "+result);
        return result;
    }

    public static Camera.Size getPreviewSize(List<Camera.Size> sizeList,double ratio){
        Camera.Size result = null;
        for (Camera.Size size:sizeList){
            if (Math.abs(ratio - size.width *1.0d / size.height) < DEVIATION){
                result = size;
            }
        }
        Log.d(TAG, "getPreviewSize: ratio = "+ratio+"; result width = "+result.width+"; height = "+result.height);
        return result;
    }

    public static Camera.Size getPicSize(List<Camera.Size> sizeList,double ratio){
        Camera.Size result = null;
        for (Camera.Size size:sizeList){
            if (Math.abs(ratio - size.width *1.0d / size.height) < DEVIATION){
                result = size;
            }
        }
        Log.d(TAG, "getPicSize: ratio = "+ratio+"; result width = "+result.width+"; height = "+result.height);
        return result;
    }
}
