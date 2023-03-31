package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class Apartment {
    @SerializedName("id")
    private int id;
    @SerializedName("apartmentCode")
    private String apartmentCode;
    @SerializedName("apartmentName")
    private String apartmentName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApartmentCode() {
        return apartmentCode;
    }

    public void setApartmentCode(String apartmentCode) {
        this.apartmentCode = apartmentCode;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }
}
