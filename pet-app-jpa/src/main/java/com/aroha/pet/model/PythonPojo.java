package com.aroha.pet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.data.annotation.CreatedDate;

import com.aroha.pet.model.audit.DateAudit;

@Entity
public class PythonPojo extends DateAudit {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long python_id;

	private long createdBy;

	@Lob
	private String pythonstr;

	@Lob
	private String resultstr;

	@Lob
	private String error;

	private String scenario;

	private int questionId;

	public long getPython_id() {
		return python_id;
	}

	public void setPython_id(long python_id) {
		this.python_id = python_id;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public String getPythonstr() {
		return pythonstr;
	}

	public void setPythonstr(String pythonstr) {
		this.pythonstr = pythonstr;
	}

	public String getResultstr() {
		return resultstr;
	}

	public void setResultstr(String resultstr) {
		this.resultstr = resultstr;
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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "PythonPojo [python_id=" + python_id + ", createdBy=" + createdBy + ", pythonstr=" + pythonstr
				+ ", resultstr=" + resultstr + ", error=" + error + ", scenario=" + scenario + ", questionId="
				+ questionId + ", getPython_id()=" + getPython_id() + ", getCreatedBy()=" + getCreatedBy()
				+ ", getPythonstr()=" + getPythonstr() + ", getResultstr()=" + getResultstr() + ", getScenario()="
				+ getScenario() + ", getQuestionId()=" + getQuestionId() + ", getError()=" + getError()
				+ ", getCreatedAt()=" + getCreatedAt() + ", getUpdatedAt()=" + getUpdatedAt() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
