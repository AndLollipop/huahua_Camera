package com.example.lenovo.workspace_huahua.manager;

import android.hardware.Camera;
import android.util.Log;

import com.example.lenovo.workspace_huahua.util.Util;

/**
 * Created by lenovo on 2018/1/18.
 */

public class ParameterExt {
    private static final String TAG = "ParameterExt";

    private Camera.Parameters mParameters;

    public ParameterExt(Camera.Parameters parameters) {
        mParameters = parameters;
    }

    public void setPicRotation(int rotation){
        mParameters.setRotation(rotation);
    }

    public void setPreviewSize(double ratio){
        Camera.Size previewSize = Util.getPreviewSize(mParameters.getSupportedPreviewSizes(),ratio);
        Log.d(TAG, "setPreviewSize: preview width = "+previewSize.width+"; height = "+previewSize.height);
        mParameters.setPreviewSize(previewSize.width,previewSize.height);

        Camera.Size picSize = Util.getPicSize(mParameters.getSupportedPictureSizes(),ratio);
        Log.d(TAG, "setPreviewSize: pic width = "+previewSize.width+"; height = "+previewSize.height);
        mParameters.setPictureSize(picSize.width,picSize.height);
    }

}
