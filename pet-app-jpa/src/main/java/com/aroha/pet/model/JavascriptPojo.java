package com.aroha.pet.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import com.aroha.pet.model.audit.DateAudit;

@Entity
public class JavascriptPojo extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long javascript_id;

    private long createdBy;

    @Lob
    private String javascriptstr;

    @Lob
    private String resultstr;

    @Lob
    private String error;

    private String scenario;

    private int questionId;

    public long getJavascript_id() {
        return javascript_id;
    }

    public void setJavascript_id(long javascript_id) {
        this.javascript_id = javascript_id;
    }

    public String getJavascriptstr() {
        return javascriptstr;
    }

    public void setJavascriptstr(String javascriptstr) {
        this.javascriptstr = javascriptstr;
    }

    public String getResultstr() {
        return resultstr;
    }

    public void setResultstr(String resultstr) {
        this.resultstr = resultstr;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "JavascriptPojo [javascript_id=" + javascript_id + ", createdBy=" + createdBy + ", javascriptstr="
                + javascriptstr + ", resultstr=" + resultstr + ", error=" + error + ", scenario=" + scenario
                + ", questionId=" + questionId + ", getJavascript_id()=" + getJavascript_id() + ", getJavascriptstr()="
                + getJavascriptstr() + ", getResultstr()=" + getResultstr() + ", getError()=" + getError()
                + ", getScenario()=" + getScenario() + ", getQuestionId()=" + getQuestionId() + ", getCreatedBy()="
                + getCreatedBy() + ", getCreatedAt()=" + getCreatedAt() + ", getUpdatedAt()=" + getUpdatedAt()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
                + "]";
    }

}
