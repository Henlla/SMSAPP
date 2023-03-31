package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class AttendanceTracking {
    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private String date;
    @SerializedName("teacherId")
    private Integer teacherId;

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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
}
