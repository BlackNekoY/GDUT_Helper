package com.rdc.gdut_helper.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.model.PersonalInfo;
import com.rdc.gdut_helper.net.VolleyManager;
import com.rdc.gdut_helper.ui.base.ToolbarActivity;
import com.rdc.gdut_helper.utils.HTMLUtil;
import com.rdc.gdut_helper.utils.ProgressDialogInflater;
import com.rdc.gdut_helper.utils.SnackbarUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by blackwhite on 15-12-27.
 */
public class PersonalInfoActivity extends ToolbarActivity implements View.OnClickListener {

    private boolean isEdit = false;
    private ImageView mIvPhoto;
    private TextView mTvName;
    private TextView mTvSex;
    private TextView mTvStuNum;
    private TextView mTvBirthTime;
    private TextView mTvAdmissionTime;
    private TextView mTvFaculty;
    private TextView mTvProfession;
    private TextView mTvClass;
    private TextView mTvEducation;
    private TextView mTvTicket;
    private TextView mTvIdCardNum;
    private TextView mTvCandidateNum;
    private EditText mEtPhoneNum;
    private EditText mEtEmail;
    private EditText mEtDormNum;
    private EditText mEtMiddleSchool;
    private EditText mEtFatherName;
    private EditText mEtFatherOrganization;
    private EditText mEtFatherPhoneNum;
    private EditText mEtMotherName;
    private EditText mEtMotherOrganization;
    private EditText mEtMotherPhoneNum;

