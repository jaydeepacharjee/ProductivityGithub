package com.aroha.pet.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class PriceModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int priceId;
	private String course_name;
	private String course_category;
	@Lob
	private byte[] course_image;
	private double course_price;
	private double course_rating;
	private String course_rated_number_count;
	private String course_description;
	public int getPriceId() {
		return priceId;
	}
	public void setPriceId(int priceId) {
		this.priceId = priceId;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	public String getCourse_category() {
		return course_category;
	}
	public void setCourse_category(String course_category) {
		this.course_category = course_category;
	}
	public byte[] getCourse_image() {
		return course_image;
	}
	public void setCourse_image(byte[] course_image) {
		this.course_image = course_image;
	}
	public double getCourse_price() {
		return course_price;
	}
	public void setCourse_price(double course_price) {
		this.course_price = course_price;
	}
	public double getCourse_rating() {
		return course_rating;
	}
	public void setCourse_rating(double course_rating) {
		this.course_rating = course_rating;
	}
	public String getCourse_rated_number_count() {
		return course_rated_number_count;
	}
	public void setCourse_rated_number_count(String course_rated_number_count) {
		this.course_rated_number_count = course_rated_number_count;
	}
	public String getCourse_description() {
		return course_description;
	}
	public void setCourse_description(String course_description) {
		this.course_description = course_description;
	}
	
	
}
