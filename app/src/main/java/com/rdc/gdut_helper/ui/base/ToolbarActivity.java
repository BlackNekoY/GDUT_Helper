package com.rdc.gdut_helper.ui.base;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.app.GDUTApplication;

/**
 * Created by blackwhite on 15-12-7.
 */
public class ToolbarActivity extends BaseActivity {

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = $(R.id.toolbar);
        mAppBarLayout = $(R.id.appbar_layout);

        if (mToolbar == null) {
            throw new IllegalStateException("No Toolbar!");
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    public void setTitle(CharSequence title) {
        mToolbar.setTitle(title);
    }

    public void setTitle(int resId) {
        mToolbar.setTitle(resId);
    }

    protected boolean hasLogin() {
        return GDUTApplication.hasLogin;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
