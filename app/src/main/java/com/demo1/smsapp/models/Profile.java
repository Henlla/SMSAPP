package com.demo1.smsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("id")
    private int id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("provinceId")
    @Expose
    private Integer provinceId;
    @SerializedName("districtId")
    @Expose
    private Integer districtId;
    @SerializedName("wardId")
    @Expose
    private Integer wardId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("avartarUrl")
    @Expose
    private String avartarUrl;
    @SerializedName("avatarPath")
    @Expose
    private String avatarPath;
    @SerializedName("identityCard")
    @Expose
    private String identityCard;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("profileProvince")
    @Expose
    private Province profileProvince;
    @SerializedName("districtByDistrictId")
    @Expose
    private District districtByDistrictId;
    @SerializedName("wardByWardId")
    @Expose
    private Ward wardByWardId;
    @SerializedName("accountByAccountId")
    @Expose
    private Account accountByAccountId;
    @SerializedName("staffById")
    @Expose
    private Staff staffById;
    @SerializedName("teachersById")
    @Expose
    private Teacher teachersById;
    @SerializedName("studentsById")
    @Expose
    private Student studentsById;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvartarUrl() {
        return avartarUrl;
    }

    public void setAvartarUrl(String avartarUrl) {
        this.avartarUrl = avartarUrl;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Province getProfileProvince() {
        return profileProvince;
    }

    public void setProfileProvince(Province profileProvince) {
        this.profileProvince = profileProvince;
    }

    public District getDistrictByDistrictId() {
        return districtByDistrictId;
    }

    public void setDistrictByDistrictId(District districtByDistrictId) {
        this.districtByDistrictId = districtByDistrictId;
    }

    public Ward getWardByWardId() {
        return wardByWardId;
    }

    public void setWardByWardId(Ward wardByWardId) {
        this.wardByWardId = wardByWardId;
    }

    public Account getAccountByAccountId() {
        return accountByAccountId;
    }

    public void setAccountByAccountId(Account accountByAccountId) {
        this.accountByAccountId = accountByAccountId;
    }

    public Staff getStaffById() {
        return staffById;
    }

    public void setStaffById(Staff staffById) {
        this.staffById = staffById;
    }

    public Teacher getTeachersById() {
        return teachersById;
    }

    public void setTeachersById(Teacher teachersById) {
        this.teachersById = teachersById;
    }

    public Student getStudentsById() {
        return studentsById;
    }

    public void setStudentsById(Student studentsById) {
        this.studentsById = studentsById;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
