package com.demo1.smsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Student {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("studentCard")
    @Expose
    private String studentCard;
    @SerializedName("profileId")
    @Expose
    private Integer profileId;
    @SerializedName("applicationsById")
    @Expose
    private List<Application> applicationsById;
    @SerializedName("majorStudentsById")
    @Expose
    private List<MajorStudent> majorStudentsById;
    @SerializedName("studentSubjectsById")
    @Expose
    private List<StudentSubject> studentSubjectsById;
//    @SerializedName("studentClassById")
//    @Expose
//    private List<StudentClass> studentClassById;
    @SerializedName("studentByProfile")
    @Expose
    private Profile studentByProfile;

    public Student(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentCard() {
        return studentCard;
    }

    public void setStudentCard(String studentCard) {
        this.studentCard = studentCard;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public List<Application> getApplicationsById() {
        return applicationsById;
    }

    public void setApplicationsById(List<Application> applicationsById) {
        this.applicationsById = applicationsById;
    }

    public List<MajorStudent> getMajorStudentsById() {
        return majorStudentsById;
    }

    public void setMajorStudentsById(List<MajorStudent> majorStudentsById) {
        this.majorStudentsById = majorStudentsById;
    }

    public List<StudentSubject> getStudentSubjectsById() {
        return studentSubjectsById;
    }

    public void setStudentSubjectsById(List<StudentSubject> studentSubjectsById) {
        this.studentSubjectsById = studentSubjectsById;
    }

//    public List<StudentClass> getStudentClassById() {
//        return studentClassById;
//    }
//
//    public void setStudentClassById(List<StudentClass> studentClassById) {
//        this.studentClassById = studentClassById;
//    }

    public Profile getStudentByProfile() {
        return studentByProfile;
    }

    public void setStudentByProfile(Profile studentByProfile) {
        this.studentByProfile = studentByProfile;
    }
}
