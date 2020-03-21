package com.aroha.pet.payload;

import com.aroha.pet.model.CPojo;
import com.aroha.pet.model.JavaPojo;

public class CPayload {
	
	private int questionId;
	private CPojo cpojo;
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public CPojo getCpojo() {
		return cpojo;
	}
	public void setCpojo(CPojo cpojo) {
		this.cpojo = cpojo;
	}
	
	

}