    private PersonalInfo mPersonalInfo = new PersonalInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);


        setTitle("个人资料");

        findViewIds();
        $(R.id.fab_edit).setOnClickListener(this);

        loadPersonalInfo();
    }

    private void loadPersonalInfo() {
        ProgressDialogInflater.showProgressDialog(this, "正在加载个人信息");
        StringRequest request = new StringRequest(ConnectConfig.HOST + ConnectConfig.PersonalInfo.PATH_INFO
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPersonalInfo = HTMLUtil.getPersonalInfo(response);
                loadPhoto(mPersonalInfo.mPhotoUrl);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialogInflater.dismiss();
                SnackbarUtil.show(PersonalInfoActivity.this, getWindow().getDecorView().findViewById(android.R.id.content), "无法链接到" + ConnectConfig.HOST);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cookie", ConnectConfig.cookie);
                headers.put("Referer", ConnectConfig.HOST + ConnectConfig.MainPage.PATH_MAIN_PAGE);
                return headers;
            }
        };

        VolleyManager.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }

    private void loadPhoto(String mPhotoUrl) {
        ImageRequest imageRequest = new ImageRequest(mPhotoUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mIvPhoto.setImageBitmap(response);
                        setInfo();
                        ProgressDialogInflater.dismiss();
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressDialogInflater.dismiss();
                SnackbarUtil.show(PersonalInfoActivity.this, getWindow().getDecorView().findViewById(android.R.id.content), "加载图片失败");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cookie", ConnectConfig.cookie);
                headers.put("Referer", ConnectConfig.HOST + ConnectConfig.PersonalInfo.PATH_INFO);
                return headers;
            }
        };
        VolleyManager.getInstance(getApplicationContext()).getRequestQueue().add(imageRequest);
    }

    private void findViewIds() {
        mIvPhoto = $(R.id.iv_photo);
        mTvName = $(R.id.tv_name);
        mTvSex = $(R.id.tv_sex);
        mTvStuNum = $(R.id.tv_stu_num);
        mTvBirthTime = $(R.id.tv_birth_time);
        mTvAdmissionTime = $(R.id.tv_admission_time);
        mTvFaculty = $(R.id.tv_faculty);
        mTvProfession = $(R.id.tv_profession);
        mTvClass = $(R.id.tv_class);
        mTvEducation = $(R.id.tv_education);
        mTvTicket = $(R.id.tv_ticket);
        mTvIdCardNum = $(R.id.tv_id_card_num);
        mTvCandidateNum = $(R.id.tv_candidate_num);
        mEtPhoneNum = $(R.id.et_phone_num);
        mEtEmail = $(R.id.et_email);
        mEtDormNum = $(R.id.et_dorm_num);
        mEtMiddleSchool = $(R.id.et_middle_school);
        mEtFatherName = $(R.id.et_father_name);
        mEtFatherOrganization = $(R.id.et_father_organization);
        mEtFatherPhoneNum = $(R.id.et_father_phone_num);
        mEtMotherName = $(R.id.et_mother_name);
        mEtMotherOrganization = $(R.id.et_mother_organization);
        mEtMotherPhoneNum = $(R.id.et_mother_phone_num);

    }

    private void setInfo() {
        mTvName.setText(mPersonalInfo.mName);
        mTvSex.setText(mPersonalInfo.mSex);
        mTvStuNum.setText(mPersonalInfo.mStuNum);
        mTvBirthTime.setText(mPersonalInfo.mBirthTime + " 出生");
        mTvAdmissionTime.setText(mPersonalInfo.mAddmissionTime + " 入学");
        mTvFaculty.setText("学院：" + mPersonalInfo.mFaculty);
        mTvProfession.setText("专业：" + mPersonalInfo.mProfession);
        mTvClass.setText("行政班：" + mPersonalInfo.mClass);
        mTvEducation.setText("学历：" + mPersonalInfo.mEducation);
        mTvTicket.setText("准考证号：" + mPersonalInfo.mTicket);
        mTvIdCardNum.setText("身份证号：" + mPersonalInfo.mIdCardNum);
        mTvCandidateNum.setText("考生号：" + mPersonalInfo.mCandidate);
        mEtPhoneNum.setText(mPersonalInfo.mPhoneNum);
        mEtEmail.setText(mPersonalInfo.mEmail);
        mEtDormNum.setText(mPersonalInfo.mDormNum);
        mEtMiddleSchool.setText(mPersonalInfo.mMiddleSchool);
        mEtFatherName.setText(mPersonalInfo.mFatherName);
        mEtFatherOrganization.setText(mPersonalInfo.mFatherOrganization);
        mEtFatherPhoneNum.setText(mPersonalInfo.mFatherPhoneNum);
        mEtMotherName.setText(mPersonalInfo.mMotherName);
        mEtMotherOrganization.setText(mPersonalInfo.mMotherOrganization);
        mEtMotherPhoneNum.setText(mPersonalInfo.mMotherPhoneNum);
    }

    private void setEditTextEnabled(boolean isEdit) {
        mEtPhoneNum.setEnabled(isEdit);
        mEtEmail.setEnabled(isEdit);
        mEtDormNum.setEnabled(isEdit);
        mEtMiddleSchool.setEnabled(isEdit);
        mEtFatherName.setEnabled(isEdit);
        mEtFatherOrganization.setEnabled(isEdit);
        mEtFatherPhoneNum.setEnabled(isEdit);
        mEtMotherName.setEnabled(isEdit);
        mEtMotherOrganization.setEnabled(isEdit);
        mEtMotherPhoneNum.setEnabled(isEdit);
    }

    private void setFABVisbility(boolean isEdit) {
        if (isEdit) {
            ((FloatingActionButton) $(R.id.fab_edit)).hide();
        } else {
            ((FloatingActionButton) $(R.id.fab_edit)).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_edit:
                isEdit = true;
                setFABVisbility(isEdit);
                setEditTextEnabled(isEdit);
                invalidateOptionsMenu();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isEdit) {
            isEdit = false;
            setFABVisbility(isEdit);
            setEditTextEnabled(isEdit);
            invalidateOptionsMenu();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.findItem(R.id.done).setVisible(isEdit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                submit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        SnackbarUtil.show(this,findViewById(android.R.id.content),"此功能尚未实现", Snackbar.LENGTH_SHORT);
    }
}
