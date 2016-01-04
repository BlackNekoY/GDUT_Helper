package com.rdc.gdut_helper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by blackwhite on 15-12-30.
 */
public class PersonalInfo implements Parcelable{
    //照片
    public String mPhotoUrl;
    //姓名
    public String mName;
    //性别
    public String mSex;
    //学号
    public String mStuNum;
    //出生日期
    public String mBirthTime;
    //入学日期
    public String mAddmissionTime;
    //学院
    public String mFaculty;
    //专业名称
    public String mProfession;
    //行政班
    public String mClass;
    //学历
    public String mEducation;
    //准考证号
    public String mTicket;
    //身份证号
    public String mIdCardNum;
    //考生号
    public String mCandidate;
    //手机号码
    public String mPhoneNum;
    //邮箱
    public String mEmail;
    //宿舍号
    public String mDormNum;
    //毕业中学
    public String mMiddleSchool;
    //父亲名字
    public String mFatherName;
    //父亲单位
    public String mFatherOrganization;
    //父亲手机
    public String mFatherPhoneNum;
    //母亲名字
    public String mMotherName;
    //母亲单位
    public String mMotherOrganization;
    //母亲手机
    public String mMotherPhoneNum;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPhotoUrl);
        dest.writeString(this.mName);
        dest.writeString(this.mSex);
        dest.writeString(this.mStuNum);
        dest.writeString(this.mBirthTime);
        dest.writeString(this.mAddmissionTime);
        dest.writeString(this.mFaculty);
        dest.writeString(this.mProfession);
        dest.writeString(this.mClass);
        dest.writeString(this.mEducation);
        dest.writeString(this.mTicket);
        dest.writeString(this.mIdCardNum);
        dest.writeString(this.mCandidate);
        dest.writeString(this.mPhoneNum);
        dest.writeString(this.mEmail);
        dest.writeString(this.mDormNum);
        dest.writeString(this.mMiddleSchool);
        dest.writeString(this.mFatherName);
        dest.writeString(this.mFatherOrganization);
        dest.writeString(this.mFatherPhoneNum);
        dest.writeString(this.mMotherName);
        dest.writeString(this.mMotherOrganization);
        dest.writeString(this.mMotherPhoneNum);
    }

    public PersonalInfo() {
    }

    private PersonalInfo(Parcel in) {
        this.mPhotoUrl = in.readString();
        this.mName = in.readString();
        this.mSex = in.readString();
        this.mStuNum = in.readString();
        this.mBirthTime = in.readString();
        this.mAddmissionTime = in.readString();
        this.mFaculty = in.readString();
        this.mProfession = in.readString();
        this.mClass = in.readString();
        this.mEducation = in.readString();
        this.mTicket = in.readString();
        this.mIdCardNum = in.readString();
        this.mCandidate = in.readString();
        this.mPhoneNum = in.readString();
        this.mEmail = in.readString();
        this.mDormNum = in.readString();
        this.mMiddleSchool = in.readString();
        this.mFatherName = in.readString();
        this.mFatherOrganization = in.readString();
        this.mFatherPhoneNum = in.readString();
        this.mMotherName = in.readString();
        this.mMotherOrganization = in.readString();
        this.mMotherPhoneNum = in.readString();
    }

    public static final Creator<PersonalInfo> CREATOR = new Creator<PersonalInfo>() {
        public PersonalInfo createFromParcel(Parcel source) {
            return new PersonalInfo(source);
        }

        public PersonalInfo[] newArray(int size) {
            return new PersonalInfo[size];
        }
    };

    @Override
    public String toString() {
        return "PersonalInfo{" +
                "mPhotoUrl='" + mPhotoUrl + '\'' +
                ", mName='" + mName + '\'' +
                ", mSex='" + mSex + '\'' +
                ", mStuNum='" + mStuNum + '\'' +
                ", mBirthTime='" + mBirthTime + '\'' +
                ", mAddmissionTime='" + mAddmissionTime + '\'' +
                ", mFaculty='" + mFaculty + '\'' +
                ", mProfession='" + mProfession + '\'' +
                ", mClass='" + mClass + '\'' +
                ", mEducation='" + mEducation + '\'' +
                ", mTicket='" + mTicket + '\'' +
                ", mIdCardNum='" + mIdCardNum + '\'' +
                ", mCandidate='" + mCandidate + '\'' +
                ", mPhoneNum='" + mPhoneNum + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mDormNum='" + mDormNum + '\'' +
                ", mMiddleSchool='" + mMiddleSchool + '\'' +
                ", mFatherName='" + mFatherName + '\'' +
                ", mFatherOrganization='" + mFatherOrganization + '\'' +
                ", mFatherPhoneNum='" + mFatherPhoneNum + '\'' +
                ", mMotherName='" + mMotherName + '\'' +
                ", mMotherOrganization='" + mMotherOrganization + '\'' +
                ", mMotherPhoneNum='" + mMotherPhoneNum + '\'' +
                '}';
    }
}
