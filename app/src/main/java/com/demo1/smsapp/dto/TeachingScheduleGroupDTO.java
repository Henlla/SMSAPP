package com.demo1.smsapp.dto;

import java.time.LocalDate;
import java.util.List;

public class TeachingScheduleGroupDTO {
    private LocalDate date;
    private List<TeachingScheduleModel> teachingScheduleModels;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TeachingScheduleModel> getTeachingScheduleModels() {
        return teachingScheduleModels;
    }

    public void setTeachingScheduleModels(List<TeachingScheduleModel> teachingScheduleModels) {
        this.teachingScheduleModels = teachingScheduleModels;
    }
}
