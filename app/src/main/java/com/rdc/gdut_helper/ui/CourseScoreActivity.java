package com.rdc.gdut_helper.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.adapter.CourseListAdapter;
import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.entity.DividerItemDecoration;
import com.rdc.gdut_helper.model.Course;
import com.rdc.gdut_helper.net.BaseRunnable;
import com.rdc.gdut_helper.net.MainScoreRunnable;
import com.rdc.gdut_helper.net.QueryScoreRunnable;
import com.rdc.gdut_helper.ui.base.ToolbarActivity;
import com.rdc.gdut_helper.utils.ProgressDialogInflater;
import com.rdc.gdut_helper.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by blackwhite on 15-12-7.
 */
public class CourseScoreActivity extends ToolbarActivity implements View.OnClickListener {

    private ExecutorService mThreadPool;
    private AppCompatSpinner mSpnYear;
    private AppCompatSpinner mSpnTerm;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabSearch;
    private RadioGroup mRgQueryWay;
    private CourseListAdapter mAdapter;
    private List<Course> mCourseList = new ArrayList<>();
    private View mItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_score);
        setTitle(R.string.course_score);
        mThreadPool = Executors.newFixedThreadPool(5);
        mItemView = getWindow().getDecorView().findViewById(android.R.id.content);

        findViews();
        initSpinner();
        initRecyclerView();

        mFabSearch.setOnClickListener(this);
    }

    private void findViews() {
        mFabSearch = $(R.id.fab_search);
        mRgQueryWay = $(R.id.rg_query_way);

        mRgQueryWay.check(((RadioButton) $(R.id.rb1)).getId());
    }

    private void initRecyclerView() {
        mRecyclerView = $(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new CourseListAdapter(this, mCourseList);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initSpinner() {
        mSpnYear = $(R.id.spn_year);
        mSpnTerm = $(R.id.spn_term);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, android.R.id.text1);
        adapter1.addAll(this.getResources().getStringArray(R.array.year_array));

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, android.R.id.text1);
        adapter2.addAll(this.getResources().getStringArray(R.array.term_array));

        adapter1.setDropDownViewResource(R.layout.spinner_item_dropdown);
        adapter2.setDropDownViewResource(R.layout.spinner_item_dropdown);

        mSpnYear.setAdapter(adapter1);
        mSpnTerm.setAdapter(adapter2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_search:
                ProgressDialogInflater.showProgressDialog(this, null);
                if (ConnectConfig.MainScore.viewState == null) {
                    viewMainScorePage();
                } else {
                    queryScore();
                }
                break;
        }
    }

    private void queryScore() {
        int btnId = mRgQueryWay.getCheckedRadioButtonId();
        View rb = mRgQueryWay.findViewById(btnId);
        int index = mRgQueryWay.indexOfChild(rb);
        int type = (index == 0) ? QueryScoreRunnable.TYPE_TERM : (index == 1) ? QueryScoreRunnable.TYPE_YEAR : QueryScoreRunnable.TYPE_ALL;
        mThreadPool.execute(new QueryScoreRunnable(
                new QueryScoreCallback(), type, mSpnYear.getSelectedItem().toString(), mSpnTerm.getSelectedItem().toString()));
    }

    private void viewMainScorePage() {
        mThreadPool.execute(new MainScoreRunnable(new MainScoreCallback()));
    }

    private class MainScoreCallback implements BaseRunnable.TaskCallback {

        @Override
        public void onResult(final boolean isConnected, Bundle data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isConnected) {
                        queryScore();
                    } else {
                        ProgressDialogInflater.dismiss();
                        SnackbarUtil.show(CourseScoreActivity.this, mItemView, "无法连接到" + ConnectConfig.HOST, Snackbar.LENGTH_INDEFINITE, "重试", listener);
                    }
                }
            });
        }
    }

    private class QueryScoreCallback implements BaseRunnable.TaskCallback {

        @Override
        public void onResult(final boolean isConnected, final Bundle data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogInflater.dismiss();
                    if (isConnected) {
                        ArrayList<Course> list = data.getParcelableArrayList("courseList");
                        mAdapter.getCourseList().clear();
                        mAdapter.getCourseList().addAll(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        SnackbarUtil.show(CourseScoreActivity.this, mItemView, "无法连接到" + ConnectConfig.HOST);
                    }
                }
            });
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ProgressDialogInflater.showProgressDialog(CourseScoreActivity.this, "连接教务系统中");
            viewMainScorePage();
        }
    };
}
