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
    private static String url = "https://android-php.herokuapp.com/";
    public static String REGISTER_URL = url + "register.php";
    public static String LOGIN_URL = url + "login.php";
    public static String EVENT_URL = url + "insertevent.php";
    public static String FETCHDATA_URL = url + "retrieveCreds.php";
    public  static  String WORKSHOP_URL = url + "workshopUpdate.php";
    public  static  String GENERAL_COUNSELING_URL = url + "workshopUpdate.php";
    public  static  HashMap<String,Integer> subjectandmark = new HashMap<>();
}
