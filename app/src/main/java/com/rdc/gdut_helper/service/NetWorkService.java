package com.rdc.gdut_helper.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rdc.gdut_helper.net.BaseRunnable;
import com.rdc.gdut_helper.utils.L;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by blackwhite on 15-12-7.
 */
public class NetWorkService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NetWorkService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        L.e("");
    }
}
