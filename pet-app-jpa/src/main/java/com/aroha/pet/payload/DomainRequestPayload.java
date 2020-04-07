package com.aroha.pet.payload;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jaydeep
 */
public class DomainRequestPayload {

    private Set<DomainResponsePayload> domainName = new HashSet<>();
    private Set<FunctionResponsePayload> functionName = new HashSet<>();
    private Set<ScenarioResponsePayload> scenarioTitle = new HashSet<>();

    public Set<DomainResponsePayload> getDomainName() {
        return domainName;
    }

    public void setDomainName(Set<DomainResponsePayload> domainName) {
        this.domainName = domainName;
    }

    public Set<FunctionResponsePayload> getFunctionName() {
        return functionName;
    }

    public void setFunctionName(Set<FunctionResponsePayload> functionName) {
        this.functionName = functionName;
    }

    public Set<ScenarioResponsePayload> getScenarioTitle() {
        return scenarioTitle;
    }

    public void setScenarioTitle(Set<ScenarioResponsePayload> scenarioTitle) {
        this.scenarioTitle = scenarioTitle;
    }

}
