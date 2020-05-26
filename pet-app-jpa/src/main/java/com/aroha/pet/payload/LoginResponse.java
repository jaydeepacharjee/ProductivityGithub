package com.aroha.pet.payload;

import java.util.List;

/**
 *
 * @author Jaydeep  | Date: 15 May, 2020  11:35:37 AM
 */
public class LoginResponse {
    
    private int statusCode;
    private String message;
    private List<LoginPayloadResponse> data;

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

    public List<LoginPayloadResponse> getData() {
        return data;
    }

    public void setData(List<LoginPayloadResponse> data) {
        this.data = data;
    }

    public LoginResponse(int statusCode, String message, List<LoginPayloadResponse> data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

   
    public LoginResponse() {
    }

    public LoginResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    
}
