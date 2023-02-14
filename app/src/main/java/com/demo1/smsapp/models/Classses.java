package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Classses {
    @SerializedName("id")
    private int id;
    @SerializedName("classCode")
    private String classCode;
    @SerializedName("limitStudent")
    private Integer limitStudent;
    @SerializedName("teacherId")
    private Integer teacherId;
    @SerializedName("majorId")
    private Integer majorId;
    @SerializedName("schedulesById")
    private List<Schedule> schedulesById;
    @SerializedName("studentClassById")
    private List<StudentClass> studentClassById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Integer getLimitStudent() {
        return limitStudent;
    }

    public void setLimitStudent(Integer limitStudent) {
        this.limitStudent = limitStudent;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public List<Schedule> getSchedulesById() {
        return schedulesById;
    }

    public void setSchedulesById(List<Schedule> schedulesById) {
        this.schedulesById = schedulesById;
    }

    public List<StudentClass> getStudentClassById() {
        return studentClassById;
    }

    public void setStudentClassById(List<StudentClass> studentClassById) {
        this.studentClassById = studentClassById;
    }
}
