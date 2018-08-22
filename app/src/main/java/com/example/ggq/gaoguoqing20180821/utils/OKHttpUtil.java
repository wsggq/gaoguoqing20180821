package com.example.ggq.gaoguoqing20180821.utils;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpUtil {
    private static OKHttpUtil okHttpUtil;
    private OkHttpClient okHttpClient;
    private static final String TAG = "OKHttpUtil";
    public static OKHttpUtil getInstance() {
        if(okHttpUtil == null){
            synchronized (OKHttpUtil.class){
                if(okHttpUtil == null){
                    okHttpUtil = new OKHttpUtil();
                }
            }
        }
        return okHttpUtil;
    }

    private OKHttpUtil() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(5,TimeUnit.SECONDS)
                .writeTimeout(5,TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .build();
    }

    /**
     * get封装
     * @param url
     * @param params
     * @param requestCallback
     */
    public void getData(String url, HashMap<String,String> params, final RequestCallback requestCallback){
        StringBuilder stringBuilder = new StringBuilder();
        String allUrl = "";
        for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
            stringBuilder.append("?").append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");
        }
        allUrl = url+stringBuilder.toString().substring(0,allUrl.length()-1);
        Log.d(TAG, "getData: "+allUrl);
        final Request request = new Request.Builder()
                .url(allUrl)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(requestCallback != null){
                    requestCallback.failure(call,e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(requestCallback != null){
                    requestCallback.onResponse(call,response);
                }
            }
        });
    }

    /**
     * post请求
     * @param url
     * @param params
     * @param requestCallback
     */
    public void postData(String url, HashMap<String,String> params, final RequestCallback requestCallback){
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null && params.size()>0){
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                builder.add(stringStringEntry.getKey(),stringStringEntry.getValue());
            }
        }
        final Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(requestCallback != null){
                    requestCallback.failure(call,e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(requestCallback != null){
                    requestCallback.onResponse(call,response);
                }
            }
        });
    }

    /**
     * 封装上传文件
     * @param url
     * @param params
     * @param requestCallback
     */
    public void uploadFile(String url, HashMap<String, Object> params, final RequestCallback requestCallback){
        //多表单上传的请求体对象,multipart/form-data
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置上传类型是表单形式
        builder.setType(MultipartBody.FORM);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            if(value instanceof File){
                File file = (File) value;
                //创建文件请求体
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
               // RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                builder.addFormDataPart(key,file.getName(),requestBody);
            }else {
                builder.addFormDataPart(key, (String) value);
            }
        }
        final Request request = new Request.Builder().post(builder.build()).url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(requestCallback != null){
                    requestCallback.failure(call,e);
                }
                URLEncoder.encode("url");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(requestCallback != null){
                    requestCallback.onResponse(call,response);
                }
            }
        });
    }
}
