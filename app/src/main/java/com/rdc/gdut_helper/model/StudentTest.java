package com.rdc.gdut_helper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class StudentTest implements Parcelable {

    public String id;
    public String name;
    public String date;
    public String addr;
    public String form;
    public String seatNumber;
    public String schoolZone;

    public static final String NULL_VALUE = "-1";

    public StudentTest() {

    }

    public static boolean isCorrectTest(StudentTest studentTest) {
        return !(studentTest == null || TextUtils.isEmpty(studentTest.id) || TextUtils.isEmpty(studentTest.name));
    }

    protected StudentTest(Parcel in) {
        id = in.readString();
        name = in.readString();
        date = in.readString();
        addr = in.readString();
        form = in.readString();
        seatNumber = in.readString();
        schoolZone = in.readString();
    }

    @Override
    public String toString() {
        return "StudentTest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", addr='" + addr + '\'' +
                ", form='" + form + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", schoolZone='" + schoolZone + '\'' +
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
        dest.writeString(date);
        dest.writeString(addr);
        dest.writeString(form);
        dest.writeString(seatNumber);
        dest.writeString(schoolZone);
    }

    public static final Creator<StudentTest> CREATOR = new Creator<StudentTest>() {
        @Override
        public StudentTest createFromParcel(Parcel in) {
            return new StudentTest(in);
        }

        @Override
        public StudentTest[] newArray(int size) {
            return new StudentTest[size];
        }
    };


}
