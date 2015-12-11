package com.rdc.gdut_helper.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.app.GDUTApplication;
import com.rdc.gdut_helper.net.BaseRunnable;
import com.rdc.gdut_helper.net.CheckCodeRunnable;
import com.rdc.gdut_helper.net.LoginRunnable;
import com.rdc.gdut_helper.net.MainPageRunnable;
import com.rdc.gdut_helper.net.WelcomePageRunnable;
import com.rdc.gdut_helper.ui.base.ToolbarActivity;
import com.rdc.gdut_helper.utils.ProgressDialogInflater;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by blackwhite on 15-12-8.
 */
public class LoginActivity extends ToolbarActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 1;
    private final int STATUS_NORMAL = 0;
    private final int STATUS_SYNC = 1;

    private ImageView mIvCheckCode;
    private TextInputLayout mInputLayoutStuNum;
    private TextInputLayout mInputLayoutStuPsw;
    private TextInputLayout mInputLayoutCheckCode;
    private TextView mTvError;
    private MenuItem mMenuItem;
    private ExecutorService mThreadPool = Executors.newFixedThreadPool(5);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.login);

        init();
        loadWelcomePage();
    }

    private void loadWelcomePage() {
        mThreadPool.execute(new WelcomePageRunnable(new WelcomePageCallback()));
    }

    private void init() {

        mIvCheckCode = $(R.id.iv_checkcode);
        mInputLayoutStuNum = $(R.id.inputlayout_stu_num);
        mInputLayoutStuPsw = $(R.id.inputlayout_stu_psw);
        mInputLayoutCheckCode = $(R.id.inputlayout_check_code);
        mTvError = $(R.id.tv_error);

        if (GDUTApplication.isRemember) {
            mInputLayoutStuNum.getEditText().setText(GDUTApplication.stuNum);
            mInputLayoutStuPsw.getEditText().setText(GDUTApplication.stuPsw);
        }
        $(R.id.tv_change_checkcode).setOnClickListener(this);
    }

    private boolean isMsgComplete() {
        return !TextUtils.isEmpty(mInputLayoutStuNum.getEditText().getText().toString().trim()) &&
                !TextUtils.isEmpty(mInputLayoutStuPsw.getEditText().getText().toString().trim()) &&
                !TextUtils.isEmpty(mInputLayoutCheckCode.getEditText().getText().toString().trim());

    }

    private String getStuNumber() {
        return mInputLayoutStuNum.getEditText().getText().toString().trim();
    }

    private String getStuPassword() {
        return mInputLayoutStuPsw.getEditText().getText().toString().trim();
    }

    private String getCheckCode() {
        return mInputLayoutCheckCode.getEditText().getText().toString().trim();
    }

    private void setCheckCodeImage(Bitmap bitmap) {
        mIvCheckCode.setImageBitmap(bitmap);
    }

    private void showErrorText(CharSequence text) {
        mTvError.setVisibility(View.VISIBLE);
        mTvError.setText(text);
        mInputLayoutCheckCode.getEditText().setText("");
    }

    private boolean login() {
        if (isMsgComplete()) {
//            setMenuIcon(STATUS_SYNC);
            ProgressDialogInflater.showProgressDialog(this, "正在登录");
            mThreadPool.execute(new LoginRunnable(getStuNumber(), getStuPassword(), getCheckCode(), new LoginCallback()));
            return true;
        }
        return false;
    }

    private void setMenuIcon(int status) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.image_done, null);

        if (status == STATUS_NORMAL) {
            iv.setImageResource(R.drawable.ic_done_white_24dp);
        } else if (status == STATUS_SYNC) {
            iv.setImageResource(R.drawable.ic_sync_white_24dp);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
            animation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(animation);
        }
        mMenuItem.setActionView(iv);

    }

    public void hideErrorText() {
        mTvError.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_checkcode:
                mThreadPool.execute(new CheckCodeRunnable(new CheckCodeCallBack()));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        mMenuItem = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                if (!login()) {
                    showErrorText("信息不完整");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
                        setCheckCodeImage(bitmap);
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
                        GDUTApplication.stuNum = getStuNumber();
                        GDUTApplication.stuPsw = getStuPassword();
                        ProgressDialogInflater.setMessage("正在获取首页");
                        mThreadPool.execute(new MainPageRunnable(new MainPageCallback()));
                    } else {
//                        setMenuIcon(STATUS_NORMAL);
                        String reason = data.getString("reason");
                        ProgressDialogInflater.dismiss();
                        showErrorText(reason);
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
//                    setMenuIcon(STATUS_NORMAL);
                    ProgressDialogInflater.dismiss();
                    if (isConnected) {
                        GDUTApplication.hasLogin = true;
                        setResult(0);
                        finish();
                    } else {
                        GDUTApplication.hasLogin = false;
                        showErrorText("获取首页失败，校园网又大姨妈了");
                    }
                }
            });
        }
    }
}
