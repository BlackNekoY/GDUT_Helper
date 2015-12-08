package com.rdc.gdut_helper.constant;

import com.rdc.gdut_helper.app.GDUTApplication;

public class ConnectConfig {
    public final static String HOST = "http://jwgl.gdut.edu.cn";

    public static String cookie;



    public static class WelcomePage {

        public final static String PATH_WELCOME = "/default2.aspx";
        public static String viewState;
    }

    public static class CheckCode {
        public final static String PATH_CHECK_CODE = "/CheckCode.aspx";
    }


    public static class Login {
        public final static String PATH_LOGIN = "/default2.aspx";

        public final static String PARAM_USER_NUM = "txtUserName";
        public final static String PARAM_USER_PSW = "TextBox2";
        public final static String PARAM_CHECK_CODE = "txtSecretCode";
        public final static String PARAM_VIEW_STATE = "__VIEWSTATE";
        public final static String PARAM_RADIO_BUTTON = "RadioButtonList1";
        public final static String PARAM_BUTTON = "Button1";
        public final static String PARAM_LANGUAGE = "lbLanguage";
        public final static String PARAM_HID_PDRS = "hidPdrs";
        public final static String PARAM_HID_SC = "hidsc";

    }

    public static class MainPage {
        public final static String PATH_MAIN_PAGE = "/xs_main.aspx?xh=" + GDUTApplication.stuNum;
    }

    public static class MainScore {
        public final static String PATH_MAIN_SCORE = "/xscj.aspx?xh=" + GDUTApplication.stuNum + "&xm=" + GDUTApplication.stuNameEncode + "&gnmkdm=N121605";

        public static String viewState;
    }

    public static class QueryScore {
        public final static String PARAM_VIEW_STATE = "__VIEWSTATE";
        public final static String PARAM_TXT_QSCJ = "txtQSCJ";
        public final static String PARAM_TXT_ZZCJ = "txtZZCJ";

        public final static String PARAM_BUTTON_1 = "Button1";
        public final static String PARAM_BUTTON_2 = "Button2";
        public final static String PARAM_BUTTON_5 = "Button5";
        public final static String PARAM_DDLXN = "ddlXN";
        public final static String PARAM_DDLXQ = "ddlXQ";
    }

    public static class PersonalInfo {
        public final static String PATH_INFO = "/xsgrxx.aspx?xh=" + GDUTApplication.stuNum + "&xm=" + GDUTApplication.stuNameEncode + "&gnmkdm=N121501";
        public final static String PATH_IMAGE = "/readimagexs.aspx?xh=" + GDUTApplication.stuNum;
    }

    public static class StudentTest {
        public final static String PATH_STUDENT_TEST = "/xskscx.aspx?xh=" + GDUTApplication.stuNum + "&xm=" + GDUTApplication.stuNameEncode + "&gnmkdm=N121604";

        public final static String PARAM_EVENTTARGET = "__EVENTTARGET";
        public final static String PARAM_EVENTARGUMENT = "__EVENTARGUMENT";
        public final static String PARAM_VIEW_STATE = "__VIEWSTATE";
        public final static String PARAM_XND = "xnd";
        public final static String PARAM_XQD = "xqd";

        public static String viewState;

    }

    public static class LevelTest {
        public final static String PATH_LEVEL_TEST = "/xsdjkscx.aspx?xh=" + GDUTApplication.stuNum + "&xm=" + GDUTApplication.stuNameEncode + "&gnmkdm=N121606";
    }

    public static class ModifyPassword {
        public final static String PATH_MODIFY_PASSWORD = "/mmxg.aspx?xh=" + GDUTApplication.stuNum + "&gnmkdm=N121502";

        public final static String PARAM_VIEW_STATE = "__VIEWSTATE";
        public final static String PARAM_TEXTBOX2 = "TextBox2";
        public final static String PARAM_TEXTBOX3 = "TextBox3";
        public final static String PARAM_TEXTBOX4 = "TextBox4";
        public final static String PARAM_BUTTON1 = "Button1";

        public static String viewState;

    }
}
