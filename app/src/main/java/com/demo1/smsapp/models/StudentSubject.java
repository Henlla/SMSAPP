package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentSubject {
    @SerializedName("id")
    private int id;
    @SerializedName("subjectId")

    private Integer subjectId;
    @SerializedName("studentId")

    private Integer studentId;
    @SerializedName("status")

    private String status;
    @SerializedName("attendancesById")
    private List<Attendance> attendancesById;
    @SerializedName("marksById")

    private List<Mark> marksById;
    @SerializedName("subjectBySubjectId")

    private Subject subjectBySubjectId;
    @SerializedName("studentByStudentId")

    private Student studentByStudentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Attendance> getAttendancesById() {
        return attendancesById;
    }

    public void setAttendancesById(List<Attendance> attendancesById) {
        this.attendancesById = attendancesById;
    }

    public List<Mark> getMarksById() {
        return marksById;
    }

    public void setMarksById(List<Mark> marksById) {
        this.marksById = marksById;
    }

    public Subject getSubjectBySubjectId() {
        return subjectBySubjectId;
    }

    public void setSubjectBySubjectId(Subject subjectBySubjectId) {
        this.subjectBySubjectId = subjectBySubjectId;
    }

    public Student getStudentByStudentId() {
        return studentByStudentId;
    }

    public void setStudentByStudentId(Student studentByStudentId) {
        this.studentByStudentId = studentByStudentId;
    }
}
