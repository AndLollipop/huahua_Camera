package com.example.lenovo.workspace_huahua.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by lenovo on 2018/1/11.
 */

public class PreviewSurfaceView extends SurfaceView {
    private static final String TAG = "PreviewSurfaceView";

    public PreviewSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
}

