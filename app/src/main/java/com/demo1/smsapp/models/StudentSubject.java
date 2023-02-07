package com.demo1.smsapp.models;

import java.util.List;

public class StudentSubject {

    private int id;

    private Integer subjectId;

    private Integer studentId;

    private String status;

    private List<Attendance> attendancesById;

    private List<Mark> marksById;

    private Subject subjectBySubjectId;

    private Student studentByStudentId;

}
