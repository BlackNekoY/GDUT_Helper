package com.rdc.gdut_helper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.app.GDUTApplication;
import com.rdc.gdut_helper.service.RefreshService;
import com.rdc.gdut_helper.ui.base.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ToolbarActivity implements View.OnClickListener {

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
                if (!hasLogin()) {
                    launchActivity(LoginActivity.class, LoginActivity.REQUEST_CODE);
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
            mMenuItem.setTitle(hasLogin() ? R.string.has_login : R.string.has_not_login);
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
                if (hasLogin()) {
                }
                break;
            case R.id.rl_schedule:
                if (!hasLogin()) {

                }
                break;
            case R.id.rl_course_score:
                if (hasLogin()) {
                    launchActivity(CourseScoreActivity.class);
                } else {
                    launchActivity(LoginActivity.class, LoginActivity.REQUEST_CODE);
                }
                break;
            case R.id.rl_level_score:
                if (hasLogin()) {
                    launchActivity(LevelScoreActivity.class);
                } else {
                    launchActivity(LoginActivity.class, LoginActivity.REQUEST_CODE);
                }
                break;
            case R.id.rl_test_time:
                if (hasLogin()) {
                    launchActivity(TestDetailActivity.class);
                } else {
                    launchActivity(LoginActivity.class, LoginActivity.REQUEST_CODE);
                }
                break;
            case R.id.rl_setting:
                launchActivity(SettingActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginActivity.REQUEST_CODE:
                refreshLoginStatus();
                if (hasLogin()) {
                    startRefreshService();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
