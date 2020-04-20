package com.aroha.pet.payload;

import com.aroha.pet.model.PythonPojo;

public class PythonPayload {
	
	private int questionId;
	private PythonPojo pythonpojo;
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public PythonPojo getPythonpojo() {
		return pythonpojo;
	}
	public void setPythonpojo(PythonPojo pythonpojo) {
		this.pythonpojo = pythonpojo;
	}
	
	

}
