package com.les.povmt.util;

/**
 * Constants define global constants values used by many parts of this application.
 *
 * @author Samuel T. C. Santos
 */

public class Constants {

    //Constants used by request headers
    public static final String AUTHORIZATION_HEADER = "Authorization";

    //Tags used for HTTP responses
    public static final String TAG_STATUS = "status";

    //Tags used for Authentication
    public static final String TAG_TOKEN = "token";

    //Tags used for register new users
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASSWORD = "password";

    //Tag used in JSON response
    public static final String TAG_USER = "user";
    public static final String TAG_ID = "id";

    //Tags used for register new activity
    public static final String TAG_CREATOR = "creator";
    public static final String TAG_TITLE = "title";
    public static final String TAG_PRIORITY = "priority";
    public static final String TAG_CATEGORY = "category";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_CREATED_AT = "createdAt";
    public static final String TAG_UPDATED_AT = "updatedAt";
    public static final String TAG_ROOT = "activities";
    public static final String TAG_ACTIVITY = "activity";

    //Tags used for register new TI
    public static final String TAG_DURATION = "duration";
    public static final String TAG_INVESTEDTIMEAT = "investedTimeAt";

    // priority
    public static final String LOW = "LOW";
    public static final String MEDIUM = "MEDIUM";
    public static final String HIGH = "HIGH";

    // category
    public static final String WORK = "WORK";
    public static final String LEISURE = "LEISURE";


}

