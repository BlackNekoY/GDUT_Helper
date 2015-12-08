package com.rdc.gdut_helper.net;


import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.utils.HTMLUtil;

import java.io.IOException;
import java.net.ProtocolException;

/**
 * 登录界面Runnable,获取网站ViewState
 */
public class WelcomePageRunnable extends BaseRunnable {

    public WelcomePageRunnable(TaskCallback callback) {
        super(callback);
        urlStr = ConnectConfig.HOST + ConnectConfig.Login.PATH_LOGIN;
    }

    @Override
    public void run() {
        super.run();
        if(isConnected) {
            try {
                ConnectConfig.cookie = conn.getHeaderField("Set-Cookie");
                String result = read();
                ConnectConfig.WelcomePage.viewState = HTMLUtil.getViewState(result);

                isConnected = true;
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
    protected void initConnection() throws ProtocolException {
        conn.setRequestMethod("GET");
        conn.setReadTimeout(DEFAULT_TIME_OUT);
        conn.setInstanceFollowRedirects(false);
    }

    @Override
    protected boolean isCorrectResponse(String response) {
        return false;
    }

}
