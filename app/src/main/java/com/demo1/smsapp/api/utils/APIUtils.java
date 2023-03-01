package com.demo1.smsapp.api.utils;

import com.demo1.smsapp.api.*;

public class APIUtils {
    private static final String URL_ACCOUNT = "http://10.0.2.2:8080/api/accounts/";
    private static final String URL_NEWS = "http://10.0.2.2:8080/api/news/";
    private static final String URL_STUDENT = "http://10.0.2.2:8080/api/students/";
    private static final String URL_PROFILE = "http://10.0.2.2:8080/api/profiles/";
    private static final String URL_TEACHER = "http://10.0.2.2:8080/api/teachers/";
    private static final String URL_STUDENT_CLASS = "http://10.0.2.2:8080/api/student-class/";
    private static final String URL_CLASS = "http://10.0.2.2:8080/api/classes/";
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
    public static StudentClassAPI getStudentClass(){
        return Retrofit.getRetrofitClient(URL_STUDENT_CLASS).create(StudentClassAPI.class);
    }
    public static ClassAPI getClasses(){
        return Retrofit.getRetrofitClient(URL_CLASS).create(ClassAPI.class);
    }
}
