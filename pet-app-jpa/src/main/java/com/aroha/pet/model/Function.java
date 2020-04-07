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

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Table;

@Entity
@Table(name="FunctionTable")
public class Function implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int functionId;

    @NotNull
    private double businessSize;

    @NotNull
    private double businessValue;
    @NotBlank
    @NotNull
    private String functionDesc;
    @NotBlank
    @NotNull
    private String functionName;

    @ManyToOne
    @JoinColumn(name = "domainId")
    private Domain domain;

    @OneToMany(mappedBy = "function", cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private Set<Scenario> scenario = new HashSet<Scenario>();

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public double getBusinessSize() {
        return businessSize;
    }

    public double getBusinessValue() {
        return businessValue;
    }

    public String getFunctionDesc() {
        return functionDesc;
    }

    public String getFunctionName() {
        return functionName;
    }

    @JsonIgnore
    public Domain getDomain() {
        return domain;
    }

    public Set<Scenario> getScenario() {
        return scenario;
    }

    public void setBusinessSize(double businessSize) {
        this.businessSize = businessSize;
    }

    public void setBusinessValue(double businessValue) {
        this.businessValue = businessValue;
    }

    public void setFunctionDesc(String functionDesc) {
        this.functionDesc = functionDesc;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @JsonIgnore
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public void setScenario(Set<Scenario> scenario) {
        this.scenario = scenario;
    }

}
