package com.aroha.pet.payload;

import java.io.Serializable;
import java.util.List;

public class JavaResponse implements Serializable {
	
	private String java;
	private List javaresult;
	private String javaexception;
	private String javastatus;
	
	public String getJava() {
		return java;
	}
	public void setJava(String java) {
		this.java = java;
	}
	public List getJavaresult() {
		return javaresult;
	}
	public void setJavaresult(List javaresult) {
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

