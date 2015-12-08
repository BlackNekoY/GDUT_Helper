package com.rdc.gdut_helper.utils;

import android.text.TextUtils;
import android.util.Log;


import com.rdc.gdut_helper.model.Course;
import com.rdc.gdut_helper.model.LevelTest;
import com.rdc.gdut_helper.model.StudentTest;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HTMLUtil {

    private static final String SPACE = "&nbsp;";

    public static String getViewState(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String key = "name=\"__VIEWSTATE\"";
        int startIndex = str.indexOf(key);
        str = str.substring(startIndex);
        int endIndex = str.indexOf("\" />");
        str = str.substring(0 + key.length() + 1, endIndex);
        str = str.substring("value=\"".length());
        return str;
    }

    public static void logHeaders(HttpURLConnection conn) {
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entries = headerFields.entrySet();
        for (Iterator<Map.Entry<String, List<String>>> it = entries.iterator(); it.hasNext(); ) {
            Map.Entry<String, List<String>> entry = it.next();
            Log.e(entry.getKey(), entry.getValue().toString());
        }
    }

    public static String getStuName(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String key = "<span id=\"xhxm\">";
        int startIndex = str.indexOf(key);
        str = str.substring(startIndex);
        int endIndex = str.indexOf("同学");
        str = str.substring(0 + key.length(), endIndex);
        return str;
    }

    private static Course getCourseFromLine(String str) {
        Course course = new Course();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String startKey = "<td>";
        String endKey = "</td>";

        for (String temp : str.split(endKey)) {
            if (temp.length() < startKey.length()) {
                break;
            }
            String value = temp.substring(startKey.length());
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
            }
        }
        return Course.isCorrectCourse(course) ? course : null;
    }

    public static ArrayList<Course> getCourseList(String str) {
        ArrayList<Course> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        String startKey = "<td>课程代码</td><td>课程名称</td><td>课程性质</td><td>成绩</td><td>课程归属</td><td>补考成绩</td><td>重修成绩</td><td>学分</td><td>辅修标记</td>\n" +
                "\t</tr><tr>";
        String endKey = "</table>";
        int startIndex = str.indexOf(startKey);
        if (startIndex == -1) {
            return list;
        }
        str = str.substring(startIndex + startKey.length());
        int endIndex = str.indexOf(endKey);
        str = str.substring(0, endIndex);
        str = str.replaceAll("</tr><tr class=\"alt\">", "");
        str = str.replaceAll("</tr><tr>", "");
        str = str.trim();
        str = str.replaceAll("\t", "");

        for (String line : str.split("\n")) {
            if (line.length() < 2) {
                continue;
            }
            Course course = HTMLUtil.getCourseFromLine(line);
            if (course != null) {
                list.add(course);
            }
        }
        return list;
    }


    private static LevelTest getLevelTest(String str) {
        LevelTest levelTest = new LevelTest();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String startKey = "<td>";
        String endKey = "</td>";

        for (String temp : str.split(endKey)) {
            if (temp.length() < startKey.length()) {
                break;
            }
            String value = temp.substring(startKey.length());
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
        String startKey = "<td>学年</td><td>学期</td><td>等级考试名称</td><td>准考证号</td><td>考试日期</td><td>成绩</td><td>听力成绩</td><td>阅读成绩</td><td>写作成绩</td><td>综合成绩</td>\n" +
                "\t</tr><tr>";
        String endKey = "</table>";
        int startIndex = str.indexOf(startKey);
        if (startIndex == -1) {
            return list;
        }
        str = str.substring(startIndex + startKey.length());
        int endIndex = str.indexOf(endKey);
        str = str.substring(0, endIndex);
        str = str.replaceAll("</tr><tr class=\"alt\">", "");
        str = str.replaceAll("</tr><tr>", "");
        str = str.trim();
        str = str.replaceAll("\t", "");

        for (String line : str.split("\n")) {
            if (line.length() < 2) {
                continue;
            }
            LevelTest levelTest = HTMLUtil.getLevelTest(line);
            if (levelTest != null) {
                list.add(levelTest);
            }
        }
        return list;
    }

    private static StudentTest getStudentTest(String str) {
        StudentTest studentTest = new StudentTest();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String name = null;
        String startKey = "<td>";
        String endKey = "</td>";

        for (String temp : str.split(endKey)) {
            if (temp.length() < startKey.length()) {
                break;
            }
            String value = temp.substring(startKey.length());
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


    public static ArrayList<StudentTest> getStudentTestList(String str) {
        ArrayList<StudentTest> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        String startKey = "<td>选课课号</td><td>课程名称</td><td>姓名</td><td>考试时间</td><td>考试地点</td><td>考试形式</td><td>座位号</td><td>校区</td>";
        String endKey = "</table>";
        int startIndex = str.indexOf(startKey);
        if (startIndex == -1) {
            return list;
        }
        str = str.substring(startIndex + startKey.length());
        int endIndex = str.indexOf(endKey);
        str = str.substring(0, endIndex);
        str = str.replaceAll("</tr><tr class=\"alt\">", "");
        str = str.replaceAll("</tr><tr>", "");
        str = str.trim();
        str = str.replaceAll("\t", "");

        for (String line : str.split("\n")) {
            if (line.length() < 2) {
                continue;
            }
            StudentTest studentTest = HTMLUtil.getStudentTest(line);
            if (studentTest != null) {
                list.add(studentTest);
            }
        }

        return list;
    }
}
