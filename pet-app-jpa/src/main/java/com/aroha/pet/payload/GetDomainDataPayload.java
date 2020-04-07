package com.aroha.pet.payload;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jaydeep
 */
public class GetDomainDataPayload {

    private int statusCode;
    private String message;
    private List data = new ArrayList<>();

    public GetDomainDataPayload(int statusCode, List data, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public GetDomainDataPayload(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public GetDomainDataPayload() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
