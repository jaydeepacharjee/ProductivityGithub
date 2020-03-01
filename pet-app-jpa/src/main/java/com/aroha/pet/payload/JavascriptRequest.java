package com.aroha.pet.payload;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

public class JavascriptRequest {
	
	@NotNull
	@NotEmpty
	private String javascript;

	private String scenario;

	public String getJavascript() {
		return javascript;
	}

	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}
    
	
}
