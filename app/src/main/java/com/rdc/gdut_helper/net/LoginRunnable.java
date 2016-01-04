package com.rdc.gdut_helper.net;

import android.os.Bundle;

import com.rdc.gdut_helper.constant.ConnectConfig;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * 登录Runnable,提交表单
 */
public class LoginRunnable extends BaseRunnable {

    private String stuNum;
    private String stuPsw;
    private String checkCode;
    private String radioButton = "学生";

    private final static String ERROR_CHECKCODE_KEY = "验证码不正确";
    private final static String ERROR_PASSWORD_KEY = "密码错误";

    public LoginRunnable(String stuNum, String stuPsw, String checkCode, TaskCallback callback) {
        super(callback);
        this.stuNum = stuNum;
        this.stuPsw = stuPsw;
        this.checkCode = checkCode;

        urlStr = ConnectConfig.HOST + ConnectConfig.Login.PATH_LOGIN;
    }

    @Override
    public void run() {
        super.run();
        Bundle data = new Bundle();
        String reason = null;
        if (isConnected) {
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(ConnectConfig.Login.PARAM_VIEW_STATE + "=" + ConnectConfig.WelcomePage.viewState + "&")
                        .append(ConnectConfig.Login.PARAM_USER_NUM + "=" + stuNum + "&")
                        .append(ConnectConfig.Login.PARAM_USER_PSW + "=" + stuPsw + "&")
                        .append(ConnectConfig.Login.PARAM_RADIO_BUTTON + "=" + URLEncoder.encode(radioButton, "gb2312") + "&")
                        .append(ConnectConfig.Login.PARAM_CHECK_CODE + "=" + checkCode + "&")
                        .append(ConnectConfig.Login.PARAM_BUTTON + "=&")
                        .append(ConnectConfig.Login.PARAM_LANGUAGE + "=&")
                        .append(ConnectConfig.Login.PARAM_HID_PDRS + "=&")
                        .append(ConnectConfig.Login.PARAM_HID_SC + "=");
                conn.getOutputStream().write(sb.toString().getBytes());
                String result = read();
                isConnected = isCorrectResponse(result);

                if (result.contains(ERROR_CHECKCODE_KEY)) {
                    reason = "验证码错误";
                } else if (result.contains(ERROR_PASSWORD_KEY)) {
                    reason = "密码错误";
                } else if (result.contains(ERROR_PAGE_KEY) || result.contains(LOGIN_PAGE_KEY)) {
                    reason = "教务系统网络有点问题";
                }
            } catch (IOException e) {
                reason = "网络状态不佳";
                isConnected = false;
                e.printStackTrace();
            }
        } else {
            reason = "网络状态不佳";
        }
        data.putString("reason", reason);
        callback.onResult(isConnected, data);
    }

    @Override
    protected void initConnection() throws IOException {
        conn.setRequestMethod("POST");
        conn.setReadTimeout(DEFAULT_TIME_OUT);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Cookie", ConnectConfig.cookie);
        conn.setRequestProperty("Referer", ConnectConfig.HOST);
        conn.setDoOutput(true);
    }

    @Override
    protected boolean isCorrectResponse(String response) {
        return !(response.contains(ERROR_PAGE_KEY) || response.contains(LOGIN_PAGE_KEY)
                || response.equals(ERROR_CHECKCODE_KEY) || response.equals(ERROR_PASSWORD_KEY));
    }

}
