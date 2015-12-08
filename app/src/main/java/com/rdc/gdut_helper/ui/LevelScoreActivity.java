package com.rdc.gdut_helper.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.adapter.LevelTestAdapter;
import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.entity.DividerGridItemDecoration;
import com.rdc.gdut_helper.model.LevelTest;
import com.rdc.gdut_helper.net.BaseRunnable;
import com.rdc.gdut_helper.net.LevelScoreRunnable;
import com.rdc.gdut_helper.ui.base.ToolbarActivity;
import com.rdc.gdut_helper.utils.ProgressDialogInflater;
import com.rdc.gdut_helper.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by blackwhite on 15-12-8.
 */
public class LevelScoreActivity extends ToolbarActivity implements View.OnClickListener {

    private ExecutorService mThreadPool;
    private List<LevelTest> mLevelScoreList;
    private RecyclerView mRecyclerView;
    private LevelTestAdapter mAdapter;
    private FloatingActionButton mFabSearch;
    private View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_score);
        setTitle(R.string.level_score);
        parent = getWindow().getDecorView().findViewById(android.R.id.content);

        mThreadPool = Executors.newFixedThreadPool(5);

        findViewsId();
        initRecycleview();
        setListener();

        loadData();
    }

    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadData();
        }
    };

    private void loadData() {
        ProgressDialogInflater.showProgressDialog(this, "加载中");
        mThreadPool.execute(new LevelScoreRunnable(new LevelTestCallback()));
    }

    private void setListener() {
        mFabSearch.setOnClickListener(this);
    }

    private void findViewsId() {
        mRecyclerView = $(R.id.recycler_view);
        mFabSearch = $(R.id.fab_search);
    }

    private void initRecycleview() {
        mLevelScoreList = new ArrayList<>();
        mAdapter = new LevelTestAdapter(this, mLevelScoreList);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_search:
                loadData();
                break;
        }
    }

    private class LevelTestCallback implements BaseRunnable.TaskCallback {

        @Override
        public void onResult(final boolean isConnected, final Bundle data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogInflater.dismiss();
                    if (isConnected) {
                        List<LevelTest> list = data.getParcelableArrayList("level_test_list");
                        mAdapter.getList().clear();
                        mAdapter.getList().addAll(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        SnackbarUtil.show(LevelScoreActivity.this, parent, "无法连接到" + ConnectConfig.HOST, Snackbar.LENGTH_LONG, "重试", l);
                    }
                }
            });

        }
    }
}
