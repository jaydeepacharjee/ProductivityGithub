package com.aroha.pet.payload;

import java.util.HashSet;
import java.util.Set;

public class DomainResponse {
	
    private Set<DomainResponsePayload> domainName = new HashSet<>();

	public Set<DomainResponsePayload> getDomainName() {
		return domainName;
	}

	public void setDomainName(Set<DomainResponsePayload> domainName) {
		this.domainName = domainName;
	}
    
    

}
