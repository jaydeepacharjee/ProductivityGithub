package com.aroha.pet.payload;

public class DeleteDomainPayload {

	private int statusCode;
	private String statusMessage;

	public DeleteDomainPayload(String statusMessage, int statusCode) {
		this.statusMessage = statusMessage;
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}		
}
