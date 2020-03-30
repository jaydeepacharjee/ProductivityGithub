package com.aroha.pet.payload;

import java.util.List;

import javax.persistence.Lob;

/**
 *
 * @author Jaydeep
 */
public class QueryObject {
    
   
    private String doaminName;
    private String functionName;
    private String scenario;
    private String sqlStr;
    private String answer;
    private String exceptionStr;
    private List resultStr;
    private String createdAt;
    private int questionId;
    private String feedBackDate;
    private String feedback;
    private String mentorName;
  

    public QueryObject() {
		super();
	}

	public QueryObject(String exceptionStr) {
		super();
		this.exceptionStr = exceptionStr;
	}

	public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public String getExceptionStr() {
        return exceptionStr;
    }

    public void setExceptionStr(String exceptionStr) {
        this.exceptionStr = exceptionStr;
    }

    public List getResultStr() {
        return resultStr;
    }

    public void setResultStr(List resultStr) {
        this.resultStr = resultStr;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getFeedBackDate() {
        return feedBackDate;
    }

    public void setFeedBackDate(String feedBackDate) {
        this.feedBackDate = feedBackDate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }
    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getDoaminName() {
        return doaminName;
    }

    public void setDoaminName(String doaminName) {
        this.doaminName = doaminName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
