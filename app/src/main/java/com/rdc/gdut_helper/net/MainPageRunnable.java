package com.rdc.gdut_helper.net;


import com.rdc.gdut_helper.app.GDUTApplication;
import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.utils.HTMLUtil;

import java.io.IOException;
import java.net.URLEncoder;

public class MainPageRunnable extends BaseRunnable {

    public MainPageRunnable(TaskCallback callback) {
        super(callback);
        urlStr = ConnectConfig.HOST + ConnectConfig.MainPage.PATH_MAIN_PAGE;
    }

    @Override
    public void run() {
        super.run();
        if (isConnected) {
            try {
                String result = read();
                isConnected = isCorrectResponse(result);
                if (isConnected) {
                    GDUTApplication.stuName = HTMLUtil.getStuName(result);
                    GDUTApplication.stuNameEncode = URLEncoder.encode(GDUTApplication.stuName, "gb2312");
                }
            } catch (IOException e) {
                isConnected = false;
                e.printStackTrace();
            }
            callback.onResult(isConnected, null);
        } else {
            callback.onResult(false, null);
        }
    }

    @Override
    protected void initConnection() throws IOException {
        conn.setRequestMethod("GET");
        conn.setReadTimeout(DEFAULT_TIME_OUT);
        conn.setRequestProperty("Cookie", ConnectConfig.cookie);
        conn.setRequestProperty("Referer", ConnectConfig.HOST );
    }

    @Override
    protected boolean isCorrectResponse(String response) {
        return !(response.contains(ERROR_PAGE_KEY) || response.contains(LOGIN_PAGE_KEY));
    }
}
