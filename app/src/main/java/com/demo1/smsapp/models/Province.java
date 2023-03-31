package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Province {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("code")
    private String code;
    @SerializedName("districtsById")
    private List<District> districtsById;
    @SerializedName("profilesById")
    private List<Profile> profilesById;
    @SerializedName("wardsById")
    private List<Ward> wardsById;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<District> getDistrictsById() {
        return districtsById;
    }

    public void setDistrictsById(List<District> districtsById) {
        this.districtsById = districtsById;
    }

    public List<Profile> getProfilesById() {
        return profilesById;
    }

    public void setProfilesById(List<Profile> profilesById) {
        this.profilesById = profilesById;
    }

    public List<Ward> getWardsById() {
        return wardsById;
    }

    public void setWardsById(List<Ward> wardsById) {
        this.wardsById = wardsById;
    }
}
