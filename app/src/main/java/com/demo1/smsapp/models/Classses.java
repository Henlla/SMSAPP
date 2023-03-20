package com.demo1.smsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Classses {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("classCode")
    @Expose
    private String classCode;
    @SerializedName("limitStudent")
    @Expose
    private Integer limitStudent;
    @SerializedName("teacherId")
    @Expose
    private Integer teacherId;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("shift")
    @Expose
    private String shift;
    @SerializedName("onDeleted")
    @Expose
    private boolean onDeleted;
    @SerializedName("classStatus")
    @Expose
    private String classStatus;
    @SerializedName("majorId")
    @Expose
    private Integer majorId;
    @SerializedName("schedulesById")
    @Expose
    private List<Schedule> schedulesById;
    @SerializedName("studentClassById")
    @Expose
    private List<StudentClass> studentClassById;
    @SerializedName("major")
    @Expose
    private Major major;
    @SerializedName("teacher")
    @Expose
    private Teacher teacher;

    public boolean isOnDeleted() {
        return onDeleted;
    }

    public void setOnDeleted(boolean onDeleted) {
        this.onDeleted = onDeleted;
    }

    public List<StudentClass> getStudentClassById() {
        return studentClassById;
    }

    public void setStudentClassById(List<StudentClass> studentClassById) {
        this.studentClassById = studentClassById;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }


    public String getStatus() {
        return classStatus;
    }

    public void setStatus(String status) {
        this.classStatus = status;
    }

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

//    public List<StudentClass> getStudentClassById() {
//        return studentClassById;
//    }
//
//    public void setStudentClassById(List<StudentClass> studentClassById) {
//        this.studentClassById = studentClassById;
//    }

}
