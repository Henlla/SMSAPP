package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class Role {
    @SerializedName("id")
    private int id;
    @SerializedName("roleName")
    private String roleName;
    @SerializedName("accountsById")
    private Account accountsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Account getAccountsById() {
        return accountsById;
    }

    public void setAccountsById(Account accountsById) {
        this.accountsById = accountsById;
    }
}
