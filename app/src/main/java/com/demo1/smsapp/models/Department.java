package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class Department {
    @SerializedName("id")
    private int id;
    @SerializedName("departmentCode")
    private String departmentCode;
    @SerializedName("departmentName")
    private String departmentName;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
