package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Major {
    @SerializedName("id")
    private int id;
    @SerializedName("majorCode")
    private String majorCode;
    @SerializedName("majorName")
    private String majorName;
    @SerializedName("majorStudentsById")
    private List<MajorStudent> majorStudentsById;
    @SerializedName("subjectsById")
    private List<Subject> subjectsById;

    @SerializedName("apartmentId")
    private Integer apartmentId;
    @SerializedName("apartmentByApartmentId")
    private Apartment apartmentByApartmentId;

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Apartment getApartmentByApartmentId() {
        return apartmentByApartmentId;
    }

    public void setApartmentByApartmentId(Apartment apartmentByApartmentId) {
        this.apartmentByApartmentId = apartmentByApartmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public List<MajorStudent> getMajorStudentsById() {
        return majorStudentsById;
    }

    public void setMajorStudentsById(List<MajorStudent> majorStudentsById) {
        this.majorStudentsById = majorStudentsById;
    }

    public List<Subject> getSubjectsById() {
        return subjectsById;
    }

    public void setSubjectsById(List<Subject> subjectsById) {
        this.subjectsById = subjectsById;
    }
}
