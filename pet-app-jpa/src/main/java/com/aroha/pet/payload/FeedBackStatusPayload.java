package com.aroha.pet.payload;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FeedBackStatusPayload {

    private java.math.BigInteger created_by;
    private String name;
    private String created_at;
    private java.math.BigInteger noOfException;
    private java.math.BigInteger noOfScenario;
    private java.math.BigInteger noOfSqlStr;
    private java.math.BigDecimal productivity;

    public BigInteger getCreated_by() {
        return created_by;
    }

    public void setCreated_by(BigInteger created_by) {
        this.created_by = created_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public BigInteger getNoOfException() {
        return noOfException;
    }

    public void setNoOfException(BigInteger noOfException) {
        this.noOfException = noOfException;
    }

    public BigInteger getNoOfScenario() {
        return noOfScenario;
    }

    public void setNoOfScenario(BigInteger noOfScenario) {
        this.noOfScenario = noOfScenario;
    }

    public BigInteger getNoOfSqlStr() {
        return noOfSqlStr;
    }

    public void setNoOfSqlStr(BigInteger noOfSqlStr) {
        this.noOfSqlStr = noOfSqlStr;
    }

    public BigDecimal getProductivity() {
        return productivity;
    }

    public void setProductivity(BigDecimal productivity) {
        this.productivity = productivity;
    }


  
}
