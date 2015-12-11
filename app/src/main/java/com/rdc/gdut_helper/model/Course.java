package com.rdc.gdut_helper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Course implements Parcelable {

    public String id;   //ID
    public String name; //名称
    public String category;//性质
    public String score;       //分数
    public String belong;       //课程归属
    public String ownershipScore;    //补考分数
    public String retakeScore;      //重修分数
    public boolean isOwnerShop;     //是否补考
    public boolean isRetake;           //是否重修
    public String point;           //学分

    public static final String NULL_VALUE = "-1";

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", score='" + score + '\'' +
                ", belong='" + belong + '\'' +
                ", isOwnerShop=" + isOwnerShop +
                ", ownershipScore='" + ownershipScore + '\'' +
                ", isRetake=" + isRetake +
                ", retakeScore='" + retakeScore + '\'' +
                ", point='" + point + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(score);
        dest.writeString(belong);
        dest.writeString(ownershipScore);
        dest.writeString(retakeScore);
        dest.writeByte((byte) (isOwnerShop ? 1 : 0));
        dest.writeByte((byte) (isRetake ? 1 : 0));
        dest.writeString(point);
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            Course course = new Course();
            course.id = source.readString();
            course.name = source.readString();
            course.category = source.readString();
            course.score = source.readString();
            course.belong = source.readString();
            course.ownershipScore = source.readString();
            course.retakeScore = source.readString();
            course.isOwnerShop = source.readByte() == 1 ? true : false;
            course.isRetake = source.readByte() == 1 ? true : false;
            course.point = source.readString();
            return course;
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public static boolean isCorrectCourse(Course course) {
        return !(course == null || TextUtils.isEmpty(course.id) || TextUtils.isEmpty(course.name) || TextUtils.isEmpty(course.category));
    }

}
