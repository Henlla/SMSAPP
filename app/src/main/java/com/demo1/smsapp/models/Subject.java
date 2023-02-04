package com.demo1.smsapp.models;

import java.util.List;

public class Subject {

    private int id;

    private String subjectCode;
 
    private String subjectName;

    private Double fee;

    private Integer slot;

    private Integer semesterId;

    private Integer majorId;

    private List<ScheduleDetail> scheduleDetailsById;

    private List<StudentSubject> studentSubjectsById;

    private Semester semesterBySemesterId;

    private Major majorByMajorId;

}
