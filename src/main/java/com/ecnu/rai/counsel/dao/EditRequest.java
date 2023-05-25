package com.ecnu.rai.counsel.dao;
public class EditRequest {
    private String token;
    private String openid;
    private String realName;
    private String userName;
    private String phoneNumber;
    private String emergencyContactName;
    private String emergencyContactPhoneNumber;
    private Integer gender;

    public String getToken() {
        return token;
    }

    public Integer getGender() {
        return gender;
    }

    public String getOpenid() {
        return openid;
    }

    public String getRealName() {
        return realName;
    }

    public String getUsername() { return userName; }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhoneNumber() {
        return emergencyContactPhoneNumber;
    }

    public void setEmergencyContactPhoneNumber(String emergencyContactPhoneNumber) {
        this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
    }
}
