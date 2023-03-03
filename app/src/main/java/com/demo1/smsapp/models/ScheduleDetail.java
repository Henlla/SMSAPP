package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class ScheduleDetail {
    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private String date;
    @SerializedName("subjectId")
    private Integer subjectId;
    @SerializedName("scheduleId")
    private Integer scheduleId;
    @SerializedName("dayOfWeek")
    private String dayOfWeek;
    @SerializedName("subjectBySubjectId")
    private Subject subjectBySubjectId;
    @SerializedName("scheduleByScheduleId")
    private Schedule scheduleByScheduleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Subject getSubjectBySubjectId() {
        return subjectBySubjectId;
    }

    public void setSubjectBySubjectId(Subject subjectBySubjectId) {
        this.subjectBySubjectId = subjectBySubjectId;
    }

    public Schedule getScheduleByScheduleId() {
        return scheduleByScheduleId;
    }

    public void setScheduleByScheduleId(Schedule scheduleByScheduleId) {
        this.scheduleByScheduleId = scheduleByScheduleId;
    }
}
