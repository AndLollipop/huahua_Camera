package com.example.lenovo.workspace_huahua;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.lenovo.workspace_huahua.customview.PreviewSurfaceView;
import com.example.lenovo.workspace_huahua.manager.CameraManager;
import com.example.lenovo.workspace_huahua.manager.PerssionManager;
import com.example.lenovo.workspace_huahua.util.Log;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG = "MainActivity";
    private CameraOP mCameraOp;
    private PerssionManager mPerssionManager;
    private PreviewSurfaceView mSurfaceView;
    private RelativeLayout mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        CameraManager manager = new CameraManager();
        mCameraOp = new CameraOP(manager);

        mPerssionManager = new PerssionManager();
        if (mPerssionManager.requesePerssion(this)){
            mCameraOp.openCamera();
            initSurfaceView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: camera = "+mCameraOp.getCamera());
        if(mPerssionManager.isCameraPerssionReady(this) && mCameraOp.getCamera() == null){
            mCameraOp.openCamera();
            initSurfaceView();

        }

    }

    private void initView(){
        mRoot = (RelativeLayout) findViewById(R.id.root);
    }

    private void initSurfaceView(){
        Log.d(TAG, "initSurfaceView: mSurfaceView = "+mSurfaceView);
        if (mSurfaceView == null){
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.camera_preview_layout,null);
            mSurfaceView = view.findViewById(R.id.camera_preview);
            SurfaceHolder holder = mSurfaceView.getHolder();
            holder.addCallback(this);
            mRoot.addView(view);
        }

        mSurfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated: ");
        mCameraOp.setPreviewDisplay(surfaceHolder);
        mCameraOp.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: ");
        mCameraOp.releaseCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mCameraOp.releaseCamera();
        if (mSurfaceView != null){
            mSurfaceView.setVisibility(View.GONE);
        }
    }

    public class CameraOP{
        CameraManager mCameraManager;

        public Camera getCamera(){
            return mCameraManager.getCamera();
        }

        public void openCamera(int id){
            mCameraManager.openCamera(id);
        }

        public void openCamera(){
            openCamera(0);
        }

        public CameraOP(CameraManager manager) {
            this.mCameraManager = manager;
        }

        public void setPreviewDisplay(SurfaceHolder holder){
            mCameraManager.setPreviewDisplay(holder);
        }

        public void startPreview(){
            mCameraManager.startPreview();
        }

        public void releaseCamera(){
            mCameraManager.release();
        }
    }

}
