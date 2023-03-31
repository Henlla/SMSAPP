package com.demo1.smsapp.dto;

import java.util.List;

public class AttendanceView {
    private String date;
    private String classCode;
    private List<AttendanceDetailView> listAttendanceDetailView;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<AttendanceDetailView> getListAttendanceDetailView() {
        return listAttendanceDetailView;
    }

    public void setListAttendanceDetailView(List<AttendanceDetailView> listAttendanceDetailView) {
        this.listAttendanceDetailView = listAttendanceDetailView;
    }
}
