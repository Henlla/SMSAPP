package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Subject {

    @SerializedName("id")
    private int id;
    @SerializedName("subjectCode")
    private String subjectCode;
    @SerializedName("subjectName")
    private String subjectName;
    @SerializedName("fee")
    private Double fee;
    @SerializedName("slot")
    private Integer slot;
    @SerializedName("semesterId")
    private Integer semesterId;
    @SerializedName("majorId")
    private Integer majorId;
    @SerializedName("scheduleDetailsById")
    private List<ScheduleDetail> scheduleDetailsById;
    @SerializedName("studentSubjectsById")
    private List<StudentSubject> studentSubjectsById;
    @SerializedName("semesterBySemesterId")
    private Semester semesterBySemesterId;
    @SerializedName("majorByMajorId")
    private Major majorByMajorId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public List<ScheduleDetail> getScheduleDetailsById() {
        return scheduleDetailsById;
    }

    public void setScheduleDetailsById(List<ScheduleDetail> scheduleDetailsById) {
        this.scheduleDetailsById = scheduleDetailsById;
    }

    public List<StudentSubject> getStudentSubjectsById() {
        return studentSubjectsById;
    }

    public void setStudentSubjectsById(List<StudentSubject> studentSubjectsById) {
        this.studentSubjectsById = studentSubjectsById;
    }

    public Semester getSemesterBySemesterId() {
        return semesterBySemesterId;
    }

    public void setSemesterBySemesterId(Semester semesterBySemesterId) {
        this.semesterBySemesterId = semesterBySemesterId;
    }

    public Major getMajorByMajorId() {
        return majorByMajorId;
    }

    public void setMajorByMajorId(Major majorByMajorId) {
        this.majorByMajorId = majorByMajorId;
    }
}
