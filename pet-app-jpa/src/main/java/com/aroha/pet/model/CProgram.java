package com.aroha.pet.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import com.aroha.pet.model.audit.DateAudit;

@Entity
public class CProgram extends DateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Long createdBy;
	
	@Lob
	private String cStr;
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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getcStr() {
		return cStr;
	}

	public void setcStr(String cStr) {
		this.cStr = cStr;
	}

	public String getResultstr() {
		return resultstr;
	}

	public void setResultstr(String resultstr) {
		this.resultstr = resultstr;
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
	
}
