package com.aroha.pet.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.NaturalId;
import com.aroha.pet.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "email"
    })
})
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 40)
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


    @NotBlank
    @Size(max = 100)
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "db_info_users",
            joinColumns = {
                @JoinColumn(name = "users_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "dbs_id")})
    private Set<DbInfo> dbs = new HashSet<DbInfo>();

    public User() {

    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    @JsonIgnore
    public Set<DbInfo> getDbs() {
        return dbs;
    }

    @JsonIgnore
    public void setDbs(Set<DbInfo> dbs) {
        this.dbs = dbs;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", phoneNo=" + phoneNo + ", altPhoneNo="
                + altPhoneNo + ", primarySkills=" + primarySkills + ", secondarySkills=" + secondarySkills
                + ", address=" + address + ", dateOfJoin=" + dateOfJoin + ", soe=" + soe + ", soeRef=" + soeRef
                + ", password=" + password + ", roles=" + roles + ", dbs=" + dbs + "]";
    }

}
