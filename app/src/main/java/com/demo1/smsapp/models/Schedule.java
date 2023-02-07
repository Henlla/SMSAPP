package com.demo1.smsapp.models;

import java.util.List;

public class Schedule {

    private int id;

    private String startDate;

    private String endDate;

    private Integer classId;

    private Classses classsesByClassId;

    private List<ScheduleDetail> scheduleDetailsById;
}
