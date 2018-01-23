package com.example.lenovo.workspace_huahua.manager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by lenovo on 2018/1/18.
 */

public class SaveManager {
    private static final String TAG = "SaveManager";
    private static final String IMG_TAG = "img";

    public static final File getOutputMediaFile(int type){
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        mediaStorageDir.mkdir();

        String timeStamp = new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date());
        timeStamp = IMG_TAG+timeStamp;
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE){
            try {
                mediaFile = File.createTempFile(timeStamp,".jpg",mediaStorageDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "getOutputMediaFile: path = "+mediaFile.getPath());
        return mediaFile;
    }

}
