package com.rdc.gdut_helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.model.LevelTest;

import java.util.List;


public class LevelTestAdapter extends RecyclerView.Adapter<LevelTestAdapter.LevelTestViewHolder>{

    private List<LevelTest> mList;
    private Context mContext;

    public LevelTestAdapter(Context mContext, List<LevelTest> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public List<LevelTest> getList() {
        return mList;
    }

    @Override
    public LevelTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_level_test, parent, false);
        return new LevelTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LevelTestViewHolder holder, int position) {
        LevelTest levelTest = mList.get(position);
        holder.tvYear.setText(levelTest.year);
        holder.tvTerm.setText(levelTest.term);
        holder.tvName.setText(levelTest.name);
        holder.tvScore.setText(levelTest.score);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class LevelTestViewHolder extends RecyclerView.ViewHolder {

        TextView tvYear;
        TextView tvTerm;
        TextView tvName;
        TextView tvScore;

        public LevelTestViewHolder(View itemView) {
            super(itemView);
            tvYear = (TextView) itemView.findViewById(R.id.tv_year);
            tvTerm = (TextView) itemView.findViewById(R.id.tv_term);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
        }
    }
}
