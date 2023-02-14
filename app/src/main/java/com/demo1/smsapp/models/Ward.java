package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ward {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("prefix")
    private String prefix;
    @SerializedName("provinceId")
    private Integer provinceId;
    @SerializedName("districtId")
    private Integer districtId;
    @SerializedName("profilesById")
    private List<Profile> profilesById;
    @SerializedName("wardProvince")
    private Province wardProvince;
    @SerializedName("districtByDistrictId")
    private District districtByDistrictId;

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

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public List<Profile> getProfilesById() {
        return profilesById;
    }

    public void setProfilesById(List<Profile> profilesById) {
        this.profilesById = profilesById;
    }

    public Province getWardProvince() {
        return wardProvince;
    }

    public void setWardProvince(Province wardProvince) {
        this.wardProvince = wardProvince;
    }

    public District getDistrictByDistrictId() {
        return districtByDistrictId;
    }

    public void setDistrictByDistrictId(District districtByDistrictId) {
        this.districtByDistrictId = districtByDistrictId;
    }
}
