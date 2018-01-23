package com.example.lenovo.workspace_huahua.manager;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.example.lenovo.workspace_huahua.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by lenovo on 2018/1/11.
 */

public class CameraManager {
    private static final String TAG = "CameraManager";
    private Camera mCamera;
    private Camera.Parameters mParameters;


    public Camera getCamera(){
        return mCamera;
    }

    public void openCamera(){
        openCamera(0);
    }

    public void openCamera(int cameraId){
        Log.d(TAG, "openCamera: cameraId = "+cameraId);
        mCamera = Camera.open(cameraId);
    }

    public Camera.Parameters getParameters(){
        mParameters = mCamera.getParameters();
        return mParameters;
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
        }
    }

    public void stopPreview(){
        Log.d(TAG, "stopPreview: ");
        if (mCamera != null){
            mCamera.stopPreview();
        }
    }

    public void setRotation(int rotation){
        Log.d(TAG, "setRotation: rotation ="+rotation);
        if (mCamera != null){
            mCamera.setDisplayOrientation(rotation);
        }
    }

    public void takephoto(){
        if (mCamera != null){

            mCamera.takePicture(null,null,mPictureCallback);
        }
    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = SaveManager.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null || !pictureFile.exists()){
                return;
            }

            try {
                FileOutputStream os = new FileOutputStream(pictureFile);
                os.write(data);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                mCamera.startPreview();
            }
        }
    };

    public void setParameters(){
        if (mCamera != null){
            mCamera.setParameters(mParameters);
        }
    }


}
