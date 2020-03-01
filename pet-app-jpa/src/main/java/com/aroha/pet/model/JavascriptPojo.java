package com.aroha.pet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.data.annotation.CreatedDate;

@Entity
public class JavascriptPojo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long javascript_id;
	
	@CreatedDate
	@Column(insertable=true, updatable=false)
	private String createdAt;
	
	@Lob
	private String javascriptstr;
	
	private String resultstr;
	
	private String scenario;
	
	private int questionId;
	
	public long getJavascript_id() {
		return javascript_id;
	}
	public void setJavascript_id(long javascript_id) {
		this.javascript_id = javascript_id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
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
	@Override
	public String toString() {
		return "JavascriptPojo [javascript_id=" + javascript_id + ", createdAt=" + createdAt + ", javascriptstr="
				+ javascriptstr + ", resultstr=" + resultstr + ", scenario="
				+ scenario + ", questionId=" + questionId + "]";
	}
	
}
