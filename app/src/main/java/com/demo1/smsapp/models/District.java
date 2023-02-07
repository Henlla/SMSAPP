package com.demo1.smsapp.models;

import java.util.List;

public class District {
    private int id;
    private String name;
    private String prefix;
    private Integer provinceId;
    private Province districtProvince;
    private List<Profile> profilesById;
    private List<Ward> wardsById;
}
