package com.aroha.pet.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;

import org.springframework.data.annotation.CreatedDate;

@Entity
public class JavaPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@CreatedDate
	@Column(insertable = true, updatable = false)
	private String createdAt;
	
	private long createdBy;

	@Lob
	private String javastr;

	public String getResultstr() {
		return resultstr;
	}
	public void setResultstr(String resultstr) {
		this.resultstr = resultstr;
	}
	@Lob
	private String resultstr;

	@Lob
	private String exceptionstr;

	private String scenario;

	private int questionId; 

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getJavastr() {
		return javastr;
	}
	public void setJavastr(String javastr) {
		this.javastr = javastr;
	}


	public String getExceptionstr() {
		return exceptionstr;
	}
	public void setExceptionstr(String exceptionstr) {
		this.exceptionstr = exceptionstr;
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

	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	
	public long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	@Override
	public String toString() {
		return "JavaPojo [id=" + id + ", javastr=" + javastr + ", resultstr=" + resultstr + ", exceptionstr="
				+ exceptionstr + ", scenario=" + scenario + ", questionId=" + questionId + "]";
	}



}

