package com.aroha.pet.payload;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Lob;

public class JavaResponse implements Serializable {

	@Lob
	private String javaresult;
	private String javaexception;
	private String javastatus;
    
	
	public String getJavaresult() {
		return javaresult;
	}
	public void setJavaresult(String javaresult) {
		this.javaresult = javaresult;
	}
	public String getJavaexception() {
		return javaexception;
	}
	public void setJavaexception(String javaexception) {
		this.javaexception = javaexception;
	}
	public String getJavastatus() {
		return javastatus;
	}
	public void setJavastatus(String javastatus) {
		this.javastatus = javastatus;
	}
  
	
}

