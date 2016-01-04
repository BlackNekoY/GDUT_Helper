package com.rdc.gdut_helper.net;

import android.os.Bundle;

import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.model.StudentTest;
import com.rdc.gdut_helper.utils.HTMLUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class StudentTestPostRunnable extends BaseRunnable {

    private String eventTarget;
    private String xnd;
    private String xqd;

    public StudentTestPostRunnable(TaskCallback callback, String eventTarget, String xnd, String xqd) {
        super(callback);
        urlStr = ConnectConfig.HOST + ConnectConfig.StudentTest.PATH_STUDENT_TEST;
        this.eventTarget = eventTarget;
        this.xnd = xnd;
        this.xqd = xqd;
    }


    @Override
    public void run() {
        super.run();

        if (isConnected) {
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(ConnectConfig.StudentTest.PARAM_EVENTTARGET + "=" + eventTarget + "&")
                        .append(ConnectConfig.StudentTest.PARAM_EVENTARGUMENT + "=&")
                        .append(ConnectConfig.StudentTest.PARAM_VIEW_STATE + "=" + URLEncoder.encode(ConnectConfig.StudentTest.viewState, "iso-8859-1") + "&")
                        .append(ConnectConfig.StudentTest.PARAM_XND + "=" + xnd + "&")
                        .append(ConnectConfig.StudentTest.PARAM_XQD + "=" + xqd);
                conn.getOutputStream().write(sb.toString().getBytes());

                String result = read();
                isConnected = isCorrectResponse(result);
                if(isConnected) {
                    ConnectConfig.StudentTest.viewState = HTMLUtil.getViewState(result);
                    ArrayList<StudentTest> list = HTMLUtil.getStudentTestList(result);
                    Bundle data = new Bundle();
                    data.putParcelableArrayList("student_test_list", list);
                    callback.onResult(true, data);
                }else {
                    callback.onResult(false, null);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                isConnected = false;
                callback.onResult(false,null);
            } catch (IOException e) {
                e.printStackTrace();
                isConnected = false;
                callback.onResult(false, null);
            }
        } else {
            isConnected = false;
            callback.onResult(false,null);
        }
    }

    @Override
    protected void initConnection() throws IOException {
        conn.setRequestMethod("POST");
        conn.setReadTimeout(DEFAULT_TIME_OUT);
        conn.setDoOutput(true);
        conn.setRequestProperty("Cookie", ConnectConfig.cookie);
        conn.setRequestProperty("Referer", ConnectConfig.HOST + ConnectConfig.StudentTest.PATH_STUDENT_TEST);
    }

    @Override
    protected boolean isCorrectResponse(String response) {
        return !(response.contains(ERROR_PAGE_KEY) || response.contains(LOGIN_PAGE_KEY));
    }
}
