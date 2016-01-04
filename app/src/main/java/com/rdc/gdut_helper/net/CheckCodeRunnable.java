package com.rdc.gdut_helper.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.rdc.gdut_helper.constant.ConnectConfig;

import java.io.IOException;

/**
 * 验证码Runnable
 */
public class CheckCodeRunnable extends BaseRunnable {

    public CheckCodeRunnable(TaskCallback callback) {
        super(callback);
        urlStr = ConnectConfig.HOST + ConnectConfig.CheckCode.PATH_CHECK_CODE;
    }

    @Override
    public void run() {
        super.run();
        if (isConnected) {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                Bundle data = new Bundle();
                data.putParcelable("bitmap", bitmap);
                isConnected = true;
                callback.onResult(true, data);
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
    }

    @Override
    protected boolean isCorrectResponse(String response) {
        return false;
    }
}
