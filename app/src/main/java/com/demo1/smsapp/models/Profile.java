package com.demo1.smsapp.models;

public class Profile {
    private int id;
    private String firstName;
    private String lastName;
    private String dob;
    private Integer provinceId;
    private Integer districtId;
    private Integer wardId;
    private String address;
    private String phone;
    private String email;
    private String avartarUrl;
    private String avatarPath;
    private String identityCard;
    private Integer accountId;
    private Province profileProvince;
    private District districtByDistrictId;
    private Ward wardByWardId;
    private Account accountByAccountId;
    private Staff staffById;
    private Teacher teachersById;
    private Student studentsById;
}
