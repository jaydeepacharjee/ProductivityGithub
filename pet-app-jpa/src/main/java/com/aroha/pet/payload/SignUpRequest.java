package com.aroha.pet.payload;

import javax.validation.constraints.*;

/**
 */
public class SignUpRequest {

    @NotBlank(message = "Name can't be null")
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
    @NotBlank
    @Size(max = 20)
    private String userType;//expected values are learner, mentor,admin
    private String phoneNo;
    private String altPhoneNo;
    private String primarySkills;
    private String secondarySkills;
    @NotBlank
    @Size(max = 150)
    private String address;
    private String dateOfJoin;
    private String soe;
    private String soeRef;
    private int dbId;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAltPhoneNo() {
        return altPhoneNo;
    }

    public void setAltPhoneNo(String altPhoneNo) {
        this.altPhoneNo = altPhoneNo;
    }

    public String getPrimarySkills() {
        return primarySkills;
    }

    public void setPrimarySkills(String primarySkills) {
        this.primarySkills = primarySkills;
    }

    public String getSecondarySkills() {
        return secondarySkills;
    }

    public void setSecondarySkills(String secondarySkills) {
        this.secondarySkills = secondarySkills;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(String dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public String getSoe() {
        return soe;
    }

    public void setSoe(String soe) {
        this.soe = soe;
    }

    public String getSoeRef() {
        return soeRef;
    }

    public void setSoeRef(String soeRef) {
        this.soeRef = soeRef;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    @Override
    public String toString() {
        return "SignUpRequest [name=" + name + ", email=" + email + ", userType=" + userType + ", phoneNo=" + phoneNo
                + ", altPhoneNo=" + altPhoneNo + ", primarySkills=" + primarySkills + ", secondarySkills="
                + secondarySkills + ", address=" + address + ", dateOfJoin=" + dateOfJoin + ", soe=" + soe + ", soeRef="
                + soeRef + ", dbId=" + dbId + ", password=" + password + "]";
    }

}
