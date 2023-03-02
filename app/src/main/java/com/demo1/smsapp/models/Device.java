package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("id")
    private Integer id;
    @SerializedName("deviceToken")
    private String deviceToken;
    @SerializedName("accountId")
    private Integer accountId;
    @SerializedName("accountDevice")
    private Account accountDevice;


    public Device() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Account getAccountDevice() {
        return accountDevice;
    }

    public void setAccountDevice(Account accountDevice) {
        this.accountDevice = accountDevice;
    }
}
