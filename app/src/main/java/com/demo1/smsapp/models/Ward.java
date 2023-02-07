package com.demo1.smsapp.models;

import java.util.List;

public class Ward {

    private Integer id;

    private String name;

    private String prefix;

    private Integer provinceId;

    private Integer districtId;

    private List<Profile> profilesById;

    private Province wardProvince;

    private District districtByDistrictId;

}
