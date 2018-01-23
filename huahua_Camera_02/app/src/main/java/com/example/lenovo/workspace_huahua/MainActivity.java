package com.example.lenovo.workspace_huahua;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.example.lenovo.workspace_huahua.customview.PreviewSurfaceView;
import com.example.lenovo.workspace_huahua.manager.CameraManager;
import com.example.lenovo.workspace_huahua.manager.ParameterExt;
import com.example.lenovo.workspace_huahua.manager.PerssionManager;
import com.example.lenovo.workspace_huahua.manager.ViewOpManager;
import com.example.lenovo.workspace_huahua.util.Log;
import com.example.lenovo.workspace_huahua.util.Util;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG = "MainActivity";
    public static final int OPEN_CAMERA = 1001;
    public static final int SET_ROTATION = 1002;
    public static final int INIT_PARAMETERS = 1003;
    public static final int SET_PICROTATION = 1004;
    public static final int SET_PREVIEWSIZE = 1005;
    public static final int RELEASE_CAMERA = 1006;
    public static final int START_PREVIEW = 1007;
    public static final int SET_HOLDER = 1008;
    public static final int SET_PARAMERTERS = 1009;
    public static final int STOP_PREVIEW = 1010;
    public static final int TAKE_PHOTO = 1011;
    public static final int SWITCH_CAMERA = 1012;

    private PerssionManager mPerssionManager;
    private PreviewSurfaceView mSurfaceView;
    private RelativeLayout mRoot;
    private CameraManager mCameraManager;
    private ViewOpManager mViewOpManager;
    private ParameterExt mParameterExt;
    private SurfaceHolder mSurfaceHolder;

    private int mDisplayRotation = 0;//图片需要旋转的角度以在屏幕显示正确的预览方向
    private int mRotation;//拍摄的照片需要旋转的角度，已使拍摄出的照片显示正确的方向
    private int mDisplayOrientation;//屏幕的方向
    private int mCameraId = 0;
    private double mRatio = 16 / 9d;

    private View mPreviewLayout;
    private View mOPViewLyout;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private OrientationEventListener mAlbumOrientationEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlbumOrientationEventListener = new AlbumOrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL);

        initHandler();
        mCameraManager = new CameraManager();
        mViewOpManager = new ViewOpManager(mHandler,this);
        mPerssionManager = new PerssionManager();
        mPerssionManager.requesePerssion(this);
        initView();
    }


    private class AlbumOrientationEventListener extends OrientationEventListener{
        public AlbumOrientationEventListener(Context context) {
            super(context);
        }

        public AlbumOrientationEventListener(Context context, int rate) {
            super(context, rate);
        }
        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == ORIENTATION_UNKNOWN) return;
            if (mDisplayOrientation == orientation) return;
            mDisplayOrientation = orientation;
            android.hardware.Camera.CameraInfo info =
                    new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(mCameraId, info);
            orientation = (orientation + 45) / 90 * 90;
            int rotation = 0;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else {  // back-facing camera
                rotation = (info.orientation + orientation) % 360;
            }
            mRotation = rotation;
            Log.d(TAG, "onOrientationChanged: rotation = "+rotation+"; orientation = "+orientation);

            mHandler.sendEmptyMessage(SET_PICROTATION);
            mHandler.sendEmptyMessage(SET_PARAMERTERS);       }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(mPerssionManager.isCameraPerssionReady(this) && mCameraManager.getCamera() == null){
            mHandler.sendEmptyMessage(OPEN_CAMERA);
            Log.d(TAG, "onResume: camera = "+mCameraManager.getCamera());
            mHandler.sendEmptyMessage(INIT_PARAMETERS);

            int sDegree = Util.getDegree(this);
            mDisplayRotation = Util.getCameraOrientation(sDegree,mCameraId);

            mHandler.sendEmptyMessage(SET_ROTATION);
            mAlbumOrientationEventListener.enable();
            mHandler.sendEmptyMessage(SET_PREVIEWSIZE);

            initSurfaceView();
        }
    }

    private void initHandler(){
        mHandlerThread = new HandlerThread("camera_op");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case OPEN_CAMERA:
                        mCameraManager.openCamera(msg.arg1);
                        break;
                    case SET_ROTATION:
                        mCameraManager.setRotation(mDisplayRotation);
                        break;
                    case INIT_PARAMETERS:
                        mParameterExt = new ParameterExt(mCameraManager.getParameters());
                        break;
                    case SET_PICROTATION:
                        mParameterExt.setPicRotation(mRotation);
                        break;
                    case SET_PREVIEWSIZE:

                        if (msg.obj != null){
                            mRatio = (double) msg.obj;
                        }
                        mParameterExt.setPreviewSize(mRatio);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSurfaceView.setRatio(mRatio);
                            }
                        });
                        break;
                    case SET_HOLDER:
                        mCameraManager.setPreviewDisplay(mSurfaceHolder);
                        break;
                    case START_PREVIEW:
                        mCameraManager.startPreview();
                        break;
                    case RELEASE_CAMERA:
                        mCameraManager.release();
                        break;
                    case SET_PARAMERTERS:
                        mCameraManager.setParameters();
                        break;
                    case TAKE_PHOTO:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCameraManager.takephoto();
                            }
                        });
                        break;
                    case SWITCH_CAMERA:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchCamera();
                            }
                        });
                        break;
                    default:
                            break;
                }
            }
        };
    }


    private void switchCamera(){
        mCameraId = mCameraId == 0?1:0;
        mCameraManager.stopPreview();
        mCameraManager.release();
        mRoot.removeView(mPreviewLayout);
        mSurfaceView = null;

        mCameraManager.openCamera(mCameraId);
        int sDegree = Util.getDegree(this);
        mDisplayRotation = Util.getCameraOrientation(sDegree,mCameraId);
        mHandler.sendEmptyMessage(SET_ROTATION);
        initSurfaceView();
        mParameterExt = new ParameterExt(mCameraManager.getParameters());
        mHandler.sendEmptyMessage(SET_PREVIEWSIZE);
        mCameraManager.setParameters();

        mCameraManager.startPreview();

    }


    private void initView(){
        mRoot = (RelativeLayout) findViewById(R.id.root);
        mOPViewLyout = mViewOpManager.getView();
        mRoot.addView(mOPViewLyout);
    }

    private void initSurfaceView(){
        Log.d(TAG, "initSurfaceView: mSurfaceView = "+mSurfaceView);
        if (mSurfaceView == null){
            LayoutInflater inflater = LayoutInflater.from(this);
            mPreviewLayout = inflater.inflate(R.layout.camera_preview_layout,null);
            mSurfaceView = mPreviewLayout.findViewById(R.id.camera_preview);

            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
            mRoot.addView(mPreviewLayout);
            mOPViewLyout.bringToFront();
        }

        mSurfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated: ");
        mHandler.sendEmptyMessage(SET_PARAMERTERS);
        mHandler.sendEmptyMessage(SET_HOLDER);
        mHandler.sendEmptyMessage(START_PREVIEW);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "surfaceChanged: ");
        mHandler.sendEmptyMessage(STOP_PREVIEW);
        mHandler.sendEmptyMessage(SET_PARAMERTERS);
        mHandler.sendEmptyMessage(START_PREVIEW);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: ");
        mHandler.sendEmptyMessage(RELEASE_CAMERA);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mHandler.sendEmptyMessage(RELEASE_CAMERA);
        if (mSurfaceView != null){
            mSurfaceView.setVisibility(View.GONE);
        }
        mAlbumOrientationEventListener.disable();
    }

}
