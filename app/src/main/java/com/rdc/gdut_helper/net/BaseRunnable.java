package com.rdc.gdut_helper.net;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseRunnable implements Runnable {

    protected String urlStr;
    protected HttpURLConnection conn;
    protected boolean isConnected;
    protected final int DEFAULT_TIME_OUT = 5 * 1000;
    protected TaskCallback callback;

    protected final static String ERROR_PAGE_KEY = "/zdy.htm?aspxerrorpath=";
    protected final static String LOGIN_PAGE_KEY = "<title>欢迎使用正方教务管理系统！请登录</title>";


    public BaseRunnable(TaskCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            initConnection();
            isConnected = true;
        } catch (MalformedURLException e) {
            isConnected = false;
            e.printStackTrace();
        } catch (IOException e) {
            isConnected = false;
            e.printStackTrace();
        }
    }

    protected abstract void initConnection() throws IOException;

    protected abstract boolean isCorrectResponse(String response);

    protected String read() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gb2312"));
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    public interface TaskCallback {
        public void onResult(boolean isConnected, Bundle data);
    }
}
