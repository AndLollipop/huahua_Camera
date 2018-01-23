package com.example.lenovo.workspace_huahua.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.example.lenovo.workspace_huahua.R;
import com.example.lenovo.workspace_huahua.util.Log;
import com.example.lenovo.workspace_huahua.util.Util;

/**
 * Created by lenovo on 2018/1/11.
 */

public class PreviewSurfaceView extends SurfaceView {
    private static final String TAG = "PreviewSurfaceView";
    private double mRatio = 16 / 9d;

    public PreviewSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
          int previewWidth = MeasureSpec.getSize(widthMeasureSpec);
          int previewHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "after onMeasure: previewWidth = "+previewWidth+"; previewHeight = "+previewHeight);

          boolean widthLonger = previewWidth > previewHeight?true:false;
          int longSide =  (widthLonger ?previewWidth : previewHeight);
          int shortSide = (widthLonger ? previewHeight : previewWidth);

          if (Math.abs((mRatio - (16 / 9d))) < Util.DEVIATION){
              Log.d(TAG, "onMeasure: 16:9");
              Log.d(TAG, "onMeasure: 16:9 longside = "+longSide+"; shortside = "+shortSide);
              if (longSide < shortSide*mRatio){
                  longSide = Math.round((float) (shortSide *mRatio) / 2)*2;
              }else {
                  shortSide = Math.round((float) (longSide / mRatio) / 2) *2;
              }
          }else {
             Log.d(TAG, "onMeasure: 4:3");
             if (longSide > shortSide * mRatio){
                 longSide = Math.round((float) (shortSide *mRatio) / 2)*2;
             }else {
                 shortSide = Math.round((float) (longSide / mRatio) / 2) *2;
             }
          }

          if (widthLonger){
              previewWidth = longSide;
              previewHeight = shortSide;
          }else {
              previewHeight = longSide;
              previewWidth = shortSide;
          }

          int marginTop = getResources().getDimensionPixelOffset(R.dimen.surface_margin_top);
          FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();

          if (mRatio < Util.FULL_SCREEN){
             params.setMargins(0,marginTop,0,0);
          }else {
             params.setMargins(0,0,0,0);
          }

          setLayoutParams(params);

        Log.d(TAG, "after onMeasure: previewWidth = "+previewWidth+"; previewHeight = "+previewHeight);
          setMeasuredDimension(previewWidth,previewHeight);
    }

    public void setRatio(double ratio){
        Log.d(TAG, "setRatio: ratio = "+ratio);
        if (mRatio != ratio){
            mRatio = ratio;
            requestLayout();
        }
    }
}

