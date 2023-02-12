package com.demo1.smsapp.api.utils;

import com.demo1.smsapp.api.*;

public class APIUtils {
    private static final String URL_ACCOUNT = "http://10.0.2.2:8080/api/accounts/";
    private static final String URL_NEWS = "http://10.0.2.2:8080/api/news/";
    private static final String URL_STUDENT = "http://10.0.2.2:8080/api/students/";
    private static final String URL_PROFILE = "http://10.0.2.2:8080/api/profiles/";
    private static final String URL_TEACHER = "http://10.0.2.2:8080/api/teachers/";
    public static AccountAPI getAccountAPI(){
        return Retrofit.getRetrofitClient(URL_ACCOUNT).create(AccountAPI.class);
    }
    public static NewsAPI getNews(){
        return Retrofit.getRetrofitClient(URL_NEWS).create(NewsAPI.class);
    }

    public static StudentAPI getStudent(){
        return Retrofit.getRetrofitClient(URL_STUDENT).create(StudentAPI.class);
    }
    public static ProfileAPI getProfile(){
        return Retrofit.getRetrofitClient(URL_PROFILE).create(ProfileAPI.class);
    }
    public static TeacherAPI getTeacher(){
        return Retrofit.getRetrofitClient(URL_TEACHER).create(TeacherAPI.class);
    }
}
