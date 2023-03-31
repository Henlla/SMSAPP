package com.demo1.smsapp.models;


import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("id")
    private Integer id;
    @SerializedName("roomCode")
    private String roomCode;
    @SerializedName("departmentId")
    private Integer departmentId;
    @SerializedName("departmentByDepartmentId")
    private Department departmentByDepartmentId;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Department getDepartmentByDepartmentId() {
        return departmentByDepartmentId;
    }

    public void setDepartmentByDepartmentId(Department departmentByDepartmentId) {
        this.departmentByDepartmentId = departmentByDepartmentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}
