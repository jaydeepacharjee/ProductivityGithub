package com.aroha.pet.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Scenario implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scenarioId;
	@NotBlank
	@NotNull
	private String scenarioTitle;
	@NotBlank
	@NotNull
	private String scenarioType;
	@NotBlank
	@NotNull
	private String scenarioCode;
	@NotBlank
	@NotNull
	private String scenarioDesc;
	@Lob
	private byte[] image;
	
	private String fileName;
	
	@ManyToOne
	@JoinColumn(name="functionId")
	private Function function;
	
	@OneToMany(mappedBy = "scenario",cascade = {CascadeType.ALL,CascadeType.REMOVE})
	private Set<Question>ques=new HashSet<Question>();

	
	public int getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getScenarioTitle() {
		return scenarioTitle;
	}

	public String getScenarioType() {
		return scenarioType;
	}

	public String getScenarioCode() {
		return scenarioCode;
	}

	public String getScenarioDesc() {
		return scenarioDesc;
	}

	public byte[] getImage() {
		return image;
	}
	@JsonIgnore
	public Function getFunction() {
		return function;
	}

	public Set<Question> getQues() {
		return ques;
	}

	public void setScenarioTitle(String scenarioTitle) {
		this.scenarioTitle = scenarioTitle;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	public void setScenarioCode(String scenarioCode) {
		this.scenarioCode = scenarioCode;
	}

	public void setScenarioDesc(String scenarioDesc) {
		this.scenarioDesc = scenarioDesc;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	   @JsonIgnore
	public void setFunction(Function function) {
		this.function = function;
	}

	public void setQues(Set<Question> ques) {
		this.ques = ques;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


}
