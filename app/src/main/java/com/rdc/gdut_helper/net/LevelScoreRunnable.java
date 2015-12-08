package com.rdc.gdut_helper.net;

import android.os.Bundle;

import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.model.LevelTest;
import com.rdc.gdut_helper.utils.HTMLUtil;

import java.io.IOException;
import java.util.ArrayList;

public class LevelScoreRunnable extends BaseRunnable {

    public LevelScoreRunnable(TaskCallback callback) {
        super(callback);
        urlStr = ConnectConfig.HOST + ConnectConfig.LevelTest.PATH_LEVEL_TEST;
    }

    @Override
    public void run() {
        super.run();
        if (isConnected) {
            try {
                String result = read();
                isConnected = isCorrectResponse(result);
                if (isConnected) {
                    //获取等级考试成绩
                    ArrayList<LevelTest> list = HTMLUtil.getLevelTestList(result);
                    Bundle data = new Bundle();
                    data.putParcelableArrayList("level_test_list", list);
                    callback.onResult(true, data);
                } else {
                    callback.onResult(false, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                isConnected = false;
                callback.onResult(false, null);
            }
        } else {
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
