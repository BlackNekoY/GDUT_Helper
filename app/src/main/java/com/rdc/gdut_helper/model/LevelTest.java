package com.rdc.gdut_helper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class LevelTest implements Parcelable {

    public String year;
    public String term;
    public String name;
    public String admissionId;
    public String date;
    public String score;
    public String hearingScore;
    public String readingScore;
    public String writingScore;

    public static final String NULL_VALUE = "-1";

    public LevelTest() {

    }

    protected LevelTest(Parcel in) {
        year = in.readString();
        term = in.readString();
        name = in.readString();
        admissionId = in.readString();
        date = in.readString();
        score = in.readString();
        hearingScore = in.readString();
        readingScore = in.readString();
        writingScore = in.readString();
    }

    public static boolean isCorrectTest(LevelTest levelTest) {
        return !(levelTest == null || TextUtils.isEmpty(levelTest.year) || TextUtils.isEmpty(levelTest.term) || TextUtils.isEmpty(levelTest.name));
    }

    @Override
    public String toString() {
        return "LevelTest{" +
                "year='" + year + '\'' +
                ", term='" + term + '\'' +
                ", name='" + name + '\'' +
                ", admissionId='" + admissionId + '\'' +
                ", date='" + date + '\'' +
                ", score='" + score + '\'' +
                ", hearingScore='" + hearingScore + '\'' +
                ", readingScore='" + readingScore + '\'' +
                ", writingScore='" + writingScore + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(year);
        dest.writeString(term);
        dest.writeString(name);
        dest.writeString(admissionId);
        dest.writeString(date);
        dest.writeString(score);
        dest.writeString(hearingScore);
        dest.writeString(readingScore);
        dest.writeString(writingScore);
    }

    public static final Creator<LevelTest> CREATOR = new Creator<LevelTest>() {
        @Override
        public LevelTest createFromParcel(Parcel in) {
            return new LevelTest(in);
        }

        @Override
        public LevelTest[] newArray(int size) {
            return new LevelTest[size];
        }
    };

}
