package com.rdc.gdut_helper.net;

import android.os.Bundle;

import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.model.Course;
import com.rdc.gdut_helper.utils.HTMLUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class QueryScoreRunnable extends BaseRunnable {

    public static final int TYPE_TERM = 1 << 0;
    public static final int TYPE_YEAR = 1 << 1;
    public static final int TYPE_ALL = 1 << 2;

    private final static String BUTTON1 = "按学期查询";
    private final static String BUTTON5 = "按学年查询";
    private final static String BUTTON2 = "在校学习成绩查询";

    private String buttonKey = ConnectConfig.QueryScore.PARAM_BUTTON_2;
    private String buttonValue = BUTTON2;
    private String ddlXn;
    private String ddlXq;

    public QueryScoreRunnable(TaskCallback callback, int type, String ddlXn, String ddlXq) {
        super(callback);
        if (ddlXn.equals("学年")) {

        }
        this.ddlXn = ddlXn.equals("学年") ? null : ddlXn;
        this.ddlXq = ddlXq.equals("学期") ? null : ddlXq;
        try {
            switch (type) {
                case TYPE_TERM:
                    buttonKey = ConnectConfig.QueryScore.PARAM_BUTTON_1;
                    buttonValue = BUTTON1;
                    break;
                case TYPE_YEAR:
                    buttonKey = ConnectConfig.QueryScore.PARAM_BUTTON_5;
                    buttonValue = BUTTON5;
                    break;
                case TYPE_ALL:
                    buttonKey = ConnectConfig.QueryScore.PARAM_BUTTON_2;
                    buttonValue = BUTTON2;
                    break;
            }
        } catch (Exception e) {

        }

        urlStr = ConnectConfig.HOST + ConnectConfig.MainScore.PATH_MAIN_SCORE;
    }

    @Override
    public void run() {
        super.run();

        if (isConnected) {
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(ConnectConfig.QueryScore.PARAM_VIEW_STATE + "=" + URLEncoder.encode(ConnectConfig.MainScore.viewState, "iso-8859-1") + "&")
                        .append(ConnectConfig.QueryScore.PARAM_TXT_QSCJ + "=0&")
                        .append(ConnectConfig.QueryScore.PARAM_TXT_ZZCJ + "=100&")
                        .append(buttonKey + "=" + URLEncoder.encode(buttonValue, "gb2312") + "&")
                        .append(ConnectConfig.QueryScore.PARAM_DDLXN + "=" + ddlXn + "&")
                        .append(ConnectConfig.QueryScore.PARAM_DDLXQ + "=" + ddlXq);
                conn.getOutputStream().write(sb.toString().getBytes());
                String result = read();
                isConnected = isCorrectResponse(result);
                if (isConnected) {
                    Bundle data = new Bundle();
                    ArrayList<Course> list = HTMLUtil.getCourseList(result);
                    data.putParcelableArrayList("courseList", list);
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
        conn.setRequestMethod("POST");
        conn.setReadTimeout(DEFAULT_TIME_OUT);
        conn.setRequestProperty("Cookie", ConnectConfig.cookie);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("Referer", ConnectConfig.HOST + ConnectConfig.MainScore.PATH_MAIN_SCORE);
        conn.setUseCaches(false);
        conn.setDoOutput(true);
    }

    @Override
    protected boolean isCorrectResponse(String response) {
        return !(response.contains(ERROR_PAGE_KEY) || response.contains(LOGIN_PAGE_KEY));
    }
}
