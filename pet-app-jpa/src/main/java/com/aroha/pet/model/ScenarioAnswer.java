package com.aroha.pet.model;

public class ScenarioAnswer {
	
	private String answer;
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public ScenarioAnswer(String answer) {
		super();
		this.answer = answer;
	}

	public ScenarioAnswer() {
		super();
	}

	@Override
	public String toString() {
		return "ScenarioAnswer [answer=" + answer + "]";
	}
	
}
