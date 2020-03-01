package com.aroha.pet.payload;

import java.util.Objects;

/**
 *
 * @author jaydeep
 */
public class ScenarioResponsePayload {
    
    private int scenario_id;
    private String scenarioTitle;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + this.scenario_id;
        hash = 43 * hash + Objects.hashCode(this.scenarioTitle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScenarioResponsePayload other = (ScenarioResponsePayload) obj;
        if (this.scenario_id != other.scenario_id) {
            return false;
        }
        if (!Objects.equals(this.scenarioTitle, other.scenarioTitle)) {
            return false;
        }
        return true;
    }

    public int getScenario_id() {
        return scenario_id;
    }

    public void setScenario_id(int scenario_id) {
        this.scenario_id = scenario_id;
    }

    public String getScenarioTitle() {
        return scenarioTitle;
    }

    public void setScenarioTitle(String scenarioTitle) {
        this.scenarioTitle = scenarioTitle;
    }
    
    
    

}
