package com.rdc.gdut_helper.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.rdc.gdut_helper.app.GDUTApplication;
import com.rdc.gdut_helper.net.BaseRunnable;
import com.rdc.gdut_helper.net.MainPageRunnable;
import com.rdc.gdut_helper.utils.L;

/**
 * Created by blackwhite on 15-12-7.
 */
public class RefreshService extends IntentService {

    private static final String TAG = "RefreshService";
    public static final String ACTION_REFRESH_MAIN_PAGE = "action_refresh_main_page";
    private MainPageRunnable mRunnable;
    private static final long DEFAULT_REFRESH_TIME = 1 * 1000;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RefreshService(String name) {
        super(name);
    }

    public RefreshService(){
        super(TAG);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if(action.equals(ACTION_REFRESH_MAIN_PAGE)) {
            startRefresh();
        }
    }

    private void startRefresh() {
        mRunnable = new MainPageRunnable(new MainPageCallback());
        while(GDUTApplication.hasLogin) {
            if(!GDUTApplication.isOpenRefreshService)
                continue;
            try {
                L.e("刷新");
                mRunnable.run();
                Thread.sleep(DEFAULT_REFRESH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MainPageCallback implements BaseRunnable.TaskCallback {

        @Override
        public void onResult(boolean isConnected, Bundle data) {
            GDUTApplication.hasLogin = isConnected;
        }
    }
}
