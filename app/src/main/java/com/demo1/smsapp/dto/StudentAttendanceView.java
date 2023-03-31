package com.demo1.smsapp.dto;

public class StudentAttendanceView {
    private Integer id;
    private Integer student_subjectId;
    private String classId;
    private String studentName;
    private String note;
    private Integer isPresent;

    public Integer isPresent() {
        return isPresent;
    }

    public Integer getIsPresent() {
        return isPresent;
    }

    public Integer getStudent_subjectId() {
        return student_subjectId;
    }

    public void setStudent_subjectId(Integer student_subjectId) {
        this.student_subjectId = student_subjectId;
    }

    public void setIsPresent(Integer isPresent) {
        this.isPresent = isPresent;
    }

    public void setPresent(Integer present) {
        isPresent = present;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
