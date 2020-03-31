package com.aroha.pet.payload;

import java.util.ArrayList;
import java.util.List;

public class JavaData {

    private int statusCode;
    private String message;
    private List<JavaReportAnalysisPayload> data = new ArrayList<>();
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
	public List<JavaReportAnalysisPayload> getData() {
		return data;
	}
	public void setData(List<JavaReportAnalysisPayload> data) {
		this.data = data;
	}
    
    

}
