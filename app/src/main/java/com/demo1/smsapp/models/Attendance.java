package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class Attendance {
    @SerializedName("id")
    private int id;
    @SerializedName("status")
    private Integer status;
    @SerializedName("date")
    private String date;
    @SerializedName("studentSubjectId")
    private Integer studentSubjectId;
    @SerializedName("note")
    private String note;
    @SerializedName("slot")
    private Integer slot;
    @SerializedName("shift")
    private String shift;

    @SerializedName("studentSubjectByStudentSubjectId")
    private StudentSubject studentSubjectByStudentSubjectId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStudentSubjectId() {
        return studentSubjectId;
    }

    public void setStudentSubjectId(Integer studentSubjectId) {
        this.studentSubjectId = studentSubjectId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public StudentSubject getStudentSubjectByStudentSubjectId() {
        return studentSubjectByStudentSubjectId;
    }

    public void setStudentSubjectByStudentSubjectId(StudentSubject studentSubjectByStudentSubjectId) {
        this.studentSubjectByStudentSubjectId = studentSubjectByStudentSubjectId;
    }
}
