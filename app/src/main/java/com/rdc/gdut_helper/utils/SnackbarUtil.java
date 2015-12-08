package com.rdc.gdut_helper.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.rdc.gdut_helper.R;


public class SnackbarUtil {

    private SnackbarUtil(){}

    private static Snackbar snackbar;

    private static final int DEFAULT_DURATION = Snackbar.LENGTH_LONG;

    public static void show(Context context,View root,String text) {
        show(context,root,text,DEFAULT_DURATION);
    }

    public static void show(Context context,View root,String text,int duration) {
       show(context,root,text,duration,null,null);
    }

    public static void show(Context context,View root,String text,int duration,String actionText,View.OnClickListener listener) {
        snackbar = Snackbar.make(root, text, duration);
        if(!TextUtils.isEmpty(actionText) && listener != null) {
            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
            snackbar.setAction(actionText,listener);
            snackbar.setActionTextColor(value.data);
        }
        snackbar.show();
    }

    public static void dismiss() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

}
