package com.demo1.smsapp.models;

public class StudentTakeMarkModel {
    private Student student;
    private Integer studentSubjectId;
    private Integer markId;
    private Double asm;
    private Double obj;
    private Integer timesUpdate;

    public Integer getTimesUpdate() {
        return timesUpdate;
    }

    public void setTimesUpdate(Integer timesUpdate) {
        this.timesUpdate = timesUpdate;
    }

    public Integer getMarkId() {
        return markId;
    }

    public void setMarkId(Integer markId) {
        this.markId = markId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getStudentSubjectId() {
        return studentSubjectId;
    }

    public void setStudentSubjectId(Integer studentSubjectId) {
        this.studentSubjectId = studentSubjectId;
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
