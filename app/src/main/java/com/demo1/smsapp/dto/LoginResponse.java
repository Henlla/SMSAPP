package com.demo1.smsapp.dto;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
        private String message;
    @SerializedName("token")
        private String token;
    @SerializedName("data")
        private Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
