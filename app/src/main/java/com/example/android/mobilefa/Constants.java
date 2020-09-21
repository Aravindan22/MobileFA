package com.example.android.mobilefa;

import java.util.HashMap;

public class Constants {
    public static int REG_NO;
    public static String PASSWORD;
    public static String DEP;
    public static String EMAIL;
    public static String NAME;
    public static int YEAR;
    public static String SECTION;
//    private static String url = "https://android-php.herokuapp.com/";
    private  static String url = "http://localhost:3001/api";
    public static String REGISTER_URL = url + "registerUser";
    public static String LOGIN_URL = url + "loginUser";
    public static String EVENT_URL = url + "addEvent";
    public static String FETCHDATA_URL = url + "fetchData";
    public  static  String WORKSHOP_URL = url + "addWorkshop";
    public  static  String COURSE_URL = url + "addCourse";
    public  static  String GENERAL_COUNSELING_URL = url + "addGeneralCounsellingData";
    public  static  String SUBJECT_MARK_UPDATION_URL = url +"subjectMarkUpdation";
    public  static  String GET_SUBJECTS = url +"getSubjects";
    public  static  HashMap<String,Integer> subjectandmark = new HashMap<>();
}
