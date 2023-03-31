package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class District {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("prefix")
    private String prefix;
    @SerializedName("provinceId")
    private Integer provinceId;
    @SerializedName("districtProvince")
    private Province districtProvince;
    @SerializedName("profilesById")
    private List<Profile> profilesById;
    @SerializedName("wardsById")
    private List<Ward> wardsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Province getDistrictProvince() {
        return districtProvince;
    }

    public void setDistrictProvince(Province districtProvince) {
        this.districtProvince = districtProvince;
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
