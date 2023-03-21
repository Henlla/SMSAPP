package com.demo1.smsapp.models;


import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("id")
    private Integer id;
    @SerializedName("roomCode")
    private String roomCode;


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
