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
    private static final String URL_APPLICATION = "http://10.0.2.2:8080/api/application/";
    private static final String URL_APPLICATION_TYPE = "http://10.0.2.2:8080/api/application_type/";
    private static final String URL_SCHEDULE = "http://10.0.2.2:8080/api/schedules/";
    private static final String URL_SUBJECT = "http://10.0.2.2:8080/api/subject/";
    private static final String URL_DEVICE = "http://10.0.2.2:8080/api/device/";
    private static final String URL_ATTENDANCE = "http://10.0.2.2:8080/api/attendance/";
    private static final String URL_STUDENT_SUBJECT = "http://10.0.2.2:8080/api/student-subject/";
<<<<<<< HEAD
    private static final String URL_SCHEDULE_DETAIL = "http://10.0.2.2:8080/api/schedules_detail/";
    private static final String URL_STUDENT_MAJOR = "http://10.0.2.2:8080/api/student-major/";
    private static final String URL_ATTENDANCE_TRACKING = "http://10.0.2.2:8080/api/attendance_tracking/";
=======
    private static  final String URL_SCHEDULE_DETAIL = "http://10.0.2.2:8080/api/schedules_detail/";
    private static final String URL_STUDENT_MAJOR = "http://10.0.2.2:8080/api/student-major/";
    private static final String URL_ATTENDANCE_TRACKING = "http://10.0.2.2:8080:8080/api/attendance_tracking/";
>>>>>>> Develop


//    private static final String URL_ACCOUNT = "http://192.168.1.58:8080/api/accounts/";
//    private static final String URL_NEWS = "http://192.168.1.58:8080/api/news/";
//    private static final String URL_STUDENT = "http://192.168.1.58:8080/api/students/";
//    private static final String URL_PROFILE = "http://192.168.1.58:8080/api/profiles/";
//    private static final String URL_TEACHER = "http://192.168.1.58:8080/api/teachers/";
//    private static final String URL_STUDENT_CLASS = "http://192.168.1.58:8080/api/student-class/";
//    private static final String URL_CLASS = "http://192.168.1.58:8080/api/classes/";
//    private static final String URL_APPLICATION = "http://192.168.1.58:8080/api/application/";
//    private static final String URL_APPLICATION_TYPE = "http://192.168.1.58:8080/api/application_type/";
//    private static final String URL_SCHEDULE = "http://192.168.1.58:8080/api/schedules/";
//    private static final String URL_SUBJECT = "http://192.168.1.58:8080/api/subject/";
//    private static final String URL_DEVICE = "http://192.168.1.58:8080/api/device/";
//    private static final String URL_ATTENDANCE = "http://192.168.1.58:8080/api/attendance/";
//    private static final String URL_STUDENT_SUBJECT = "http://192.168.1.58:8080/api/student-subject/";
//    private static final String URL_SCHEDULE_DETAIL = "http://192.168.1.58:8080/api/schedules_detail/";
//    private static final String URL_STUDENT_MAJOR = "http://192.168.1.58:8080/api/student-major/";
//    private static final String URL_ATTENDANCE_TRACKING = "http://192.168.1.58:8080/api/attendance_tracking/";

    public static AccountAPI getAccountAPI() {
        return Retrofit.getRetrofitClient(URL_ACCOUNT).create(AccountAPI.class);
    }

    public static AttendanceTrackingApi getAttendanceTracking() {
        return Retrofit.getRetrofitClient(URL_ATTENDANCE_TRACKING).create(AttendanceTrackingApi.class);
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

    public static StudentClassAPI getStudentClass() {
        return Retrofit.getRetrofitClient(URL_STUDENT_CLASS).create(StudentClassAPI.class);
    }

    public static ClassAPI getClasses() {
        return Retrofit.getRetrofitClient(URL_CLASS).create(ClassAPI.class);
    }

    public static ApplicationAPI getApplicationApi() {
        return Retrofit.getRetrofitClient(URL_APPLICATION).create(ApplicationAPI.class);
    }

    public static ApplicationTypeAPI getApplicationType() {
        return Retrofit.getRetrofitClient(URL_APPLICATION_TYPE).create(ApplicationTypeAPI.class);
    }

    public static ScheduleAPI getScheduleAPI() {
        return Retrofit.getRetrofitClient(URL_SCHEDULE).create(ScheduleAPI.class);
    }

    public static SubjectAPI getSubject() {
        return Retrofit.getRetrofitClient(URL_SUBJECT).create(SubjectAPI.class);
    }

    public static DeviceAPI getDeviceAPI() {
        return Retrofit.getRetrofitClient(URL_DEVICE).create(DeviceAPI.class);
    }

    public static AttendanceAPI getAttendance() {
        return Retrofit.getRetrofitClient(URL_ATTENDANCE).create(AttendanceAPI.class);
    }

    public static ScheduleDetailAPI getScheduleDetail() {
        return Retrofit.getRetrofitClient(URL_SCHEDULE_DETAIL).create(ScheduleDetailAPI.class);
    }

    public static StudentMajorAPI getStudentMajor() {
        return Retrofit.getRetrofitClient(URL_STUDENT_MAJOR).create(StudentMajorAPI.class);
    }

    public static StudentSubjectAPI getStudentSubject() {
        return Retrofit.getRetrofitClient(URL_STUDENT_SUBJECT).create(StudentSubjectAPI.class);
    }

}
