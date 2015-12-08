package com.rdc.gdut_helper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.constant.ConnectConfig;


public class SearchCourseScoreView extends RelativeLayout {

    private View mRoot;
    private Context mContext;
    private AppCompatSpinner mSpnYear;
    private AppCompatSpinner mSpnTerm;

    private String eventTarget = null;

    public SearchCourseScoreView(Context context) {
        super(context);
        mRoot = View.inflate(context, R.layout.search_course_score_dialog, null);
        mContext = context;
        init();
    }

    public SearchCourseScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = View.inflate(context, R.layout.search_course_score_dialog, null);
        mContext = context;
        init();
    }

    public SearchCourseScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRoot = View.inflate(context, R.layout.search_course_score_dialog, null);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchCourseScoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mRoot = View.inflate(context, R.layout.search_course_score_dialog, null);
        mContext = context;
        init();
    }

    private void init() {
        mSpnYear = (AppCompatSpinner) mRoot.findViewById(R.id.spn_year);
        mSpnTerm = (AppCompatSpinner) mRoot.findViewById(R.id.spn_term);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.spinner_item_light, android.R.id.text1);
        adapter1.addAll(mContext.getResources().getStringArray(R.array.year_array));

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.spinner_item_light, android.R.id.text1);
        adapter2.addAll(mContext.getResources().getStringArray(R.array.term_array));

        adapter1.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapter2.setDropDownViewResource(R.layout.spinner_item_dropdown);

        mSpnYear.setAdapter(adapter1);
        mSpnTerm.setAdapter(adapter2);

        mSpnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventTarget = ConnectConfig.StudentTest.PARAM_XND;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpnTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventTarget = ConnectConfig.StudentTest.PARAM_XQD;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addView(mRoot);
    }

    public String getYear() {
        return mSpnYear.getSelectedItem().toString();
    }

    public String getTerm() {
        return mSpnTerm.getSelectedItem().toString();
    }

    public String getEventTarget() {
        return eventTarget;
    }

    public boolean isNothingChoosed() {
        return (getYear().equals("学年") && getTerm().equals("学期"));
    }

}

