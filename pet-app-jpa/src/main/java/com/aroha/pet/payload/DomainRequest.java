package com.aroha.pet.payload;

import com.aroha.pet.model.Domain;
import com.aroha.pet.model.Function;
import com.aroha.pet.model.Question;
import com.aroha.pet.model.Scenario;

public class DomainRequest {


	private int domainId;
	private int functionId;
	private int scenarioId;
	private int questionId;

	private Domain domain;
	private Function function;
	private Scenario scenario;
	private Question question;




	public DomainRequest(int domainId, int functionId, int scenarioId, int questionId) {
		super();
		this.domainId = domainId;
		this.functionId = functionId;
		this.scenarioId = scenarioId;
		this.questionId = questionId;
	}


	public DomainRequest() {
		super();
	}


	public int getDomainId() {
		return domainId;
	}
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}
	public int getFunctionId() {
		return functionId;
	}
	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}
	public int getScenarioId() {
		return scenarioId;
	}
	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
	}
	public Scenario getScenario() {
		return scenario;
	}
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public Domain getDomain() {
		return domain;
	}
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

}
