package com.aroha.pet.payload;

import java.util.List;

import javax.persistence.Lob;

public class CResponse {

	@Lob
	private String cresult;
	private String cerror;
	private String cstatus;

	public String getCresult() {
		return cresult;
	}
	public void setCresult(String cresult) {
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
