package com.demo1.smsapp.dto;

public class FunctionModel {
    private String role;
    private String title;
    private String code;
    private Integer img;

    public FunctionModel() {

    }

    public FunctionModel(String role, String title, String code, Integer img) {
        this.role = role;
        this.title = title;
        this.code = code;
        this.img = img;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }
}
