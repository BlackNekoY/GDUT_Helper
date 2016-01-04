package com.rdc.gdut_helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdc.gdut_helper.R;
import com.rdc.gdut_helper.model.Course;

import java.util.List;


public class CourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Course> mCourseList;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CourseListAdapter(Context context, List<Course> courseList) {
        this.mContext = context;
        this.mCourseList = courseList;
    }

    private double calculatePoint() {
        double points = 0, credits = 0;

        for (Course course : mCourseList) {
            double point = course.calculatePoint();//单科学分绩点
            double credit = Double.parseDouble(course.point);

            points += point;
            credits += credit;
        }
        return points / credits;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CourseViewHolder) {
            Course course = mCourseList.get(position - 1);
            ((CourseViewHolder) holder).tvCourseName.setText(course.name);
            ((CourseViewHolder) holder).tvCourseCategory.setText(course.category);
            ((CourseViewHolder) holder).tvCourseScore.setText(course.score);
            ((CourseViewHolder) holder).tvCoursePoint.setText(course.point);
        } else if (holder instanceof HeaderViewHolder) {
            if (mCourseList.size() > 0) {
                ((HeaderViewHolder) holder).mTvPoint.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) holder).mTvPoint.setText("平均绩点：" + String.format("%.2f", calculatePoint()));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_course, parent, false);
            return new CourseViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.header_course, parent, false);
            return new HeaderViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mCourseList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

   /* private float calculatePoint(List<Course> courses) {
        float result = 0f;
        for(Course course : courses) {
        }
    }*/


    public List<Course> getCourseList() {
        return mCourseList;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView tvCourseName;
        TextView tvCourseCategory;
        TextView tvCourseScore;
        TextView tvCoursePoint;

        public CourseViewHolder(View itemView) {
            super(itemView);
            tvCourseName = (TextView) itemView.findViewById(R.id.tv_course_name);
            tvCourseCategory = (TextView) itemView.findViewById(R.id.tv_course_categroy);
            tvCourseScore = (TextView) itemView.findViewById(R.id.tv_course_score);
            tvCoursePoint = (TextView) itemView.findViewById(R.id.tv_course_point);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView mTvPoint;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mTvPoint = (TextView) itemView.findViewById(R.id.tv_course_point);
        }
    }
}
