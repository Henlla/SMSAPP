package com.demo1.smsapp.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("data")
    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
