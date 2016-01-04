package com.rdc.gdut_helper.net;

import android.os.Bundle;

import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.model.StudentTest;
import com.rdc.gdut_helper.utils.HTMLUtil;

import java.io.IOException;
import java.util.ArrayList;

public class StudentTestGetRunnable extends BaseRunnable {

    public StudentTestGetRunnable(TaskCallback callback) {
        super(callback);
        urlStr = ConnectConfig.HOST + ConnectConfig.StudentTest.PATH_STUDENT_TEST;
    }

    @Override
    public void run() {
        super.run();
        if (isConnected) {
            try {
                String result = read();
                isConnected = isCorrectResponse(result);
                if (isConnected) {
                    ConnectConfig.StudentTest.viewState = HTMLUtil.getViewState(result);
                    ArrayList<StudentTest> list = HTMLUtil.getStudentTestList(result);
                    Bundle data = new Bundle();
                    data.putParcelableArrayList("student_test_list", list);
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
        conn.setReadTimeout(DEFAULT_TIME_OUT);
        conn.setRequestProperty("Cookie", ConnectConfig.cookie);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Referer", ConnectConfig.HOST + ConnectConfig.MainPage.PATH_MAIN_PAGE);
    }

    @Override
    protected boolean isCorrectResponse(String response) {
        return !(response.contains(ERROR_PAGE_KEY) || response.contains(LOGIN_PAGE_KEY));
    }
}
