package com.rdc.gdut_helper.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
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

    private ImageView mIvCheckCode;
    private TextInputLayout mInputLayoutStuNum;
    private TextInputLayout mInputLayoutStuPsw;
    private TextInputLayout mInputLayoutCheckCode;
    private TextView mTvError;
    private ExecutorService mThreadPool = Executors.newFixedThreadPool(5);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);

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
        $(R.id.btn_login).setOnClickListener(this);
    }

    public boolean isMsgComplete() {
        return !TextUtils.isEmpty(mInputLayoutStuNum.getEditText().getText().toString().trim()) &&
                !TextUtils.isEmpty(mInputLayoutStuPsw.getEditText().getText().toString().trim()) &&
                !TextUtils.isEmpty(mInputLayoutCheckCode.getEditText().getText().toString().trim());

    }

    public String getStuNumber() {
        return mInputLayoutStuNum.getEditText().getText().toString().trim();
    }

    public String getStuPassword() {
        return mInputLayoutStuPsw.getEditText().getText().toString().trim();
    }

    public String getCheckCode() {
        return mInputLayoutCheckCode.getEditText().getText().toString().trim();
    }

    public void setCheckCodeImage(Bitmap bitmap) {
        mIvCheckCode.setImageBitmap(bitmap);
    }

    public void showErrorText(CharSequence text) {
        mTvError.setVisibility(View.VISIBLE);
        mTvError.setText(text);
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
            case R.id.btn_login:
                if (isMsgComplete()) {
                    ProgressDialogInflater.showProgressDialog(this, "正在登录");
                    mThreadPool.execute(new LoginRunnable(getStuNumber(), getStuPassword(), getCheckCode(), new LoginCallback()));
                } else {
                    showErrorText("信息不完整");
                }
                break;
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
