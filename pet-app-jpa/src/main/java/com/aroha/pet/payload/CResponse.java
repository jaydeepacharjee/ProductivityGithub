package com.aroha.pet.payload;

import java.util.List;

public class CResponse {
	
	private String cprogram;
	private List cresult;
	private String cerror;
	private String cstatus;
	public String getCprogram() {
		return cprogram;
	}
	public void setCprogram(String cprogram) {
		this.cprogram = cprogram;
	}
	public List getCresult() {
		return cresult;
	}
	public void setCresult(List cresult) {
		this.cresult = cresult;
	}
	
	public String getCerror() {
		return cerror;
	}
	public void setCerror(String cerror) {
		this.cerror = cerror;
	}
	public String getCstatus() {
		return cstatus;
	}
	public void setCstatus(String cstatus) {
		this.cstatus = cstatus;
	}
	
	

}
