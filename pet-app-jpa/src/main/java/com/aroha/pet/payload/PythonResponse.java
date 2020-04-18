package com.aroha.pet.payload;

import java.util.List;

import javax.persistence.Lob;

public class PythonResponse {

	@Lob
	private String pythonresult;
	private String pythonerror;
	private String pythonstatus;


	public String getPythonresult() {
		return pythonresult;
	}
	public void setPythonresult(String pythonresult) {
		this.pythonresult = pythonresult;
	}
	public String getPythonerror() {
		return pythonerror;
	}
	public void setPythonerror(String pythonerror) {
		this.pythonerror = pythonerror;
	}
	public String getPythonstatus() {
		return pythonstatus;
	}
	public void setPythonstatus(String pythonstatus) {
		this.pythonstatus = pythonstatus;
	}
	
	

}
