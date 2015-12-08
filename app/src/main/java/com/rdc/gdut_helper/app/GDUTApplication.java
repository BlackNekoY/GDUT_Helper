package com.rdc.gdut_helper.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.rdc.gdut_helper.utils.L;


public class GDUTApplication extends Application {

    private static SharedPreferences sp;
    public static String stuNum;
    public static String stuPsw;
    public static String stuName;
    public static String stuNameEncode;
    public static boolean isRemember;
    public static boolean hasLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void initSharePreferences() {
        sp = getSharedPreferences("GDUT_Helper_Config", MODE_PRIVATE);
        stuNum = sp.getString("student_number", null);
        stuPsw = sp.getString("student_password", null);
        stuName = sp.getString("student_name", null);
        isRemember = sp.getBoolean("is_remember", false);
    }

    public static void saveSettings() {
        if(isRemember)  {
            L.e("true");
        }else {
            L.e("false");
        }
        SharedPreferences.Editor editor = sp.edit();
        if (isRemember) {
            editor.putString("student_number", stuNum);
            editor.putString("student_password", stuPsw);
            editor.putBoolean("is_remember", isRemember);
        } else {
            editor.clear();
        }
        editor.commit();
    }
}
