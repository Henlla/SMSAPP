package com.demo1.smsapp.dto;

import com.demo1.smsapp.models.Subject;

public class MarkModel {
    private Subject subject;
    private Double asm;
    private Double obj;
    private Integer semester;

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Double getAsm() {
        return asm;
    }

    public void setAsm(Double asm) {
        this.asm = asm;
    }

    public Double getObj() {
        return obj;
    }

    public void setObj(Double obj) {
        this.obj = obj;
    }
}
