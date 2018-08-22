package com.example.ggq.gaoguoqing20180821.utils;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Response;

public interface RequestCallback {
    void onResponse(Call call, Response response);
    void failure(Call call, IOException e);
}
