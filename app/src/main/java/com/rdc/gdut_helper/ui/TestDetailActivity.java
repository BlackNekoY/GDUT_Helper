package com.rdc.gdut_helper.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.adapter.StudentTestAdapter;
import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.entity.DividerGridItemDecoration;
import com.rdc.gdut_helper.model.StudentTest;
import com.rdc.gdut_helper.net.BaseRunnable;
import com.rdc.gdut_helper.net.StudentTestGetRunnable;
import com.rdc.gdut_helper.net.StudentTestPostRunnable;
import com.rdc.gdut_helper.ui.base.ToolbarActivity;
import com.rdc.gdut_helper.utils.ProgressDialogInflater;
import com.rdc.gdut_helper.utils.SnackbarUtil;
import com.rdc.gdut_helper.view.SearchCourseScoreView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by blackwhite on 15-12-8.
 */
public class TestDetailActivity extends ToolbarActivity implements View.OnClickListener {

    private ExecutorService mThreadPool;
    private View mParent;
    private RecyclerView mRecyclerView;
    private StudentTestAdapter mAdapter;
    private SearchCourseScoreView mSearchStudentView;
    private FloatingActionButton mFabSearch;
    private AlertDialog mDialog;
    private List<StudentTest> mStudentTestList;

    private String year;
    private String term;

    public void loadData() {
        ProgressDialogInflater.showProgressDialog(this, "加载中");
        year = "2015-2016";
        term = "1";
        mThreadPool.execute(new StudentTestGetRunnable(new StudentTestCallback()));
    }

    public void loadData(String eventTarget, String xnd, String xqd) {
        ProgressDialogInflater.showProgressDialog(this, "加载中");
        year = xnd;
        term = xqd;
        mThreadPool.execute(new StudentTestPostRunnable(new StudentTestCallback(), eventTarget, xnd, xqd));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        setTitle(R.string.test_detail);
        mThreadPool = Executors.newFixedThreadPool(5);
        mParent = getWindow().getDecorView().findViewById(android.R.id.content);

        findViewsId();
        initRecyclerview();
        initSearchView();
        setListener();

        loadData();
    }

    private void initSearchView() {

        if (mSearchStudentView == null) {
            mSearchStudentView = new SearchCourseScoreView(this);
        }
    }

    private void setListener() {
        mFabSearch.setOnClickListener(this);
    }

    private void initRecyclerview() {
        mStudentTestList = new ArrayList<>();
        mAdapter = new StudentTestAdapter(this, mStudentTestList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && mFabSearch.isShown()) {
                    mFabSearch.hide();
                } else if (dy < 0 && !mFabSearch.isShown()) {
                    mFabSearch.show();
                }
            }
        });

    }

    private void findViewsId() {
        mFabSearch = $(R.id.fab_search);
        mRecyclerView = $(R.id.recycler_view);
    }

    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadData();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_search:
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mSearchStudentView.isNothingChoosed()) {
                                loadData();
                            } else {
                                loadData(mSearchStudentView.getEventTarget(), mSearchStudentView.getYear(), mSearchStudentView.getTerm());
                            }
                        }
                    }).setNegativeButton("取消", null).create();
                    mDialog.setView(mSearchStudentView);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                }
                mDialog.show();
                break;
        }
    }

    private class StudentTestCallback implements BaseRunnable.TaskCallback {

        @Override
        public void onResult(final boolean isConnected, final Bundle data) {
            ProgressDialogInflater.dismiss();
            if (isConnected) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<StudentTest> list = data.getParcelableArrayList("student_test_list");
                        mAdapter.setHeaderYear(year);
                        mAdapter.setHeaderTerm(term);
                        mAdapter.getList().clear();
                        mAdapter.getList().addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                SnackbarUtil.show(TestDetailActivity.this, mParent, "无法连接到" + ConnectConfig.HOST, Snackbar.LENGTH_LONG, "重试", l);
            }

        }
    }
}
