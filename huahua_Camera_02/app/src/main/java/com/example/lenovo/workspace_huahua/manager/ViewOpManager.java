package com.example.lenovo.workspace_huahua.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.example.lenovo.workspace_huahua.MainActivity;
import com.example.lenovo.workspace_huahua.R;


/**
 * Created by lenovo on 2018/1/21.
 */

public class ViewOpManager implements View.OnClickListener{
    private static final String TAG = "ViewOpManager";

    private Handler mHandler;
    private Context mContext;
    private View mRootView;
    private PopupWindow mPopupWindow;

    private ImageView mSetting;
    private ImageView mTakdPhoto;
    private ImageView mSwitchCamera;

    public ViewOpManager(Handler handler, Context context) {
        mHandler = handler;
        mContext = context;
    }

    public View getView(){
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_main,null);
        RelativeLayout relativeLayout = rootView.findViewById(R.id.root);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = inflater.inflate(R.layout.op_view,relativeLayout);

        mSetting = mRootView.findViewById(R.id.setting);
        mTakdPhoto = mRootView.findViewById(R.id.take_photo);
        mSwitchCamera = mRootView.findViewById(R.id.switch_camera);

        mSetting.setOnClickListener(this);
        mTakdPhoto.setOnClickListener(this);
        mSwitchCamera.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                showSetPreviewPop();
                break;
            case R.id.take_photo:

                mHandler.sendEmptyMessage(MainActivity.TAKE_PHOTO);
                break;
            case R.id.radiobtn1:
                Message message = Message.obtain();
                message.obj = 16 / 9d;
                message.what = MainActivity.SET_PREVIEWSIZE;
                mHandler.sendMessage(message);
                mPopupWindow.dismiss();
                break;
            case R.id.radiobtn2:
                Message message1 = Message.obtain();
                message1.obj = 4 / 3d;
                message1.what = MainActivity.SET_PREVIEWSIZE;
                mHandler.sendMessage(message1);
                mPopupWindow.dismiss();
                break;

            case R.id.switch_camera:
                mHandler.sendEmptyMessage(MainActivity.SWITCH_CAMERA);
                break;
        }
    }

    private void showSetPreviewPop(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.set_preview,null);

        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        RadioButton radioButton1 = view.findViewById(R.id.radiobtn1);
        RadioButton radioButton2 = view.findViewById(R.id.radiobtn2);

        radioButton1.setOnClickListener(this);
        radioButton2.setOnClickListener(this);

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_main,null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
    }

}
