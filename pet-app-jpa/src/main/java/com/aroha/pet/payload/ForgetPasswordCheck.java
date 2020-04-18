package com.aroha.pet.payload;

/**
 *
 * @author jaydeep
 */
public class ForgetPasswordCheck {

    private Long code;
    private String generatedDate;

    public ForgetPasswordCheck(Long code, String generatedDate) {
        this.code = code;
        this.generatedDate = generatedDate;
    }

    public ForgetPasswordCheck() {
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "ForgetPasswordCheck{" + "code=" + code + ", generatedDate=" + generatedDate + '}';
    }
    
    

}
