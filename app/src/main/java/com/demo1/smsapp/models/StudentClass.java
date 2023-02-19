package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class StudentClass {
    @SerializedName("id")
    private int id;
    @SerializedName("studentId")

    private Integer studentId;
    @SerializedName("classId")

    private Integer classId;
    @SerializedName("classStudentByStudent")

    private Student classStudentByStudent;
    @SerializedName("classStudentByClass")

    private Classses classStudentByClass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Student getClassStudentByStudent() {
        return classStudentByStudent;
    }

    public void setClassStudentByStudent(Student classStudentByStudent) {
        this.classStudentByStudent = classStudentByStudent;
    }

    public Classses getClassStudentByClass() {
        return classStudentByClass;
    }

    public void setClassStudentByClass(Classses classStudentByClass) {
        this.classStudentByClass = classStudentByClass;
    }
}
