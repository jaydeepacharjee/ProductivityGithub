package com.aroha.pet.payload;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Jaydeep
 */
public class Message {

    private HttpStatus status;
    private List<Query> queryResponse = new ArrayList<>();

    public List<Query> getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(List<Query> queryResponse) {
        this.queryResponse = queryResponse;
    }

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
