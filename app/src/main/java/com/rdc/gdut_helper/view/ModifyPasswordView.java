package com.rdc.gdut_helper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rdc.gdut_helper.R;


public class ModifyPasswordView extends RelativeLayout {

    private View mRoot;
    private Context mContext;
    private TextInputLayout mInputLayoutOldPsw;
    private TextInputLayout mInputLayoutNewPsw;
    private TextInputLayout mInputLayoutRepeatPsw;
    private TextView mTvError;


    public ModifyPasswordView(Context context) {
        super(context);
        mRoot = View.inflate(context, R.layout.modify_psw_dialog, null);
        mContext = context;
        init();
    }

    public ModifyPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = View.inflate(context, R.layout.modify_psw_dialog, null);
        mContext = context;
        init();
    }

    public ModifyPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRoot = View.inflate(context, R.layout.modify_psw_dialog, null);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ModifyPasswordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mRoot = View.inflate(context, R.layout.modify_psw_dialog, null);
        mContext = context;
        init();
    }

    private void init() {

        mInputLayoutOldPsw = (TextInputLayout) mRoot.findViewById(R.id.inputlayout_old_psw);
        mInputLayoutNewPsw = (TextInputLayout) mRoot.findViewById(R.id.inputlayout_new_psw);
        mInputLayoutRepeatPsw = (TextInputLayout) mRoot.findViewById(R.id.inputlayout_repeat_psw);
        mTvError = (TextView) mRoot.findViewById(R.id.tv_error);


//        mInputLayoutOldPsw.getEditText().getBackground().setColorFilter(mValue.data, PorterDuff.Mode.SRC_ATOP);
//        mInputLayoutNewPsw.getEditText().getBackground().setColorFilter(mValue.data, PorterDuff.Mode.SRC_ATOP);
//        mInputLayoutRepeatPsw.getEditText().getBackground().setColorFilter(mValue.data, PorterDuff.Mode.SRC_ATOP);

        mInputLayoutNewPsw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mInputLayoutNewPsw.getEditText().getText().toString().length() < 6) {
                    mInputLayoutNewPsw.setError("密码不能少于6位");
                } else {
                    mInputLayoutNewPsw.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mInputLayoutRepeatPsw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isSame()) {
                    mInputLayoutRepeatPsw.setErrorEnabled(false);
                } else {
                    mInputLayoutRepeatPsw.setError("两次输入密码不一致");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addView(mRoot);
    }

    public boolean isCorrect() {
        return mInputLayoutNewPsw.getEditText().getText().toString().length() >= 6 && isSame();
    }

    public String getOldPsw() {
        return mInputLayoutOldPsw.getEditText().getText().toString().trim();
    }

    public String getNewPsw() {
        return mInputLayoutNewPsw.getEditText().getText().toString().trim();
    }

    private boolean isSame() {
        return mInputLayoutRepeatPsw.getEditText().getText().toString().trim().equals(getNewPsw());
    }

    public void showErrorText(CharSequence text) {
        mTvError.setVisibility(View.VISIBLE);
        mTvError.setText(text);
    }

    public void hideErrorText() {
        mTvError.setVisibility(View.GONE);
    }
}
