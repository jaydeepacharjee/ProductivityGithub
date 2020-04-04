package com.aroha.pet.payload;

public class ForgetPasswordPayload {

	private int statusCode;
	private boolean status;
	private String message;
	
	public ForgetPasswordPayload() {
		super();
	}
	public ForgetPasswordPayload(int statusCode, boolean status, String message) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
