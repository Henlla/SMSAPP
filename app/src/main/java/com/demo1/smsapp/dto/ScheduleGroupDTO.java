package com.demo1.smsapp.dto;

import java.time.LocalDate;
import java.util.List;

public class ScheduleGroupDTO {
    private LocalDate date;
    private List<ScheduleDetailModel> list;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ScheduleDetailModel> getList() {
        return list;
    }

    public void setList(List<ScheduleDetailModel> list) {
        this.list = list;
    }
}
