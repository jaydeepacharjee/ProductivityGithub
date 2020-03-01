package com.aroha.pet.payload;

import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

public class JavaRequest {


	@NotNull
	@NotEmpty
	private String java;

	private String scenario;



	public String getJava() {
		return java;
	}

	public void setJava(String java) {
		this.java = java;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

}
