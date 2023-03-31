package com.demo1.smsapp.dto;

import java.util.List;

public class MarkGroupModel {
    private Integer semester;
    private List<MarkModel> list;

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<MarkModel> getList() {
        return list;
    }

    public void setList(List<MarkModel> list) {
        this.list = list;
    }
}
