package com.aroha.pet.model;

import java.util.ArrayList;
import java.util.List;
import com.aroha.pet.payload.MentorFeedbackResponse;

public class ShowMentorFeebackApi {
	
	private int statusCode;
    private String message;
    private String technologyName;
    private List<MentorFeedbackResponse> data = new ArrayList<>();
	public int getStatusCode() {
		return statusCode;
	}
		
	public ShowMentorFeebackApi(int statusCode, List<MentorFeedbackResponse> data, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}
	public ShowMentorFeebackApi(int statusCode, String message) {	
		this.statusCode = statusCode;
		this.message = message;
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
	public String getTechnologyName() {
		String tech="";
		for(MentorFeedbackResponse res:data) {
			tech=res.getTechnologyName();
		}
		technologyName=tech;
		return technologyName;
	}
	public void setTechnologyName(String technologyName) {
		this.technologyName = technologyName;
	}

	public List<MentorFeedbackResponse> getData() {
		return data;
	}
	public void setData(List<MentorFeedbackResponse> data) {
		this.data = data;
	}
    
}
