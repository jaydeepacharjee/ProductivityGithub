package com.aroha.pet.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 *
 * @author Jaydeep
 */
public class QuestionQueryInfo implements Serializable {

	private String sqlStr;
	@Lob
	private String resultStr;
	@Lob
	private String exceptionStr;
	private String scenario;
	private int questionId;
	@Lob
	private String answer;
	private Timestamp createdAt;
	private String feedback;
	private String mentorName;
	private Timestamp feedbackDate;

	public QuestionQueryInfo(String exceptionStr) {
		this.exceptionStr = exceptionStr;
	}

	public QuestionQueryInfo() {
	}

	public String getSqlStr() {
		return sqlStr;
	}

	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}

	public String getResultStr() {
		return resultStr;
	}

	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}

	public String getExceptionStr() {
		return exceptionStr;
	}

	public void setExceptionStr(String exceptionStr) {
		this.exceptionStr = exceptionStr;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}



	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setFeedbackDate(Timestamp feedbackDate) {
		this.feedbackDate = feedbackDate;
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

	public Timestamp getFeedbackDate() {
		return feedbackDate;
	}

}
