package com.aroha.pet.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Domain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int domainId;
    @NotBlank
    @NotNull
    private String domainName;
    @NotBlank
    @NotNull
    private String domainCode;
    @NotBlank
    @NotNull
    private String domainDesc;
    @NotBlank
    @NotNull
    private String domainWebsite;

    @ManyToOne
    @JoinColumn(name = "technologyId")
    private Technology technology;
    
    @OneToMany(mappedBy = "domain",cascade = {CascadeType.ALL,CascadeType.REMOVE})
    private Set<Function> functions = new HashSet<Function>();

    public int getDomainId() {
        return domainId;
    }

    public void setDomainId(int domainId) {
        this.domainId = domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDomainDesc() {
        return domainDesc;
    }

    public void setDomainDesc(String domainDesc) {
        this.domainDesc = domainDesc;
    }

    public String getDomainWebsite() {
        return domainWebsite;
    }

    public void setDomainWebsite(String domainWebsite) {
        this.domainWebsite = domainWebsite;
    }

    public Set<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(Set<Function> functions) {
        this.functions = functions;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

}
