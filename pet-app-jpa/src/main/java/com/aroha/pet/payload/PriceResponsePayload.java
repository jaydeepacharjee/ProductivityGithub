package com.aroha.pet.payload;

import java.util.ArrayList;
import java.util.List;
import com.aroha.pet.model.PriceModel;

public class PriceResponsePayload {
	
	private int statusCode;
	private String mesage;
	private List<PriceModel>courseData=new ArrayList<>();
	private PriceModel model;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMesage() {
		return mesage;
	}
	public void setMesage(String mesage) {
		this.mesage = mesage;
	}

	public PriceResponsePayload(int statusCode, String mesage, List<PriceModel> courseData) {
		super();
		this.statusCode = statusCode;
		this.mesage = mesage;
		this.courseData = courseData;
	}
	public List<PriceModel> getCourseData() {
		return courseData;
	}
	public void setCourseData(List<PriceModel> courseData) {
		this.courseData = courseData;
	}
	public PriceResponsePayload() {
		super();
	}
	public PriceResponsePayload(int statusCode, String mesage) {
		super();
		this.statusCode = statusCode;
		this.mesage = mesage;
	}
	public PriceModel getModel() {
		return model;
	}
	public void setModel(PriceModel model) {
		this.model = model;
	}
	public PriceResponsePayload(int statusCode, String mesage, PriceModel model) {
		super();
		this.statusCode = statusCode;
		this.mesage = mesage;
		this.model = model;
	}
  
	
	
	
}
