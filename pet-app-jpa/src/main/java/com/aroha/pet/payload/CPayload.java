package com.aroha.pet.payload;

import com.aroha.pet.model.CProgram;

public class CPayload {

	private int questionId;
	private CProgram cProgram;

	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public CProgram getcProgram() {
		return cProgram;
	}
	public void setcProgram(CProgram cProgram) {
		this.cProgram = cProgram;
	}
		
}
