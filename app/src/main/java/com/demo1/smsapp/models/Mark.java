package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;

public class Mark {
    @SerializedName("id")
    private int id;
    @SerializedName("asm")
    private Double asm;
    @SerializedName("obj")
    private Double obj;
    @SerializedName("studentSubjectId")
    private Integer studentSubjectId;
    @SerializedName("studentSubjectByStudentSubjectId")
    private StudentSubject studentSubjectByStudentSubjectId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Integer getStudentSubjectId() {
        return studentSubjectId;
    }

    public void setStudentSubjectId(Integer studentSubjectId) {
        this.studentSubjectId = studentSubjectId;
    }

    public StudentSubject getStudentSubjectByStudentSubjectId() {
        return studentSubjectByStudentSubjectId;
    }

    public void setStudentSubjectByStudentSubjectId(StudentSubject studentSubjectByStudentSubjectId) {
        this.studentSubjectByStudentSubjectId = studentSubjectByStudentSubjectId;
    }
}
