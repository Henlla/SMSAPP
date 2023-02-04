package com.demo1.smsapp.models;

import java.util.List;

public class Major {
    private int id;
    private String majorCode;
    private String majorName;
    private List<MajorStudent> majorStudentsById;
    private List<Subject> subjectsById;
}
