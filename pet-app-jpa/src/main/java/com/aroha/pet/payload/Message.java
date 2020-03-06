package com.aroha.pet.payload;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Jaydeep
 */
public class Message {

    private int status;
    private List<QueryObject> queryResponse = new ArrayList<>();

    public List<QueryObject> getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(List<QueryObject> queryResponse) {
        this.queryResponse = queryResponse;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


}
