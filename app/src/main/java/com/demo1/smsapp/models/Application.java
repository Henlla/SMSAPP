package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class Application {
    @SerializedName("id")
    private int id;
    @SerializedName("sendDate")
    private String sendDate;
    @SerializedName("status")
    private String status;
    @SerializedName("note")
    private String note;
    @SerializedName("file")
    private String file;
    @SerializedName("studentId")
    private Integer studentId;
    @SerializedName("applicationTypeId")
    private Integer applicationTypeId;
    @SerializedName("studentByStudentId")
    private Student studentByStudentId;
    @SerializedName("applicationTypeByApplicationTypeId")
    private ApplicationType applicationTypeByApplicationTypeId;

    public Application(ApplicationType applicationTypeByApplicationTypeId) {
        this.applicationTypeByApplicationTypeId = applicationTypeByApplicationTypeId;
    }

    public Application() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getApplicationTypeId() {
        return applicationTypeId;
    }

    public void setApplicationTypeId(Integer applicationTypeId) {
        this.applicationTypeId = applicationTypeId;
    }

    public Student getStudentByStudentId() {
        return studentByStudentId;
    }

    public void setStudentByStudentId(Student studentByStudentId) {
        this.studentByStudentId = studentByStudentId;
    }

    public ApplicationType getApplicationTypeByApplicationTypeId() {
        return applicationTypeByApplicationTypeId;
    }

    public void setApplicationTypeByApplicationTypeId(ApplicationType applicationTypeByApplicationTypeId) {
        this.applicationTypeByApplicationTypeId = applicationTypeByApplicationTypeId;
    }
}
