package com.demo1.smsapp.dto;

import com.demo1.smsapp.models.Classses;
import com.demo1.smsapp.models.Schedule;
import com.demo1.smsapp.models.ScheduleDetail;
import com.demo1.smsapp.models.Subject;
import com.google.gson.annotations.SerializedName;


public class ScheduleDetailModel {
    private int id;
    private String date;
    private Integer subjectId;
    private Integer scheduleId;
    private String dayOfWeek;
    private String timeStart;
    private String timeEnd;
    private Integer slot;
    private String roomCode;
    private String teacherName;
    private String className;
    private Subject subjectBySubjectId;
    private Schedule scheduleByScheduleId;


    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }



    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Subject getSubjectBySubjectId() {
        return subjectBySubjectId;
    }

    public void setSubjectBySubjectId(Subject subjectBySubjectId) {
        this.subjectBySubjectId = subjectBySubjectId;
    }

    public Schedule getScheduleByScheduleId() {
        return scheduleByScheduleId;
    }

    public void setScheduleByScheduleId(Schedule scheduleByScheduleId) {
        this.scheduleByScheduleId = scheduleByScheduleId;
    }
}
