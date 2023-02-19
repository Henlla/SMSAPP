package com.demo1.smsapp.api.utils;

import com.demo1.smsapp.api.*;

public class APIUtils {
//    private static final String URL_ACCOUNT = "http://10.0.2.2:8080/api/accounts/";
//    private static final String URL_NEWS = "http://10.0.2.2:8080/api/news/";
//    private static final String URL_STUDENT = "http://10.0.2.2:8080/api/students/";
//    private static final String URL_PROFILE = "http://10.0.2.2:8080/api/profiles/";
//    private static final String URL_TEACHER = "http://10.0.2.2:8080/api/teachers/";
//    private static final String URL_APPLICATION = "http://10.0.2.2:8080/api/application/";
//    private static final String URL_APPLICATION_TYPE = "http://10.0.2.2:8080/api/application_type/";
    private static final String URL_ACCOUNT = "http://192.168.1.55:8080/api/accounts/";
    private static final String URL_NEWS = "http://192.168.1.55:8080/api/news/";
    private static final String URL_STUDENT = "http://192.168.1.55:8080/api/students/";
    private static final String URL_PROFILE = "http://192.168.1.55:8080/api/profiles/";
    private static final String URL_TEACHER = "http://192.168.1.55:8080/api/teachers/";
    private static final String URL_APPLICATION = "http://192.168.1.55:8080/api/application/";
    private static final String URL_APPLICATION_TYPE = "http://192.168.1.55:8080/api/application_type/";

    public static AccountAPI getAccountAPI() {
        return Retrofit.getRetrofitClient(URL_ACCOUNT).create(AccountAPI.class);
    }

    public static NewsAPI getNews() {
        return Retrofit.getRetrofitClient(URL_NEWS).create(NewsAPI.class);
    }

    public static StudentAPI getStudent() {
        return Retrofit.getRetrofitClient(URL_STUDENT).create(StudentAPI.class);
    }

    public static ProfileAPI getProfile() {
        return Retrofit.getRetrofitClient(URL_PROFILE).create(ProfileAPI.class);
    }

    public static TeacherAPI getTeacher() {
        return Retrofit.getRetrofitClient(URL_TEACHER).create(TeacherAPI.class);
    }

    public static ApplicationApi Application() {
        return Retrofit.getRetrofitClient(URL_APPLICATION).create(ApplicationApi.class);
    }

    public static ApplicationTypeApi ApplicationType() {
        return Retrofit.getRetrofitClient(URL_APPLICATION_TYPE).create(ApplicationTypeApi.class);
    }
}
