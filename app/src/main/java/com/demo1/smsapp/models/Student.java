package com.demo1.smsapp.models;

import java.util.List;

public class Student {

    private Integer id;

    private String studentCard;

    private Integer profileId;

    private List<Application> applicationsById;

    private List<MajorStudent> majorStudentsById;

    private List<StudentSubject> studentSubjectsById;

    private List<StudentClass> studentClassById;

    private Profile studentByProfile;

}
