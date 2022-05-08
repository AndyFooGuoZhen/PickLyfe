package com.example.userprofilescreen.util;

public class ConstUrl {
    // Local Host
    public static final String BASE_URL = "http://10.0.2.2:8080/";
    // Remote Host
//    public static final String BASE_URL = "http://coms-309-001.class.las.iastate.edu:8080/";

    public static Long USERNAME_ID = (long) 1;
    public static String USERNAME = "";
    public static boolean isAdmin = false;

    public static final String GET_LOGIN_URL = BASE_URL + "user/login/";
    public static final String GET_USERNAME_ID_URL = BASE_URL + "user/";
    public static final String GET_RESET_MAIL_URL = BASE_URL + "user/forgotpassword/";
    public static final String GET_IMAGE_URL = BASE_URL + "userprofile/getImg/" + USERNAME_ID;
    public static final String GET_USERPROFILE_DETAILS = BASE_URL + "userprofile/" + USERNAME_ID;
    public static final String GET_PERKLIST_URL = BASE_URL + "perk/all";
    public static final String GET_ANNOUNCEMENT_URL = BASE_URL + "ann/all";
    public static final String GET_PATCH_URL = BASE_URL + "patch/all";


    public static final String PUT_USERPROFILE_URL = BASE_URL + "userprofile/";
    public static final String PUT_USERNAME_URL = "/name/";
    public static final String PUT_USERABOUTME_URL = "/am/";
    public static final String PUT_IMAGE_URL = BASE_URL + "userprofile/upload/";

    public static final String POST_USERACCOUNT_URL = BASE_URL + "user/post";
    public static final String POST_ANNOUNCEMENT_URL = BASE_URL + "ann/newPost";
    public static final String POST_PATCH_URL = BASE_URL + "patch/newPost";

}
