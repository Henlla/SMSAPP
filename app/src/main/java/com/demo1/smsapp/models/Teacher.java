package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class Teacher {
    @SerializedName("id")
    private Integer id;
    @SerializedName("profileId")
    private Integer profileId;
    @SerializedName("profileByProfileId")
    private Profile profileByProfileId;

    public Teacher() {
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
