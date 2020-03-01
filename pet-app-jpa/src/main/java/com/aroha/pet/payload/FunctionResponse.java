package com.aroha.pet.payload;

import java.util.HashSet;
import java.util.Set;

public class FunctionResponse {

	private Set<FunctionResponsePayload> functionName = new HashSet<>();

	public Set<FunctionResponsePayload> getFunctionName() {
		return functionName;
	}

	public void setFunctionName(Set<FunctionResponsePayload> functionName) {
		this.functionName = functionName;
	}


}
