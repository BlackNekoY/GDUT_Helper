package com.rdc.gdut_helper.ui;

import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.app.GDUTApplication;
import com.rdc.gdut_helper.net.BaseRunnable;
import com.rdc.gdut_helper.net.CheckCodeRunnable;
import com.rdc.gdut_helper.net.LoginRunnable;
import com.rdc.gdut_helper.net.MainPageRunnable;
import com.rdc.gdut_helper.net.WelcomePageRunnable;
import com.rdc.gdut_helper.service.RefreshService;
import com.rdc.gdut_helper.ui.base.ToolbarActivity;
import com.rdc.gdut_helper.utils.L;
import com.rdc.gdut_helper.utils.ProgressDialogInflater;
import com.rdc.gdut_helper.view.LoginView;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends ToolbarActivity implements View.OnClickListener {

    private ExecutorService mThreadPool = Executors.newFixedThreadPool(5);
    private LoginView mLoginView;
    private AlertDialog mLoginDialog;
    private MenuItem mMenuItem;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ((GDUTApplication) getApplication()).initSharePreferences();
        initListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_status:
                if (!GDUTApplication.hasLogin) {
                    initLoginDialog();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenuItem = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        refreshLoginStatus();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        stopRefreshService();
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void refreshLoginStatus() {
        if (mMenuItem != null) {
            mMenuItem.setTitle(GDUTApplication.hasLogin ? R.string.has_login : R.string.has_not_login);
        }

    }

    private void startRefreshService() {
        Intent intent = new Intent(this, RefreshService.class);
        intent.setAction(RefreshService.ACTION_REFRESH_MAIN_PAGE);
        startService(intent);
    }

    private void stopRefreshService() {
        GDUTApplication.hasLogin = false;
    }


    private void loadWelcomePage() {
        mThreadPool.execute(new WelcomePageRunnable(new WelcomePageCallback()));
    }

    private void initLoginDialog() {
        loadWelcomePage();
        mLoginView = new LoginView(this);
        mLoginView.setListener(new LoginViewListener());
        mLoginDialog = new AlertDialog.Builder(this)
                .setView(mLoginView)
                .setPositiveButton(R.string.login, null)
                .setNegativeButton(R.string.cancel, null).setCancelable(false).create();
        mLoginDialog.show();
        mLoginDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginView.isMsgComplete()) {
                    ProgressDialogInflater.showProgressDialog(MainActivity.this, "登录中...");
                    mThreadPool.execute(new LoginRunnable(mLoginView.getStuNumber(), mLoginView.getStuPassword(), mLoginView.getCheckCode(), new LoginCallback()));
                } else {
                    mLoginView.showErrorText("信息不完整");
                }
            }
        });
    }

    private void initListener() {
        List<View> viewList = new ArrayList<>();
        viewList.add($(R.id.rl_profile));
        viewList.add($(R.id.rl_schedule));
        viewList.add($(R.id.rl_course_score));
        viewList.add($(R.id.rl_level_score));
        viewList.add($(R.id.rl_test_time));
        viewList.add($(R.id.rl_setting));

        for (int i = 0; i < viewList.size(); i++) {
            viewList.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_profile:
                break;
            case R.id.rl_schedule:
                break;
            case R.id.rl_course_score:
                launchActivity(CourseScoreActivity.class);
                break;
            case R.id.rl_level_score:
                launchActivity(LevelScoreActivity.class);
                break;
            case R.id.rl_test_time:
                launchActivity(TestDetailActivity.class);
                break;
            case R.id.rl_setting:
                launchActivity(SettingActivity.class);
                break;
        }
    }

    private class LoginViewListener implements LoginView.LoginViewClickListener {

        @Override
        public void getCheckCode() {
            mThreadPool.execute(new CheckCodeRunnable(new CheckCodeCallBack()));
        }
    }

    private class WelcomePageCallback implements BaseRunnable.TaskCallback {

        @Override
        public void onResult(boolean isConnected, Bundle data) {
            if (isConnected) {
                mThreadPool.execute(new CheckCodeRunnable(new CheckCodeCallBack()));
            } else {

            }
        }
    }

    private class CheckCodeCallBack implements BaseRunnable.TaskCallback {
        @Override
        public void onResult(final boolean isConnected, final Bundle data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isConnected) {
                        Bitmap bitmap = data.getParcelable("bitmap");
                        mLoginView.setCheckCodeImage(bitmap);
                    }
                }
            });
        }
    }

    private class LoginCallback implements BaseRunnable.TaskCallback {
        @Override
        public void onResult(final boolean isConnected, final Bundle data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isConnected) {
                        L.e("LoginSucceed");
                        GDUTApplication.stuNum = mLoginView.getStuNumber();
                        GDUTApplication.stuPsw = mLoginView.getStuPassword();
                        mLoginDialog.dismiss();
                        mLoginView = null;
                        mThreadPool.execute(new MainPageRunnable(new MainPageCallback()));

                    } else {
                        L.e("LoginFailed");
                        String reason = data.getString("reason");
                        ProgressDialogInflater.dismiss();
                        mLoginView.showErrorText(reason);
                        mMenuItem.setTitle(R.string.has_not_login);
                        mThreadPool.execute(new WelcomePageRunnable(new WelcomePageCallback()));
                    }
                }
            });

        }
    }

    private class MainPageCallback implements BaseRunnable.TaskCallback {

        @Override
        public void onResult(final boolean isConnected, Bundle data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogInflater.dismiss();
                    if (isConnected) {
                        GDUTApplication.hasLogin = true;
                        startRefreshService();
                    } else {
                        GDUTApplication.hasLogin = false;
                    }
                    refreshLoginStatus();
                }
            });
        }
    }

}
