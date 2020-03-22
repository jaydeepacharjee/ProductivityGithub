package com.aroha.pet.payload;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jaydeep
 */
public class CData {

    private int statusCode;
    private String message;

    private List<CReportAnalysisPayload> data = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<CReportAnalysisPayload> getData() {
        return data;
    }

    public void setData(List<CReportAnalysisPayload> data) {
        this.data = data;
    }
}
