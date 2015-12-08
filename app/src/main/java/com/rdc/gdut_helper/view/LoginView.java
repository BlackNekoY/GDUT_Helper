package com.rdc.gdut_helper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.app.GDUTApplication;


public class LoginView extends RelativeLayout implements View.OnClickListener{

    private View mRoot;
    private Context mContext;
    private ImageView mIvCheckCode;
    private TextInputLayout mInputLayoutStuNum;
    private TextInputLayout mInputLayoutStuPsw;
    private TextInputLayout mInputLayoutCheckCode;
    private TextView mTvChangeCheckcode;
    private TextView mTvError;

    private LoginViewClickListener l;

    public LoginView(Context context) {
        super(context);
        mRoot = View.inflate(context, R.layout.login_dialog, null);
        mContext = context;
        init();
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = View.inflate(context, R.layout.login_dialog, null);
        mContext = context;
        init();
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRoot = View.inflate(context, R.layout.login_dialog, null);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoginView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mRoot = View.inflate(context, R.layout.login_dialog, null);
        mContext = context;
        init();
    }

    private void init() {

        mIvCheckCode = (ImageView) mRoot.findViewById(R.id.iv_checkcode);
        mInputLayoutStuNum = (TextInputLayout) mRoot.findViewById(R.id.inputlayout_stu_num);
        mInputLayoutStuPsw = (TextInputLayout) mRoot.findViewById(R.id.inputlayout_stu_psw);
        mInputLayoutCheckCode = (TextInputLayout) mRoot.findViewById(R.id.inputlayout_check_code);
        mTvChangeCheckcode = (TextView) mRoot.findViewById(R.id.tv_change_checkcode);
        mTvError = (TextView) mRoot.findViewById(R.id.tv_error);


        if(GDUTApplication.isRemember) {
            ((AppCompatEditText) mInputLayoutStuNum.getEditText()).setText(GDUTApplication.stuNum);
            ((AppCompatEditText) mInputLayoutStuPsw.getEditText()).setText(GDUTApplication.stuPsw);
        }

        mTvChangeCheckcode.setOnClickListener(this);

        addView(mRoot);
    }

    public boolean isMsgComplete() {
        return !TextUtils.isEmpty(mInputLayoutStuNum.getEditText().getText().toString().trim()) &&
                !TextUtils.isEmpty(mInputLayoutStuPsw.getEditText().getText().toString().trim()) &&
                !TextUtils.isEmpty(mInputLayoutCheckCode.getEditText().getText().toString().trim());

    }

    public String getStuNumber(){
        return mInputLayoutStuNum.getEditText().getText().toString().trim();
    }
    public String getStuPassword() {
        return mInputLayoutStuPsw.getEditText().getText().toString().trim();
    }
    public String getCheckCode() {
        return mInputLayoutCheckCode.getEditText().getText().toString().trim();
    }
    public void setListener(LoginViewClickListener l) {
        this.l = l;
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
                if(l != null) {
                    l.getCheckCode();
                }
                break;
        }
    }

    /**
     * 重新获取验证码
     */
    public interface LoginViewClickListener {
        public void getCheckCode();
    }
}
