package com.example.ggq.gaoguoqing20180821;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.Call;
import okhttp3.Response;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.ggq.gaoguoqing20180821.utils.OKHttpUtil;
import com.example.ggq.gaoguoqing20180821.utils.RequestCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        //File externalStorageDirectory = Environment.getExternalStorageDirectory();
       // String filePath = externalStorageDirectory.getAbsolutePath()+"/mnt/shared/Image/15280997761425000.jpg";
        String filePath = "/mnt/shared/Image/15280997761425000.jpg";
        File file = new File(filePath);
        Log.d(TAG, "onCreate: "+file.exists());

        HashMap<String, Object> params = new HashMap<>();
        params.put("uid","71");
        params.put("file",file);
        OKHttpUtil.getInstance().uploadFile("https://www.zhaoapi.cn/file/upload", params, new RequestCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String string = response.body().string();
                    Log.d(TAG, "onResponse: "+string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Call call, IOException e) {
                Log.d(TAG, "failure: "+e);
            }
        });
    }
}
