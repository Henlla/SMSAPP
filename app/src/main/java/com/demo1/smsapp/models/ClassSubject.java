package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class ClassSubject {
    @SerializedName("id")
    private int id;
    @SerializedName("subjectId")
    private Integer subjectId;
    @SerializedName("classId")
    private Integer classId;
    @SerializedName("startDate")
    private String startDate;
    @SerializedName("endDate")
    private String endDate;
    @SerializedName("subjectBySubjectId")
    private Subject subjectBySubjectId;
    @SerializedName("classsesByClassId")
    private Classses classsesByClassId;

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

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
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

    public Subject getSubjectBySubjectId() {
        return subjectBySubjectId;
    }

    public void setSubjectBySubjectId(Subject subjectBySubjectId) {
        this.subjectBySubjectId = subjectBySubjectId;
    }

    public Classses getClasssesByClassId() {
        return classsesByClassId;
    }

    public void setClasssesByClassId(Classses classsesByClassId) {
        this.classsesByClassId = classsesByClassId;
    }
}
