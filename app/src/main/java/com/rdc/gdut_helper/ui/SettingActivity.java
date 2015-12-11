package com.rdc.gdut_helper.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.test.mock.MockApplication;
import android.view.View;
import android.widget.RelativeLayout;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.app.GDUTApplication;
import com.rdc.gdut_helper.net.BaseRunnable;
import com.rdc.gdut_helper.net.ModifyPasswordRunnable;
import com.rdc.gdut_helper.service.RefreshService;
import com.rdc.gdut_helper.ui.base.ToolbarActivity;
import com.rdc.gdut_helper.utils.ProgressDialogInflater;
import com.rdc.gdut_helper.utils.SnackbarUtil;
import com.rdc.gdut_helper.view.ModifyPasswordView;

/**
 * Created by blackwhite on 15-12-8.
 */
public class SettingActivity extends ToolbarActivity implements View.OnClickListener {

    private SwitchCompat mSwitchRemeber;
    private ModifyPasswordView mModifyView;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.settings);

        mSwitchRemeber = (SwitchCompat) ((RelativeLayout) $(R.id.rl_remember)).getChildAt(1);
        mSwitchRemeber.setChecked(GDUTApplication.isRemember);

        $(R.id.rl_remember).setOnClickListener(this);
        $(R.id.rl_modify_password).setOnClickListener(this);
        $(R.id.rl_github_project).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if(hasLogin()) {
            mSwitchRemeber.setChecked(GDUTApplication.isRemember);
        }else {
            mSwitchRemeber.setChecked(false);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(hasLogin()) {
            GDUTApplication.saveSettings();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_remember:
                mSwitchRemeber.setChecked(!mSwitchRemeber.isChecked());
                GDUTApplication.isRemember = mSwitchRemeber.isChecked();
                break;
            case R.id.rl_modify_password:
                if (hasLogin()) {
                    modifyPassword();
                } else {
                    launchActivity(LoginActivity.class, LoginActivity.REQUEST_CODE);
                }
                break;
            case R.id.rl_github_project:
                viewProjectUrl();
                break;
        }
    }

    private void viewProjectUrl() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.project_url)));
        startActivity(intent);
    }

    private void startRefreshService() {
        Intent intent = new Intent(this, RefreshService.class);
        intent.setAction(RefreshService.ACTION_REFRESH_MAIN_PAGE);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginActivity.REQUEST_CODE:
                if (hasLogin()) {
                    startRefreshService();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void modifyPassword() {
        mModifyView = new ModifyPasswordView(this);
        dialog = new AlertDialog.Builder(this).setTitle("修改密码").setPositiveButton("确认", null).setNegativeButton("取消", null).create();
        dialog.setCancelable(false);
        dialog.setView(mModifyView);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModifyView.hideErrorText();
                if (mModifyView.isCorrect()) {
                    if (!mModifyView.getOldPsw().equals(GDUTApplication.stuPsw)) {
                        mModifyView.showErrorText("原密码错误");
                    } else {
                        ProgressDialogInflater.showProgressDialog(SettingActivity.this, "正在修改...");
                        new Thread(new ModifyPasswordRunnable(new ModifyCallback(), mModifyView.getNewPsw())).start();
                    }
                }
            }
        });
    }

    private class ModifyCallback implements BaseRunnable.TaskCallback {

        @Override
        public void onResult(final boolean isConnected, final Bundle data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogInflater.dismiss();
                    if (isConnected) {
                        GDUTApplication.stuPsw = mModifyView.getNewPsw();
                        dialog.dismiss();
                        SnackbarUtil.show(SettingActivity.this, findViewById(android.R.id.content), "修改成功");
                    } else {
                        String reason = data.getString("reason");
                        mModifyView.showErrorText(reason);
                    }
                }
            });
        }
    }
}
