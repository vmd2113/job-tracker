package com.duongw.common.constant;


public final class ApiPath {


    public static final String API_PATH = "/api";
    public static final String API_VERSION = "/v1";
    public static final String API_PATH_VERSION = API_PATH + API_VERSION;

    // common service
     public static final String API_COMMON = API_PATH_VERSION + "/cm";


    public static final String API_CONFIG_VIEW = API_COMMON + "/config-view";
    public static final String API_FILE = API_COMMON + "/files";
    public static final String API_USER = API_COMMON + "/users";  // API User
    public static final String API_USER_ROLE = API_COMMON + "/user-role";
    public static final String API_CATEGORY = API_COMMON + "/categories";
    public static final String API_ITEM = API_COMMON + "/items";
    public static final String API_DEPARTMENT = API_COMMON + "/departments";
    public static final String API_AUTH = API_PATH_VERSION + "/auth";


    // wfm service
    public  static final String API_WORKFORCE = API_PATH_VERSION + "/wfm";
    public static final String API_WORK_TYPE = API_WORKFORCE + "/work-type";
    public static final String API_WORK_CONFIG_BUSINESS = API_WORKFORCE + "/work-config";
    public static final String API_WORK_HISTORY = API_WORKFORCE + "/work-history";
    public static final String API_WORKS = API_WORKFORCE + "/works";

    // opm service

    public static final String API_OPM = API_PATH_VERSION + "/opm";
}

