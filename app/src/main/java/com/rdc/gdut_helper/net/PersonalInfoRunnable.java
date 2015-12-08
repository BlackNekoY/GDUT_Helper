package com.rdc.gdut_helper.net;


import com.rdc.gdut_helper.constant.ConnectConfig;

import java.io.IOException;

public class PersonalInfoRunnable extends BaseRunnable {

    public PersonalInfoRunnable(TaskCallback callback) {
        super(callback);
        urlStr = ConnectConfig.HOST + ConnectConfig.PersonalInfo.PATH_INFO;
    }

    @Override
    public void run() {
        super.run();
        if(isConnected) {
            try {
                String result = read();
                isConnected = isCorrectResponse(result);
            } catch (IOException e) {
                isConnected = false;
                e.printStackTrace();
            }
            callback.onResult(isConnected,null);
        }else {
            callback.onResult(false,null);
        }
    }

    @Override
    protected void initConnection() throws IOException {
        conn.setRequestMethod("GET");
        conn.setReadTimeout(DEFAULT_TIME_OUT);
        conn.setRequestProperty("Cookie", ConnectConfig.cookie);
        conn.setRequestProperty("Referer", ConnectConfig.HOST + ConnectConfig.MainPage.PATH_MAIN_PAGE);
    }

    @Override
    protected boolean isCorrectResponse(String response) {
        return !(response.contains(ERROR_PAGE_KEY) || response.contains(LOGIN_PAGE_KEY));
    }
}
