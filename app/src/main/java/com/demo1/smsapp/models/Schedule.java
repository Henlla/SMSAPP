package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule {
    @SerializedName("id")
    private int id;
    @SerializedName("startDate")
    private String startDate;
    @SerializedName("endDate")
    private String endDate;
    @SerializedName("classId")
    private Integer classId;
    @SerializedName("classsesByClassId")
    private Classses classsesByClassId;
    @SerializedName("scheduleDetailsById")
    private List<ScheduleDetail> scheduleDetailsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Classses getClasssesByClassId() {
        return classsesByClassId;
    }

    public void setClasssesByClassId(Classses classsesByClassId) {
        this.classsesByClassId = classsesByClassId;
    }

    public List<ScheduleDetail> getScheduleDetailsById() {
        return scheduleDetailsById;
    }

    public void setScheduleDetailsById(List<ScheduleDetail> scheduleDetailsById) {
        this.scheduleDetailsById = scheduleDetailsById;
    }
}
