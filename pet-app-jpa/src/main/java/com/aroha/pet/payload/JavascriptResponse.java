package com.aroha.pet.payload;

import java.util.List;

import javax.persistence.Lob;

public class JavascriptResponse {

	@Lob
	private String javascriptresult;
	private String javascripterror;
	private String javascriptstatus;

    
	public String getJavascriptresult() {
		return javascriptresult;
	}
	public void setJavascriptresult(String javascriptresult) {
		this.javascriptresult = javascriptresult;
	}
	public String getJavascriptstatus() {
		return javascriptstatus;
	}
	public void setJavascriptstatus(String javascriptstatus) {
		this.javascriptstatus = javascriptstatus;
	}
	public String getJavascripterror() {
		return javascripterror;
	}
	public void setJavascripterror(String javascripterror) {
		this.javascripterror = javascripterror;
	}
	
	
}
