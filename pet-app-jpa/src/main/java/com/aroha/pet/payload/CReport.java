package com.aroha.pet.payload;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Jaydeep
 */
public class CReport {

    private java.math.BigInteger userId;
    private String created_at;
    private String name;
    private java.math.BigInteger noOfError;
    private java.math.BigInteger noOfQuestion;
    private java.math.BigInteger noOfAttempt;
    private java.math.BigDecimal productivity;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getNoOfError() {
        return noOfError;
    }

    public void setNoOfError(BigInteger noOfError) {
        this.noOfError = noOfError;
    }

    public BigInteger getNoOfQuestion() {
        return noOfQuestion;
    }

    public void setNoOfQuestion(BigInteger noOfQuestion) {
        this.noOfQuestion = noOfQuestion;
    }

    public BigInteger getNoOfAttempt() {
        return noOfAttempt;
    }

    public void setNoOfAttempt(BigInteger noOfAttempt) {
        this.noOfAttempt = noOfAttempt;
    }

    public BigDecimal getProductivity() {
        return productivity;
    }

    public void setProductivity(BigDecimal productivity) {
        this.productivity = productivity;
    }

   

}
