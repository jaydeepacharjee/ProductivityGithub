package com.aroha.pet.payload;

import java.util.HashSet;
import java.util.Set;

public class ScenarioResponse {

	private Set<ScenarioResponsePayload> scenarioTitle = new HashSet<>();

	public Set<ScenarioResponsePayload> getScenarioTitle() {
		return scenarioTitle;
	}

	public void setScenarioTitle(Set<ScenarioResponsePayload> scenarioTitle) {
		this.scenarioTitle = scenarioTitle;
	}


}
