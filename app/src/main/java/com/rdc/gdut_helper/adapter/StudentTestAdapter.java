package com.rdc.gdut_helper.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.model.StudentTest;

import java.util.List;


public class StudentTestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StudentTest> mList;
    private Context mContext;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String year;
    private String term;

    public StudentTestAdapter(Context mContext, List<StudentTest> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public List<StudentTest> getList() {
        return mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.header_student_test, parent, false);
            return new StudentTestHeaderHolder(view);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_student_test, parent, false);
            return new StudentTestViewHolder(view);
        }
        throw new IllegalStateException("ViewHolder Error");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentTestViewHolder) {
            StudentTest studentTest = mList.get(position - 1);
            ((StudentTestViewHolder) holder).tvName.setText(studentTest.name);
            ((StudentTestViewHolder) holder).tvDate.setText(studentTest.date.equals(StudentTest.NULL_VALUE) ? "时间" : "时间:" + studentTest.date);
            ((StudentTestViewHolder) holder).tvAddr.setText(studentTest.addr.equals(StudentTest.NULL_VALUE) ? "地点" : "地点:" + studentTest.addr);
            ((StudentTestViewHolder) holder).tvSeatNumber.setText(studentTest.seatNumber.equals(StudentTest.NULL_VALUE) ? "座位" : "座位:" +studentTest.seatNumber);
            ((StudentTestViewHolder) holder).tvSchoolZone.setText(studentTest.schoolZone.equals(StudentTest.NULL_VALUE) ? "校区" : studentTest.schoolZone);
        } else if (holder instanceof StudentTestHeaderHolder) {
            if (mList.size() > 0) {
                ((StudentTestHeaderHolder) holder).cardView.setVisibility(View.VISIBLE);
                ((StudentTestHeaderHolder) holder).tvHeaderYear.setText(year);
                ((StudentTestHeaderHolder) holder).tvHeaderTerm.setText("第" + term + "学期");
            }else {
                ((StudentTestHeaderHolder) holder).cardView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setHeaderYear(String year) {
        this.year = year;
    }
    public void setHeaderTerm(String term) {
        this.term = term;
    }

    class StudentTestViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDate;
        TextView tvAddr;
        TextView tvSeatNumber;
        TextView tvSchoolZone;

        public StudentTestViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvAddr = (TextView) itemView.findViewById(R.id.tv_addr);
            tvSeatNumber = (TextView) itemView.findViewById(R.id.tv_seat_number);
            tvSchoolZone = (TextView) itemView.findViewById(R.id.tv_school_zone);
        }
    }


    class StudentTestHeaderHolder extends RecyclerView.ViewHolder {
        TextView tvHeaderYear;
        TextView tvHeaderTerm;
        CardView cardView;

        public StudentTestHeaderHolder(View itemView) {
            super(itemView);
            tvHeaderYear = (TextView) itemView.findViewById(R.id.tv_header_year);
            tvHeaderTerm = (TextView) itemView.findViewById(R.id.tv_header_term);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
