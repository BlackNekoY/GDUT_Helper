package com.rdc.gdut_helper.net;


import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.utils.HTMLUtil;

import java.io.IOException;

public class MainScoreRunnable extends BaseRunnable {

    public MainScoreRunnable(TaskCallback callback) {
        super(callback);

        urlStr = ConnectConfig.HOST + ConnectConfig.MainScore.PATH_MAIN_SCORE;
    }

    @Override
    public void run() {
        super.run();
        if (isConnected) {
            try {
                String result = read();
                isConnected = isCorrectResponse(result);
                if (isConnected) {
                    ConnectConfig.MainScore.viewState = HTMLUtil.getViewState(result);
                    isConnected = true;
                }
            } catch (IOException e) {
                isConnected = false;
                e.printStackTrace();
            }
            callback.onResult(isConnected, null);
        } else

        {
            callback.onResult(false, null);
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
