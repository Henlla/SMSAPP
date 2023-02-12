package com.demo1.smsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Account {
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("roleId")
    @Expose
    private Integer roleId;
    @SerializedName("roleByRoleId")
    @Expose
    private Role roleByRoleId;
    @SerializedName("accountProfile")
    @Expose
    private List<Profile> accountProfile;

    public Account() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Role getRoleByRoleId() {
        return roleByRoleId;
    }

    public void setRoleByRoleId(Role roleByRoleId) {
        this.roleByRoleId = roleByRoleId;
    }

    public List<Profile> getAccountProfile() {
        return accountProfile;
    }

    public void setAccountProfile(List<Profile> accountProfile) {
        this.accountProfile = accountProfile;
    }
}
