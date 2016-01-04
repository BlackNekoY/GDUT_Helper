package com.rdc.gdut_helper.utils;

import android.text.TextUtils;
import android.util.Log;

import com.rdc.gdut_helper.constant.ConnectConfig;
import com.rdc.gdut_helper.model.Course;
import com.rdc.gdut_helper.model.LevelTest;
import com.rdc.gdut_helper.model.PersonalInfo;
import com.rdc.gdut_helper.model.StudentTest;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtil {

    private static final String TAG = "HTMLUtil";
    private static final String SPACE = "&nbsp;";

    public static String getViewState(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String regex = "(?<=name=\"__VIEWSTATE\" value=\").*?(?=\")";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static String getStuName(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String regex = "(?<=<span id=\"xhxm\">).*?(?=同学)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }


    public static void logHeaders(HttpURLConnection conn) {
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entries = headerFields.entrySet();
        for (Iterator<Map.Entry<String, List<String>>> it = entries.iterator(); it.hasNext(); ) {
            Map.Entry<String, List<String>> entry = it.next();
            Log.e(entry.getKey(), entry.getValue().toString());
        }
    }


    private static Course getCourseFromLine(String str) {
        Course course = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        String regex = "(?<=<td>).*?(?=</td>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            if (course == null) {
                course = new Course();
            }
            String value = matcher.group();
            if (value.equals(SPACE)) {
                value = Course.NULL_VALUE;
            }
            if (course.id == null) {
                course.id = value;
            } else if (course.name == null) {
                course.name = value;
            } else if (course.category == null) {
                course.category = value;
            } else if (course.score == null) {
                course.score = value;
            } else if (course.belong == null) {
                course.belong = value;
            } else if (course.ownershipScore == null) {
                course.ownershipScore = value;
                course.isOwnerShop = !(value == Course.NULL_VALUE);
            } else if (course.retakeScore == null) {
                course.retakeScore = value;
                course.isRetake = !(value == Course.NULL_VALUE);
            } else if (course.point == null) {
                course.point = value;
            } else {
                break;
            }
        }
        return Course.isCorrectCourse(course) ? course : null;
    }

    /**
     * 获取课程考试成绩
     *
     * @param str
     * @return
     */
    public static ArrayList<Course> getCourseList(String str) {
        ArrayList<Course> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        String startKey = "<tr class=\"datelisthead\">\n";
        String endKey = "</table>";
        int startIndex = str.indexOf(startKey) + startKey.length();
        if (startIndex == -1) {
            return list;
        }
        str = str.substring(startIndex);
        int endIndex = str.indexOf(endKey);
        str = str.substring(0, endIndex);

        String[] strs = str.split("\n");
        for (int i = 1; i < strs.length; i++) {
            String line = strs[i];
            Course course = HTMLUtil.getCourseFromLine(line);
            if (course != null) {
                list.add(course);
            }
        }
        return list;
    }

    private static LevelTest getLevelTest(String str) {
        LevelTest levelTest = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String regex = "(?<=<td>).*?(?=</td>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            if (levelTest == null) {
                levelTest = new LevelTest();
            }
            String value = matcher.group();
            if (value.equals(SPACE)) {
                value = Course.NULL_VALUE;
            }
            if (levelTest.year == null) {
                levelTest.year = value;
            } else if (levelTest.term == null) {
                levelTest.term = value;
            } else if (levelTest.name == null) {
                levelTest.name = value;
            } else if (levelTest.admissionId == null) {
                levelTest.admissionId = value;
            } else if (levelTest.date == null) {
                levelTest.date = value;
            } else if (levelTest.score == null) {
                levelTest.score = value;
            } else if (levelTest.hearingScore == null) {
                levelTest.hearingScore = value;
            } else if (levelTest.readingScore == null) {
                levelTest.readingScore = value;
            } else if (levelTest.writingScore == null) {
                levelTest.writingScore = value;
            } else {
                break;
            }
        }
        return LevelTest.isCorrectTest(levelTest) ? levelTest : null;
    }

    public static ArrayList<LevelTest> getLevelTestList(String str) {
        ArrayList<LevelTest> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        String startKey = "<tr class=\"datelisthead\">\n";
        String endKey = "</table>";
        int startIndex = str.indexOf(startKey) + startKey.length();
        if (startIndex == -1) {
            return list;
        }
        str = str.substring(startIndex);
        int endIndex = str.indexOf(endKey);
        str = str.substring(0, endIndex);

        String[] strs = str.split("\n");
        for (int i = 1; i < strs.length; i++) {
            String line = strs[i];
            LevelTest levelTest = HTMLUtil.getLevelTest(line);
            if (levelTest != null) {
                list.add(levelTest);
            }
        }
        return list;
    }

    private static StudentTest getStudentTest(String str) {
        StudentTest studentTest = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String regex = "(?<=<td>).*?(?=</td>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        String name = null;
        while (matcher.find()) {
            if (studentTest == null) {
                studentTest = new StudentTest();
            }
            String value = matcher.group();
            if (value.equals(SPACE)) {
                value = StudentTest.NULL_VALUE;
            }
            if (studentTest.id == null) {
                studentTest.id = value;
            } else if (studentTest.name == null) {
                studentTest.name = value;
            } else if (name == null) {
                name = value;
            } else if (studentTest.date == null) {
                studentTest.date = value;
            } else if (studentTest.addr == null) {
                studentTest.addr = value;
            } else if (studentTest.form == null) {
                studentTest.form = value;
            } else if (studentTest.seatNumber == null) {
                studentTest.seatNumber = value;
            } else if (studentTest.schoolZone == null) {
                studentTest.schoolZone = value;
            } else {
                break;
            }
        }
        return StudentTest.isCorrectTest(studentTest) ? studentTest : null;

    }

    /**
     * 获取期末考试信息
     *
     * @param str
     * @return
     */
    public static ArrayList<StudentTest> getStudentTestList(String str) {
        ArrayList<StudentTest> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        String startKey = "<tr class=\"datelisthead\">\n";
        String endKey = "</table>";
        int startIndex = str.indexOf(startKey) + startKey.length();

        if (startIndex == -1) {
            return list;
        }
        str = str.substring(startIndex);
        int endIndex = str.indexOf(endKey);
        str = str.substring(0, endIndex);

        String[] strs = str.split("\n");
        for (int i = 1; i < strs.length; i++) {
            String line = strs[i];
            StudentTest studentTest = HTMLUtil.getStudentTest(line);
            if (studentTest != null) {
                list.add(studentTest);
            }
        }
        return list;
    }

    public static PersonalInfo getPersonalInfo(String str) {
        PersonalInfo personalInfo = new PersonalInfo();

        String photoUrlRegex = "(?<=<img id=\"xszp\" src=\").*?(?=\" alt=\"照片\")";
        String nameRegex = "(?<=<span id=\"" + "xm" + "\">).*?(?=</span>)";
        String sexRegex = "(?<=<span id=\"" + "lbl_xb" + "\">).*?(?=</span>)";
        String stuNumRegex = "(?<=<span id=\"" + "xh" + "\">).*?(?=</span>)";
        String birthTimeRegex = "(?<=<span id=\"" + "lbl_csrq" + "\">).*?(?=</span>)";
        String admissionTimeRegex = "(?<=<span id=\"" + "lbl_rxrq" + "\">).*?(?=</span>)";
        String facultyRegex = "(?<=<span id=\"" + "lbl_xy" + "\">).*?(?=</span>)";
        String professionRegex = "(?<=<span id=\"" + "lbl_zymc" + "\">).*?(?=</span>)";
        String classRegex = "(?<=<span id=\"" + "lbl_xzb" + "\">).*?(?=</span>)";
        String educationRegex = "(?<=<span id=\"" + "lbl_CC" + "\">).*?(?=</span>)";
        String ticketRegex = "(?<=<span id=\"" + "lbl_zkzh" + "\">).*?(?=</span>)";
        String idCardNumRegex = "(?<=<span id=\"" + "lbl_sfzh" + "\">).*?(?=</span>)";
        String cadidateNumRegex = "(?<=<span id=\"" + "lbl_ksh" + "\">).*?(?=</span>)";
        String phoneNumRegex = "(?<=<input name=\"TELNUMBER\" type=\"text\" value=\").*?(?=\" id=\"TELNUMBER\")";
        String emailRegex = "(?<=<input name=\"dzyxdz\" type=\"text\" value=\").*?(?=\" id=\"dzyxdz\")";
        String dormNumRegex = "(?<=<input name=\"ssh\" type=\"text\" value=\").*?(?=\" id=\"ssh\")";
        String middleSchoolRegex = "(?<=<input name=\"byzx\" type=\"text\" value=\").*?(?=\" id=\"byzx\")";
        String fatherNameRegex = "(?<=<input name=\"fqxm\" type=\"text\" value=\").*?(?=\" id=\"fqxm\")";
        String fatherOrganizationRegex = "(?<=<input name=\"fqdw\" type=\"text\" value=\").*?(?=\" id=\"fqdw\")";
        String fatherPhoneNumRegex = "(?<=<input name=\"fqdwdh\" type=\"text\" value=\").*?(?=\" id=\"fqdwdh\")";
        String motherNameRegex = "(?<=<input name=\"mqxm\" type=\"text\" value=\").*?(?=\" id=\"mqxm\")";
        String motherOrganizationRegex = "(?<=<input name=\"mqdw\" type=\"text\" value=\").*?(?=\" id=\"mqdw\")";
        String motherPhoneNumRegex = "(?<=<input name=\"mqdwdh\" type=\"text\" value=\").*?(?=\" id=\"mqdwdh\")";

        Matcher matcher;

        matcher = Pattern.compile(photoUrlRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mPhotoUrl = ConnectConfig.HOST + "/" + matcher.group();
        }
        matcher = Pattern.compile(nameRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mName = matcher.group();
        }
        matcher = Pattern.compile(sexRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mSex = matcher.group();
        }
        matcher = Pattern.compile(stuNumRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mStuNum = matcher.group();
        }
        matcher = Pattern.compile(birthTimeRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mBirthTime = matcher.group();
        }
        matcher = Pattern.compile(admissionTimeRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mAddmissionTime = matcher.group();
        }
        matcher = Pattern.compile(facultyRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mFaculty = matcher.group();
        }
        matcher = Pattern.compile(professionRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mProfession = matcher.group();
        }
        matcher = Pattern.compile(classRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mClass = matcher.group();
        }
        matcher = Pattern.compile(educationRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mEducation = matcher.group();
        }
        matcher = Pattern.compile(ticketRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mTicket = matcher.group();
        }
        matcher = Pattern.compile(idCardNumRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mIdCardNum = matcher.group();
        }
        matcher = Pattern.compile(cadidateNumRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mCandidate = matcher.group();
        }
        matcher = Pattern.compile(phoneNumRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mPhoneNum = matcher.group();
        }
        matcher = Pattern.compile(emailRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mEmail = matcher.group();
        }
        matcher = Pattern.compile(dormNumRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mDormNum = matcher.group();
        }
        matcher = Pattern.compile(middleSchoolRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mMiddleSchool = matcher.group();
        }
        matcher = Pattern.compile(fatherNameRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mFatherName = matcher.group();
        }
        matcher = Pattern.compile(fatherOrganizationRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mFatherOrganization = matcher.group();
        }
        matcher = Pattern.compile(fatherPhoneNumRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mFatherPhoneNum = matcher.group();
        }
        matcher = Pattern.compile(motherNameRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mMotherName = matcher.group();
        }
        matcher = Pattern.compile(motherOrganizationRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mMotherOrganization = matcher.group();
        }
        matcher = Pattern.compile(motherPhoneNumRegex).matcher(str);
        if (matcher.find()) {
            personalInfo.mMotherPhoneNum = matcher.group();
        }

        return personalInfo;

    }
}
