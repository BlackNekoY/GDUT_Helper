package com.rdc.gdut_helper.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ProgressDialogInflater {

    private static final int DISMISS = 1;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    };
    public static ProgressDialog dialog;

    public static void showProgressDialog(Context context, CharSequence message) {
        dialog = new ProgressDialog(context);
        if (message != null) {
            dialog.setMessage(message);
        }
        dialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void setMessage(CharSequence message) {
        if(dialog != null) {
            dialog.setMessage(message);
        }
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

