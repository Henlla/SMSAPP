package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Teacher {
    @SerializedName("id")
    private Integer id;
    @SerializedName("profileId")
    private Integer profileId;

    @SerializedName("teacherCard")
    private String teacherCard;
    @SerializedName("profileByProfileId")
    private Profile profileByProfileId;
    @SerializedName("teacherClass")
    private List<Classses> teacherClass;
    public Teacher() {
    }

    public String getTeacherCard() {
        return teacherCard;
    }

    public void setTeacherCard(String teacherCard) {
        this.teacherCard = teacherCard;
    }

    public List<Classses> getTeacherClass() {
        return teacherClass;
    }

    public void setTeacherClass(List<Classses> teacherClass) {
        this.teacherClass = teacherClass;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Profile getProfileByProfileId() {
        return profileByProfileId;
    }

    public void setProfileByProfileId(Profile profileByProfileId) {
        this.profileByProfileId = profileByProfileId;
    }
}
