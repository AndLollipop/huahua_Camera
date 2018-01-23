package com.example.lenovo.workspace_huahua.manager;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.example.lenovo.workspace_huahua.util.Log;

import java.io.IOException;

/**
 * Created by lenovo on 2018/1/11.
 */

public class CameraManager {
    private static final String TAG = "CameraManager";
    private Camera mCamera;

    public Camera getCamera(){
        return mCamera;
    }

    public void openCamera(int cameraId){
        Log.d(TAG, "openCamera: cameraId = "+cameraId);
        mCamera = Camera.open(cameraId);
    }

    public void setPreviewDisplay(SurfaceHolder holder){
        Log.d(TAG, "setPreviewDisplay: camera = "+mCamera);
        if (mCamera != null){
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void release(){
        Log.d(TAG, "release: camera = "+mCamera);
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void startPreview(){
        Log.d(TAG, "startPreview: camera = "+mCamera);
        if (mCamera != null){
            mCamera.startPreview();
            mCamera.setDisplayOrientation(90);
        }
    }
}
