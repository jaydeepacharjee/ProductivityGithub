package com.aroha.pet.payload;

import java.util.List;

public class CResponse {
	
	private String cScenario;
	private List cResult;
	private String cException;
	private String cStatus;
	public String getcScenario() {
		return cScenario;
	}
	public void setcScenario(String cScenario) {
		this.cScenario = cScenario;
	}
	public List getcResult() {
		return cResult;
	}
	public void setcResult(List cResult) {
		this.cResult = cResult;
	}
	public String getcException() {
		return cException;
	}
	public void setcException(String cException) {
		this.cException = cException;
	}
	public String getcStatus() {
		return cStatus;
	}
	public void setcStatus(String cStatus) {
		this.cStatus = cStatus;
	}
	
	
	
}
