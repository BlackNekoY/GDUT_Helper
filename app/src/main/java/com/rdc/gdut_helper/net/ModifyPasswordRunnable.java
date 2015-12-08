package com.rdc.gdut_helper.net;


import android.os.Bundle;

import com.rdc.gdut_helper.app.GDUTApplication;
import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.utils.HTMLUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ModifyPasswordRunnable extends BaseRunnable {

    private String password;
    private final String MODIFY_SUCCEED_KEY = "修改成功";
    private final String MODIFY_ERROR1 = "您的新密码过于简单，请重新输入";
    private final String MODIFY_ERROR2 = "新密码与登录的帐号不能相同！";


    public ModifyPasswordRunnable(TaskCallback callback, String password) {
        super(callback);
        this.password = password;
        urlStr = ConnectConfig.HOST + ConnectConfig.ModifyPassword.PATH_MODIFY_PASSWORD;
    }

    @Override
    public void run() {
        super.run();
        Bundle data = new Bundle();
        String reason = null;
        if (isConnected) {
            try {
                String result = read();
                isConnected = isCorrectResponse(result);
                if (isConnected) {
                    ConnectConfig.ModifyPassword.viewState = HTMLUtil.getViewState(result);
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(DEFAULT_TIME_OUT);
                    connection.setRequestProperty("Cookie", ConnectConfig.cookie);
                    connection.setRequestProperty("Referer", ConnectConfig.HOST + ConnectConfig.ModifyPassword.PATH_MODIFY_PASSWORD);
                    connection.setDoOutput(true);

                    StringBuilder sb = new StringBuilder();
                    sb.append(ConnectConfig.ModifyPassword.PARAM_VIEW_STATE + "=" + URLEncoder.encode(ConnectConfig.ModifyPassword.viewState, "iso-8859-1") + "&")
                            .append(ConnectConfig.ModifyPassword.PARAM_TEXTBOX2 + "=" + GDUTApplication.stuPsw + "&")
                            .append(ConnectConfig.ModifyPassword.PARAM_TEXTBOX3 + "=" + password + "&")
                            .append(ConnectConfig.ModifyPassword.PARAM_TEXTBOX4 + "=" + password + "&")
                            .append(ConnectConfig.ModifyPassword.PARAM_BUTTON1 + "=" + URLEncoder.encode("修改", "gb2312"));

                    connection.getOutputStream().write(sb.toString().getBytes());

                    sb = new StringBuilder();
                    String line = null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gb2312"));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    isConnected = sb.toString().contains(MODIFY_SUCCEED_KEY);
                    if (!isConnected) {
                        if (sb.toString().contains(MODIFY_ERROR1)) {
                            reason = MODIFY_ERROR1;
                        } else if (sb.toString().contains(MODIFY_ERROR2)) {
                            reason = MODIFY_ERROR2;
                        } else {
                            reason = "修改失败";
                        }
                    }
                }
            } catch (IOException e) {
                isConnected = false;
                reason = "网络不给力啊~";
                e.printStackTrace();
            }
        } else {
            reason = "网络不给力啊~";
        }
        data.putString("reason", reason);
        callback.onResult(isConnected, data);
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
        return !(response.contains(ERROR_PAGE_KEY) || response.contains(LOGIN_PAGE_KEY) || response.contains(MODIFY_ERROR1));
    }
}
