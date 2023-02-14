package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class MajorStudent {
    @SerializedName("id")
    private int id;
    @SerializedName("majorId")
    private Integer majorId;
    @SerializedName("studentId")
    private Integer studentId;
    @SerializedName("majorByMajorId")
    private Major majorByMajorId;
    @SerializedName("studentByStudentId")
    private Student studentByStudentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Major getMajorByMajorId() {
        return majorByMajorId;
    }

    public void setMajorByMajorId(Major majorByMajorId) {
        this.majorByMajorId = majorByMajorId;
    }

    public Student getStudentByStudentId() {
        return studentByStudentId;
    }

    public void setStudentByStudentId(Student studentByStudentId) {
        this.studentByStudentId = studentByStudentId;
    }
}
